package com.fpt.init;

import com.fpt.entity.*;
import com.fpt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final LicenseRepository licenseRepository;
    private final OptionRepository optionRepository;
    private final BranchRepository branchRepository;
    private final BranchProductRepository branchProductRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        if (optionRepository.count() == 0) {
            List<Option> options = Arrays.asList(
                    Option.builder().name("5 documents per month").isActive(true).isDeleted(false).build(),
                    Option.builder().name("Unlimited access").isActive(true).isDeleted(false).build(),
                    Option.builder().name("Priority support").isActive(true).isDeleted(false).build()
            );
            optionRepository.saveAll(options);
        }

        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                    User.builder().userName("admin").email("admin@gmail.com")
                            .password("$2a$10$RAU5Vl1A6Iheyeg2MSBlVeLRLpH2kRSpredJkzJIm72ZscI6pg/62")
                            .firstName("Nguyen Van").lastName("A").phoneNumber("0987654321")
                            .role(Role.ADMIN).status(UserStatus.ACTIVE).isActive(true)
                            .avatarUrl("").isDeleted(false).build(),
                    User.builder().userName("CaoVanBay").email("Bay@gmail.com")
                            .password("$2a$10$RAU5Vl1A6Iheyeg2MSBlVeLRLpH2kRSpredJkzJIm72ZscI6pg/62")
                            .firstName("Cao Van").lastName("Bay").phoneNumber("0999999999")
                            .role(Role.CUSTOMER).status(UserStatus.ACTIVE).isActive(true)
                            .avatarUrl("").isDeleted(false).build(),
                    User.builder().userName("LeThiTam").email("Tam@gmail.com")
                            .password("$2a$10$RAU5Vl1A6Iheyeg2MSBlVeLRLpH2kRSpredJkzJIm72ZscI6pg/62")
                            .firstName("Le Thi").lastName("Tam").phoneNumber("0912345678")
                            .role(Role.CUSTOMER).status(UserStatus.ACTIVE).isActive(true)
                            .avatarUrl("").isDeleted(false).build()
            );
            userRepository.saveAll(users);
        }

        if (subscriptionPackageRepository.count() == 0) {
            Option option1 = optionRepository.findById(1L).orElseThrow();
            Option option2 = optionRepository.findById(2L).orElseThrow();
            Option option3 = optionRepository.findById(3L).orElseThrow();

            List<SubscriptionPackage> packagesToSave = new ArrayList<>();

            for (SubscriptionPackage.BillingCycle cycle : SubscriptionPackage.BillingCycle.values()) {
                SubscriptionPackage runtimePackage = SubscriptionPackage.builder()
                        .name("Runtime Package")
                        .description("hihi")
                        .price(getFixedPrice(cycle))
                        .discount(0f)
                        .billingCycle(cycle)
                        .typePackage(SubscriptionPackage.TypePackage.RUNTIME)
                        .isActive(true)
                        .isDeleted(false)
                        .build();
                packagesToSave.add(runtimePackage);

                SubscriptionPackage devPackage = SubscriptionPackage.builder()
                        .name("Dev Package")
                        .description("hihi")
                        .price(getFixedPrice(cycle))
                        .discount(10f)
                        .billingCycle(cycle)
                        .typePackage(SubscriptionPackage.TypePackage.DEV)
                        .isActive(true)
                        .isDeleted(false)
                        .build();
                packagesToSave.add(devPackage);
            }

            subscriptionPackageRepository.saveAll(packagesToSave);

            for (SubscriptionPackage pkg : packagesToSave) {
                if (pkg.getTypePackage() == SubscriptionPackage.TypePackage.RUNTIME) {
                    pkg.setOptions(List.of(option1));
                } else {
                    pkg.setOptions(List.of(option2, option3));
                }
            }

            subscriptionPackageRepository.saveAll(packagesToSave);
        }

        if (paymentOrderRepository.count() == 0) {
            PaymentOrder order1 = PaymentOrder.builder()
                    .user(userRepository.findById(1L).orElse(null))
                    .subscriptionPackage(subscriptionPackageRepository.findById(2L).orElse(null))
                    .orderId(123456)
                    .price(4000f)
                    .paymentLink("https://payment.example.com/checkout/ORDER-123456")
                    .paymentStatus(PaymentOrder.PaymentStatus.SUCCESS)
                    .paymentMethod(PaymentOrder.PaymentMethod.BANK)
                    .licenseCreated(false)
                    .bin("970415")
                    .accountName("NGUYEN THI HUONG")
                    .accountNumber("0386331971")
                    .cancelReason("00020101021138540010A00000072701240006970415011003863319710208QRIBFTTA53037045802VN63046733")
                    .dateTransfer("2025-07-30 11:09:43")
                    .isDeleted(false)
                    .build();

            paymentOrderRepository.saveAll(List.of(order1));
        }

        if (licenseRepository.count() == 0) {
            PaymentOrder paymentOrder = paymentOrderRepository.findByOrderId(123456).orElse(null);

            License license = License.builder()
                    .user(userRepository.findById(1L).orElse(null))
                    .subscriptionPackage(subscriptionPackageRepository.findById(2L).orElse(null))
                    .licenseKey("LIC-ABCDEF-123456")
                    .duration(60)
                    .ip("203.113.78.9")
                    .hardwareId("203.113.78.9")
                    .canUsed(false)
                    .activatedAt(null)
                    .orderId(paymentOrder != null ? paymentOrder.getOrderId() : null)
                    .isDeleted(false)
                    .build();

            licenseRepository.save(license);
        }
        if (productRepository.count() == 0) {
            Product product1 = Product.builder()
                    .name("Product A")
                    .description("Sản phẩm A mô tả chi tiết")
                    .imageUrl("https://example.com/product-a.png")
                    .isActive(true)
                    .isDeleted(false)
                    .build();

            Product product2 = Product.builder()
                    .name("Product B")
                    .description("Sản phẩm B mô tả chi tiết")
                    .imageUrl("https://example.com/product-b.png")
                    .isActive(true)
                    .isDeleted(false)
                    .build();

            Product product3 = Product.builder()
                    .name("Product C")
                    .description("Sản phẩm C mô tả chi tiết")
                    .imageUrl("https://example.com/product-c.png")
                    .isActive(true)
                    .isDeleted(false)
                    .build();

            productRepository.saveAll(List.of(product1, product2, product3));
        }
  if (branchRepository.count() == 0) {
        Branch branch1 = Branch.builder()
                .name("Branch A")
                .description("Chi nhánh chính tại Hà Nội")
                .address("123 Phố Huế, Hà Nội")
                .phone("02412345678")
                .imageUrl("https://example.com/branch-a.png")
                .isActive(true)
                .isDeleted(false)
                .build();

        Branch branch2 = Branch.builder()
                .name("Branch B")
                .description("Chi nhánh phụ tại Hồ Chí Minh")
                .address("456 Lê Lợi, TP.HCM")
                .phone("02812345678")
                .imageUrl("https://example.com/branch-b.png")
                .isActive(true)
                .isDeleted(false)
                .build();

        branchRepository.saveAll(List.of(branch1, branch2));
    }
    // --- BranchProduct seeder ---
        if (branchProductRepository.count() == 0) {
        List<Branch> branches = branchRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (!branches.isEmpty() && !products.isEmpty()) {
            List<BranchProduct> branchProducts = new ArrayList<>();

            // tạo ví dụ mỗi branch có 2 product
            for (Branch branch : branches) {
                for (int i = 0; i < Math.min(2, products.size()); i++) {
                    BranchProduct bp = BranchProduct.builder()
                            .branch(branch)
                            .product(products.get(i))
                            .price(1000.0 + i * 500) // ví dụ giá
                            .isAvailable(true)
                            .isDeleted(false)
                            .build();
                    branchProducts.add(bp);
                }
            }

            branchProductRepository.saveAll(branchProducts);
        }
    }
    }


    private float getFixedPrice(SubscriptionPackage.BillingCycle cycle) {
        return switch (cycle) {
            case MONTHLY -> 2000f;
            case HALF_YEARLY -> 2500f;
            case YEARLY -> 3000f;
        };
    }
}