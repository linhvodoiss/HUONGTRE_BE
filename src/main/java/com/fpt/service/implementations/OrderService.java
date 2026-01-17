package com.fpt.service.implementations;

import com.fpt.dto.*;
import com.fpt.entity.*;
import com.fpt.form.OrderCreateRequest;
import com.fpt.form.OrderItemRequest;
import com.fpt.repository.*;
import com.fpt.service.interfaces.IBranchService;
import com.fpt.service.interfaces.IOrderService;
import com.fpt.specification.BranchSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final CustomerRepository customerRepository;
    @Override
    @Transactional
    public OrderDTO createOrder(OrderCreateRequest request) {

        // =========================
        // 1. FIND OR CREATE CUSTOMER
        // =========================
        Customer customer = customerRepository
                .findByPhone(request.getReceiverPhone())
                .orElseGet(() -> customerRepository.save(
                        Customer.builder()
                                .phone(request.getReceiverPhone())
                                .totalOrders(0)
                                .totalSpent(0.0)
                                .build()
                ));

        // =========================
        // 2. CREATE ORDER (CHƯA TÍNH TIỀN)
        // =========================
        Order order = Order.builder()
                .status("NEW")
                .customer(customer)
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .deliveryAddress(request.getDeliveryAddress())
                .totalAmount(0)
                .items(new ArrayList<>())
                .build();

        order = orderRepository.save(order); // ⚠️ BẮT BUỘC SAVE TRƯỚC

        int totalAmount = 0;

        // =========================
        // 3. CREATE ORDER ITEMS
        // =========================
        for (OrderItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .basePrice(product.getPrice())
                    .options(new ArrayList<>())
                    .build();

            double itemTotal = product.getPrice() * itemReq.getQuantity();

            if (itemReq.getOptionIds() != null) {
                for (Long optionId : itemReq.getOptionIds()) {

                    Option option = optionRepository.findById(optionId)
                            .orElseThrow(() -> new RuntimeException("Option not found"));

                    OrderItemOption oio = OrderItemOption.builder()
                            .orderItem(orderItem)
                            .optionId(option.getId())
                            .optionName(option.getName())
                            .optionPrice(option.getPrice())
                            .build();

                    orderItem.getOptions().add(oio);
                    itemTotal += option.getPrice();
                }
            }

            totalAmount += itemTotal;
            order.getItems().add(orderItem); // ⚠️ KHÔNG save orderItem
        }

        // =========================
        // 4. UPDATE ORDER TOTAL
        // =========================
        order.setTotalAmount(totalAmount);

        // =========================
        // 5. UPDATE CUSTOMER STATS
        // =========================
        customer.setTotalOrders(customer.getTotalOrders() + 1);
        customer.setTotalSpent(customer.getTotalSpent() + totalAmount);

        customerRepository.save(customer);

        // =========================
        // 6. SAVE ORDER (CASCADE)
        // =========================
        orderRepository.save(order);

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }


    // ================= MAPPER =================
    private OrderDTO mapToResponse(Order order) {
        Customer customer = order.getCustomer();
        CustomerDTO buyerDto = null;
        if (customer != null) {
            buyerDto = CustomerDTO.builder()
                    .id(customer.getId())
                    .phone(customer.getPhone())
                    .note(customer.getNote())
                    .build();
        }
        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .customer(buyerDto)
                .note(order.getNote())
                .createdAt(order.getCreatedAt())
                .items(
                        order.getItems().stream().map(item ->
                                OrderItemDTO.builder()
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getName())
                                        .quantity(item.getQuantity())
                                        .basePrice(item.getBasePrice())
                                        .options(
                                                item.getOptions().stream().map(opt ->
                                                        OrderItemOptionDTO.builder()
                                                                .optionId(opt.getOptionId())
                                                                .optionName(opt.getOptionName())
                                                                .optionPrice(opt.getOptionPrice())
                                                                .build()
                                                ).toList()
                                        )
                                        .build()
                        ).toList()
                )
                .build();
    }
}
