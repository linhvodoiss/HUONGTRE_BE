package com.fpt.init;

import com.fpt.entity.*;
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
    private final PaymentOrderRepository paymentOrderRepository;
    private final LicenseRepository licenseRepository;
    private final OptionRepository optionRepository;
    private final BranchRepository branchRepository;
    private final BranchProductRepository branchProductRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ToppingRepository toppingRepository;
    private final SugarRepository sugarRepository;
    private final IceRepository iceRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        seedOptions();
        seedUsers();
        seedSubscriptionPackages();
        seedPaymentOrders();
        seedLicenses();
        seedSizes();
        seedToppings();
        seedIces();
        seedSugars();
        seedCategories();
        seedProducts();
        seedBranches();
        seedBranchProducts();
    }

    private void seedOptions() {
        if (optionRepository.count() == 0) {
            List<Option> options = Arrays.asList(
                    Option.builder().name("5 documents per month").isActive(true).isDeleted(false).build(),
                    Option.builder().name("Unlimited access").isActive(true).isDeleted(false).build(),
                    Option.builder().name("Priority support").isActive(true).isDeleted(false).build()
            );
            optionRepository.saveAll(options);
        }
    }

    private void seedUsers() {
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
    }

    private void seedSubscriptionPackages() {
        if (subscriptionPackageRepository.count() == 0) {
            Option option1 = optionRepository.findById(1L).orElseThrow();
            Option option2 = optionRepository.findById(2L).orElseThrow();
            Option option3 = optionRepository.findById(3L).orElseThrow();

            List<SubscriptionPackage> packagesToSave = new ArrayList<>();
            for (SubscriptionPackage.BillingCycle cycle : SubscriptionPackage.BillingCycle.values()) {
                SubscriptionPackage runtimePackage = SubscriptionPackage.builder()
                        .name("Runtime Package")
                        .description("Runtime package description")
                        .price(getFixedPrice(cycle))
                        .discount(0f)
                        .billingCycle(cycle)
                        .typePackage(SubscriptionPackage.TypePackage.RUNTIME)
                        .isActive(true)
                        .isDeleted(false)
                        .options(new ArrayList<>()) // mutable list
                        .build();
                packagesToSave.add(runtimePackage);

                SubscriptionPackage devPackage = SubscriptionPackage.builder()
                        .name("Dev Package")
                        .description("Dev package description")
                        .price(getFixedPrice(cycle))
                        .discount(10f)
                        .billingCycle(cycle)
                        .typePackage(SubscriptionPackage.TypePackage.DEV)
                        .isActive(true)
                        .isDeleted(false)
                        .options(new ArrayList<>())
                        .build();
                packagesToSave.add(devPackage);
            }

            subscriptionPackageRepository.saveAll(packagesToSave);

            for (SubscriptionPackage pkg : packagesToSave) {
                if (pkg.getTypePackage() == SubscriptionPackage.TypePackage.RUNTIME) {
                    pkg.getOptions().add(option1);
                } else {
                    pkg.getOptions().addAll(Arrays.asList(option2, option3));
                }
            }
            subscriptionPackageRepository.saveAll(packagesToSave);
        }
    }

    private void seedPaymentOrders() {
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
                    .cancelReason("000201......733")
                    .dateTransfer("2025-07-30 11:09:43")
                    .isDeleted(false)
                    .build();

            paymentOrderRepository.save(order1);
        }
    }

    private void seedLicenses() {
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
    }

    private void seedSizes() {
        if (sizeRepository.count() == 0) {
            List<Size> sizes = Arrays.asList(
                    Size.builder().name("S").description("Small").isActive(true).isDeleted(false).build(),
                    Size.builder().name("M").description("Medium").isActive(true).isDeleted(false).build(),
                    Size.builder().name("L").description("Large").isActive(true).isDeleted(false).build()
            );
            sizeRepository.saveAll(sizes);
        }
    }

    private void seedToppings() {
        if (toppingRepository.count() == 0) {
            List<Topping> toppings = Arrays.asList(
                    Topping.builder().name("Cheese").description("Extra cheese").price(500.0).isActive(true).isDeleted(false).isAvailable(true).build(),
                    Topping.builder().name("Bacon").description("Crispy bacon").price(700.0).isActive(true).isDeleted(false).isAvailable(true).build(),
                    Topping.builder().name("Mushroom").description("Fresh mushroom").price(400.0).isActive(true).isDeleted(false).isAvailable(true).build()
            );
            toppingRepository.saveAll(toppings);
        }
    }

    private void seedIces() {
        if (iceRepository.count() == 0) {
            List<Ice> ices = Arrays.asList(
                    Ice.builder().name("0% Đá").description("0% Đá").isActive(true).isDeleted(false).isAvailable(true).build(),
                    Ice.builder().name("50% Đá").description("50% Đá").isActive(true).isDeleted(false).isAvailable(true).build(),
                    Ice.builder().name("100% Đá").description("100% Đá").isActive(true).isDeleted(false).isAvailable(true).build()
            );
            iceRepository.saveAll(ices);
        }
    }

    private void seedSugars() {
        if (sugarRepository.count() == 0) {
            List<Sugar> sugars = Arrays.asList(
                    Sugar.builder().name("Không Đường").description("Không Đường").isActive(true).isDeleted(false).isAvailable(true).build(),
                    Sugar.builder().name("50% Đường").description("50% Đường").isActive(true).isDeleted(false).isAvailable(true).build(),
                    Sugar.builder().name("100% Đường").description("100% Đường").isActive(true).isDeleted(false).isAvailable(true).build()
            );
            sugarRepository.saveAll(sugars);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.builder().name("Trà").description("Các loại trà thơm ngon").imageUrl("https://example.com/category-tea.png").isActive(true).isDeleted(false).build(),
                    Category.builder().name("Nước uống khác").description("Các loại nước uống khác").imageUrl("https://example.com/category-other.png").isActive(true).isDeleted(false).build()
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category teaCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Trà")).findFirst().orElse(null);
            Category otherCategory = categoryRepository.findAll().stream().filter(c -> c.getName().equals("Nước uống khác")).findFirst().orElse(null);

            List<Product> products = new ArrayList<>();

            Product product1 = Product.builder()
                    .name("Trà đào")
                    .description("Trà đào mô tả chi tiết")
                    .price(40000d)
                    .imageUrl("https://example.com/product-a.png")
                    .category(teaCategory)
                    .productSizes(new ArrayList<>())
                    .toppings(new ArrayList<>())
                    .ices(new ArrayList<>())
                    .sugars(new ArrayList<>())
                    .branchProducts(new ArrayList<>())
                    .isActive(true).isDeleted(false)
                    .build();

            Product product2 = Product.builder()
                    .name("Trà sữa olong")
                    .description("Trà sữa olong mô tả chi tiết")
                    .price(30000d)
                    .imageUrl("https://example.com/product-b.png")
                    .category(teaCategory)
                    .productSizes(new ArrayList<>())
                    .toppings(new ArrayList<>())
                    .ices(new ArrayList<>())
                    .sugars(new ArrayList<>())
                    .branchProducts(new ArrayList<>())
                    .isActive(true).isDeleted(false)
                    .build();

            Product product3 = Product.builder()
                    .name("Nước lọc")
                    .description("Nước lọc mô tả chi tiết")
                    .price(5000d)
                    .imageUrl("https://example.com/product-c.png")
                    .category(otherCategory)
                    .productSizes(new ArrayList<>())
                    .toppings(new ArrayList<>())
                    .ices(new ArrayList<>())
                    .sugars(new ArrayList<>())
                    .branchProducts(new ArrayList<>())
                    .isActive(true).isDeleted(false)
                    .build();

            products.addAll(Arrays.asList(product1, product2, product3));
            productRepository.saveAll(products);

            // --- gán toppings ---
            List<Topping> toppings = toppingRepository.findAll();
            if (!toppings.isEmpty()) {
                product1.setToppings(new ArrayList<>(toppings.subList(0,2))); // Cheese + Bacon
                product2.setToppings(new ArrayList<>(toppings.subList(1,3))); // Bacon + Mushroom
                product3.setToppings(new ArrayList<>(Arrays.asList(toppings.get(0), toppings.get(2)))); // Cheese + Mushroom
            }
            productRepository.saveAll(products);


            // --- gán ices ---
            List<Ice> ices = iceRepository.findAll();
            if (!ices.isEmpty()) {
                product1.setIces(new ArrayList<>(ices.subList(0,2)));
                product2.setIces(new ArrayList<>(ices.subList(1,3)));
                product3.setIces(new ArrayList<>(Arrays.asList(ices.get(0), ices.get(2))));
            }
            productRepository.saveAll(products);

            // --- gán sugars ---
            List<Sugar> sugars = sugarRepository.findAll();
            if (!toppings.isEmpty()) {
                product1.setSugars(new ArrayList<>(sugars.subList(0,2)));
                product2.setSugars(new ArrayList<>(sugars.subList(1,3)));
                product3.setSugars(new ArrayList<>(Arrays.asList(sugars.get(0), sugars.get(2))));
            }
            productRepository.saveAll(products);

            // --- tạo ProductSize ---
            List<Size> sizes = sizeRepository.findAll();
            for (Product product : products) {
                createProductSize(product, sizes);
            }
        }
    }

    private void seedBranches() {
        if (branchRepository.count() == 0) {
            List<Branch> branches = Arrays.asList(
                    Branch.builder().name("Branch A").description("Chi nhánh chính tại Hà Nội").address("123 Phố Huế, Hà Nội").phone("02412345678").imageUrl("https://example.com/branch-a.png").isActive(true).isDeleted(false).branchProducts(new ArrayList<>()).build(),
                    Branch.builder().name("Branch B").description("Chi nhánh phụ tại Hồ Chí Minh").address("456 Lê Lợi, TP.HCM").phone("02812345678").imageUrl("https://example.com/branch-b.png").isActive(true).isDeleted(false).branchProducts(new ArrayList<>()).build()
            );
            branchRepository.saveAll(branches);
        }
    }

    private void seedBranchProducts() {
        if (branchProductRepository.count() == 0) {
            List<Branch> branches = branchRepository.findAll();
            List<Product> products = productRepository.findAll();

            for (Branch branch : branches) {
                for (int i = 0; i < Math.min(2, products.size()); i++) {
                    Product product = products.get(i);
                    BranchProduct bp = BranchProduct.builder()
                            .branch(branch)
                            .product(product)
                            .isAvailable(true)
                            .isDeleted(false)
                            .build();

                    branch.getBranchProducts().add(bp);
                    product.getBranchProducts().add(bp);
                }
            }
            productRepository.saveAll(products);
            branchRepository.saveAll(branches);
        }
    }

    private void createProductSize(Product product, List<Size> sizes) {
        for (Size size : sizes) {

            double extra = switch (size.getName()) {
                case "M" -> 5000;
                case "L" -> 10000;
                default -> 0;
            };
            ProductSize ps = ProductSize.builder()
                    .product(product)
                    .size(size)
                    .price(extra)
                    .isDeleted(false)
                    .build();
            product.getProductSizes().add(ps);
        }
        productRepository.save(product);
    }

    private float getFixedPrice(SubscriptionPackage.BillingCycle cycle) {
        return switch (cycle) {
            case MONTHLY -> 2000f;
            case HALF_YEARLY -> 2500f;
            case YEARLY -> 3000f;
        };
    }
}
