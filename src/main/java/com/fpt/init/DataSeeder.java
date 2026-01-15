package com.fpt.init;

import com.fpt.entity.*;
import com.fpt.enums.OptionSelectType;
import com.fpt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final BranchProductRepository branchProductRepository;

    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final ProductOptionGroupRepository productOptionGroupRepository;

    @Transactional
    @Override
    public void run(String... args) {
        seedUsers();
        seedSubscriptionPackages();
        seedPaymentOrders();
        seedLicenses();

        seedCategories();
        seedProducts();

        seedOptionGroupsAndOptions();
        seedProductOptionGroups();

        seedBranches();
        seedBranchProducts();
    }

    // ================= USERS =================
    private void seedUsers() {
        if (userRepository.count() > 0) return;

        userRepository.saveAll(List.of(
                User.builder()
                        .userName("admin")
                        .email("admin@gmail.com")
                        .password("$2a$10$RAU5Vl1A6Iheyeg2MSBlVeLRLpH2kRSpredJkzJIm72ZscI6pg/62")
                        .firstName("Nguyen Van")
                        .lastName("A")
                        .phoneNumber("0987654321")
                        .role(Role.ADMIN)
                        .status(UserStatus.ACTIVE)
                        .isActive(true)
                        .isDeleted(false)
                        .build(),

                User.builder()
                        .userName("CaoVanBay")
                        .email("Bay@gmail.com")
                        .password("$2a$10$RAU5Vl1A6Iheyeg2MSBlVeLRLpH2kRSpredJkzJIm72ZscI6pg/62")
                        .firstName("Cao Van")
                        .lastName("Bay")
                        .phoneNumber("0999999999")
                        .role(Role.CUSTOMER)
                        .status(UserStatus.ACTIVE)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        ));
    }

    // ================= CATEGORY =================
    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.saveAll(List.of(
                Category.builder()
                        .name("Trà")
                        .description("Các loại trà")
                        .isActive(true)
                        .isDeleted(false)
                        .build(),
                Category.builder()
                        .name("Nước khác")
                        .description("Nước uống khác")
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        ));
    }

    // ================= PRODUCT =================
    private void seedProducts() {
        if (productRepository.count() > 0) return;

        Category tea = categoryRepository.findAll().get(0);
        Category other = categoryRepository.findAll().get(1);

        productRepository.saveAll(List.of(
                Product.builder()
                        .name("Trà đào")
                        .price(40000d)
                        .category(tea)
                        .isActive(true)
                        .isDeleted(false)
                        .build(),

                Product.builder()
                        .name("Trà sữa olong")
                        .price(30000d)
                        .category(tea)
                        .isActive(true)
                        .isDeleted(false)
                        .build(),

                Product.builder()
                        .name("Nước lọc")
                        .price(5000d)
                        .category(other)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        ));
    }

    // ================= OPTION GROUP & OPTION =================
    private void seedOptionGroupsAndOptions() {
        if (optionGroupRepository.count() > 0) return;

        // ICE
        OptionGroup ice = optionGroupRepository.save(
                OptionGroup.builder()
                        .name("Ice")
                        .selectType(OptionSelectType.SINGLE)
                        .required(true)
                        .minSelect(1)
                        .maxSelect(1)
                        .displayOrder(1)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        optionRepository.saveAll(List.of(
                Option.builder().optionGroup(ice).name("0%").price(0).displayOrder(1).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(ice).name("50%").price(0).displayOrder(1).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(ice).name("100%").price(0).displayOrder(1).isActive(true).isDeleted(false).build()
        ));

        // SUGAR
        OptionGroup sugar = optionGroupRepository.save(
                OptionGroup.builder()
                        .name("Sugar")
                        .selectType(OptionSelectType.SINGLE)
                        .required(true)
                        .minSelect(1)
                        .maxSelect(1)
                        .displayOrder(2)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        optionRepository.saveAll(List.of(
                Option.builder().optionGroup(sugar).name("0%").price(0).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(sugar).name("50%").price(0).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(sugar).name("100%").price(0).isActive(true).isDeleted(false).build()
        ));

        // TOPPING
        OptionGroup topping = optionGroupRepository.save(
                OptionGroup.builder()
                        .name("Topping")
                        .selectType(OptionSelectType.MULTIPLE)
                        .required(false)
                        .minSelect(0)
                        .maxSelect(3)
                        .displayOrder(3)
                        .isActive(true)
                        .isDeleted(false)
                        .build()
        );

        optionRepository.saveAll(List.of(
                Option.builder().optionGroup(topping).name("Trân châu").price(5000).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(topping).name("Pudding").price(7000).isActive(true).isDeleted(false).build(),
                Option.builder().optionGroup(topping).name("Thạch").price(3000).isActive(true).isDeleted(false).build()
        ));
    }

    // ================= PRODUCT OPTION GROUP =================
    private void seedProductOptionGroups() {
        if (productOptionGroupRepository.count() > 0) return;

        Map<String, OptionGroup> groupMap = new HashMap<>();
        optionGroupRepository.findAll()
                .forEach(g -> groupMap.put(g.getName(), g));

        for (Product product : productRepository.findAll()) {
            if (product.getName().equalsIgnoreCase("Nước lọc")) continue;

            productOptionGroupRepository.saveAll(List.of(
                    ProductOptionGroup.builder().product(product).optionGroup(groupMap.get("Ice")).build(),
                    ProductOptionGroup.builder().product(product).optionGroup(groupMap.get("Sugar")).build(),
                    ProductOptionGroup.builder().product(product).optionGroup(groupMap.get("Topping")).build()
            ));
        }
    }

    // ================= BRANCH =================
    private void seedBranches() {
        if (branchRepository.count() > 0) return;

        branchRepository.saveAll(List.of(
                Branch.builder().name("Branch A").isActive(true).isDeleted(false).build(),
                Branch.builder().name("Branch B").isActive(true).isDeleted(false).build()
        ));
    }

    private void seedBranchProducts() {
        if (branchProductRepository.count() > 0) return;

        for (Branch branch : branchRepository.findAll()) {
            for (Product product : productRepository.findAll()) {
                branchProductRepository.save(
                        BranchProduct.builder()
                                .branch(branch)
                                .product(product)
                                .isAvailable(true)
                                .isDeleted(false)
                                .build()
                );
            }
        }
    }

    // ================= SUBSCRIPTION / PAYMENT (GIỮ NGUYÊN LOGIC CŨ) =================
    private void seedSubscriptionPackages() {
        if (subscriptionPackageRepository.count() > 0) return;
        for (SubscriptionPackage.BillingCycle cycle : SubscriptionPackage.BillingCycle.values()) {
            subscriptionPackageRepository.save(
                    SubscriptionPackage.builder()
                            .name("Runtime Package")
                            .billingCycle(cycle)
                            .price(2000f)
                            .isActive(true)
                            .isDeleted(false)
                            .build()
            );
        }
    }

    private void seedPaymentOrders() {}
    private void seedLicenses() {}
}
