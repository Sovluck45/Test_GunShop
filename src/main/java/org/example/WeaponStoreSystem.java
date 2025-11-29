import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Weapon {
    private String id;
    private String name;
    private String type;
    private String caliber;
    private double price;
    private int quantity;
    private boolean inStock;

    public Weapon(String id, String name, String type, String caliber, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.caliber = caliber;
        this.price = price;
        this.quantity = quantity;
        this.inStock = quantity > 0;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getCaliber() { return caliber; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public boolean isInStock() { return inStock; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.inStock = quantity > 0;
    }

    public void decreaseStock(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
            inStock = quantity > 0;
        }
    }

    public void increaseStock(int amount) {
        quantity += amount;
        inStock = true;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | %s | Тип: %s | Калибр: %s | Цена: %.2f руб. | В наличии: %d шт.",
                id, name, type, caliber, price, quantity);
    }
}

class Order {
    private String orderId;
    private List<OrderItem> items;
    private double totalAmount;
    private String customerName;
    private String date;

    public Order(String orderId, String customerName, String date) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.customerName = customerName;
        this.date = date;
        this.totalAmount = 0.0;
    }

    public void addItem(Weapon weapon, int quantity) {
        items.add(new OrderItem(weapon, quantity));
        totalAmount += weapon.getPrice() * quantity;
    }

    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getCustomerName() { return customerName; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Заказ #%s для %s от %s\n", orderId, customerName, date));
        sb.append("Товары:\n");
        for (OrderItem item : items) {
            sb.append(String.format("  - %s x%d = %.2f руб.\n",
                    item.getWeapon().getName(),
                    item.getQuantity(),
                    item.getWeapon().getPrice() * item.getQuantity()));
        }
        sb.append(String.format("Итого: %.2f руб.", totalAmount));
        return sb.toString();
    }
}

class OrderItem {
    private Weapon weapon;
    private int quantity;

    public OrderItem(Weapon weapon, int quantity) {
        this.weapon = weapon;
        this.quantity = quantity;
    }

    public Weapon getWeapon() { return weapon; }
    public int getQuantity() { return quantity; }
}

class WeaponStore {
    private List<Weapon> inventory;
    private List<Order> orders;
    private int orderCounter;

    public WeaponStore() {
        inventory = new ArrayList<>();
        orders = new ArrayList<>();
        orderCounter = 1;
        initializeInventory();
    }

    private void initializeInventory() {
        inventory.add(new Weapon("W001", "Пистолет Макарова", "Пистолет", "9x18 мм", 25000.0, 5));
        inventory.add(new Weapon("W002", "Винтовка Мосина", "Винтовка", "7.62x54 мм", 45000.0, 3));
        inventory.add(new Weapon("W003", "МР-153", "Дробовик", "12x70", 35000.0, 4));
        inventory.add(new Weapon("W004", "Glock 17", "Пистолет", "9x19 мм", 32000.0, 2));
        inventory.add(new Weapon("W005", "СВД", "Снайперская винтовка", "7.62x54 мм", 85000.0, 1));
        inventory.add(new Weapon("W006", "ПП-19", "Пистолет-пулемет", "9x19 мм", 55000.0, 2));
    }

    public void displayInventory() {
        System.out.println("\n===== КАТАЛОГ ТОВАРОВ =====");
        if (inventory.isEmpty()) {
            System.out.println("Каталог пуст");
            return;
        }

        for (Weapon weapon : inventory) {
            System.out.println(weapon);
        }
        System.out.println("===========================\n");
    }

    public void displayAvailableWeapons() {
        System.out.println("\n===== ТОВАРЫ В НАЛИЧИИ =====");
        List<Weapon> available = inventory.stream()
                .filter(Weapon::isInStock)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.println("Нет товаров в наличии");
            return;
        }

        for (Weapon weapon : available) {
            System.out.println(weapon);
        }
        System.out.println("===========================\n");
    }

    public void filterByType(String type) {
        System.out.println("\n===== ФИЛЬТР ПО ТИПУ: " + type.toUpperCase() + " =====");
        List<Weapon> filtered = inventory.stream()
                .filter(w -> w.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("Товаров данного типа не найдено");
            return;
        }

        for (Weapon weapon : filtered) {
            System.out.println(weapon);
        }
        System.out.println("===========================\n");
    }

    public void filterByCaliber(String caliber) {
        System.out.println("\n===== ФИЛЬТР ПО КАЛИБРУ: " + caliber.toUpperCase() + " =====");
        List<Weapon> filtered = inventory.stream()
                .filter(w -> w.getCaliber().equalsIgnoreCase(caliber))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("Товаров данного калибра не найдено");
            return;
        }

        for (Weapon weapon : filtered) {
            System.out.println(weapon);
        }
        System.out.println("===========================\n");
    }

    public void filterByPriceRange(double minPrice, double maxPrice) {
        System.out.println("\n===== ФИЛЬТР ПО ЦЕНЕ: ОТ " + minPrice + " ДО " + maxPrice + " РУБ. =====");
        List<Weapon> filtered = inventory.stream()
                .filter(w -> w.getPrice() >= minPrice && w.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("Товаров в указанном ценовом диапазоне не найдено");
            return;
        }

        for (Weapon weapon : filtered) {
            System.out.println(weapon);
        }
        System.out.println("===========================\n");
    }

    public Weapon findWeaponById(String id) {
        for (Weapon weapon : inventory) {
            if (weapon.getId().equals(id)) {
                return weapon;
            }
        }
        return null;
    }

    public void placeOrder(String customerName) {
        Scanner scanner = new Scanner(System.in);
        List<Weapon> cart = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        double total = 0.0;

        System.out.println("\n===== СОЗДАНИЕ ЗАКАЗА ДЛЯ: " + customerName + " =====");
        displayAvailableWeapons();

        while (true) {
            System.out.print("Введите ID товара для добавления в корзину (или 'done' для завершения): ");
            String itemId = scanner.nextLine().trim();

            if (itemId.equalsIgnoreCase("done")) {
                break;
            }

            Weapon weapon = findWeaponById(itemId);
            if (weapon == null) {
                System.out.println("Товар с таким ID не найден");
                continue;
            }

            if (!weapon.isInStock()) {
                System.out.println("Товар отсутствует на складе");
                continue;
            }

            System.out.print("Введите количество (доступно: " + weapon.getQuantity() + "): ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Количество должно быть больше нуля");
                    continue;
                }
                if (quantity > weapon.getQuantity()) {
                    System.out.println("Недостаточно товара на складе");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректное количество");
                continue;
            }

            cart.add(weapon);
            quantities.add(quantity);
            total += weapon.getPrice() * quantity;

            System.out.println(String.format("Добавлено: %s x%d = %.2f руб.",
                    weapon.getName(), quantity, weapon.getPrice() * quantity));
            System.out.println(String.format("Текущая сумма заказа: %.2f руб.\n", total));
        }

        if (cart.isEmpty()) {
            System.out.println("Корзина пуста. Заказ не создан");
            return;
        }

        // Создаем заказ
        String orderId = "ORD" + String.format("%04d", orderCounter++);
        String date = java.time.LocalDate.now().toString();
        Order order = new Order(orderId, customerName, date);

        // Добавляем товары в заказ и уменьшаем количество на складе
        for (int i = 0; i < cart.size(); i++) {
            Weapon weapon = cart.get(i);
            int quantity = quantities.get(i);
            order.addItem(weapon, quantity);
            weapon.decreaseStock(quantity);
        }

        orders.add(order);
        System.out.println("\n===== ЗАКАЗ УСПЕШНО СОЗДАН =====");
        System.out.println(order);
        System.out.println("===========================\n");
    }

    public void displayOrders() {
        System.out.println("\n===== ИСТОРИЯ ЗАКАЗОВ =====");
        if (orders.isEmpty()) {
            System.out.println("Заказов нет");
        } else {
            for (Order order : orders) {
                System.out.println(order);
                System.out.println("---------------------------");
            }
        }
        System.out.println("===========================\n");
    }

    public void generateSalesReport() {
        System.out.println("\n===== ОТЧЕТ ПО ПРОДАЖАМ =====");

        if (orders.isEmpty()) {
            System.out.println("Нет данных для отчета");
            return;
        }

        double totalRevenue = 0.0;
        int totalItemsSold = 0;
        java.util.Map<String, Integer> salesByType = new java.util.HashMap<>();

        for (Order order : orders) {
            totalRevenue += order.getTotalAmount();

            for (OrderItem item : order.getItems()) {
                totalItemsSold += item.getQuantity();
                String type = item.getWeapon().getType();
                salesByType.put(type, salesByType.getOrDefault(type, 0) + item.getQuantity());
            }
        }

        System.out.println("Общая выручка: " + String.format("%.2f", totalRevenue) + " руб.");
        System.out.println("Всего продано товаров: " + totalItemsSold + " шт.");
        System.out.println("\nПродажи по типам оружия:");

        salesByType.entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " шт."));

        System.out.println("===========================\n");
    }

    public void addNewWeapon() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Название оружия: ");
        String name = scanner.nextLine().trim();

        System.out.print("Тип оружия: ");
        String type = scanner.nextLine().trim();

        System.out.print("Калибр: ");
        String caliber = scanner.nextLine().trim();

        double price = 0.0;
        while (true) {
            try {
                System.out.print("Цена (руб.): ");
                price = Double.parseDouble(scanner.nextLine().trim());
                if (price <= 0) {
                    System.out.println("Цена должна быть больше нуля");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректная цена");
            }
        }

        int quantity = 0;
        while (true) {
            try {
                System.out.print("Количество на складе: ");
                quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity < 0) {
                    System.out.println("Количество не может быть отрицательным");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Некорректное количество");
            }
        }

        // Генерируем новый ID
        String id = "W" + String.format("%03d", inventory.size() + 1);

        Weapon newWeapon = new Weapon(id, name, type, caliber, price, quantity);
        inventory.add(newWeapon);

        System.out.println("Товар успешно добавлен в каталог!");
        System.out.println(newWeapon);
    }
}

public class WeaponStoreSystem {
    public static void main(String[] args) {
        WeaponStore store = new WeaponStore();
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== СИСТЕМА УПРАВЛЕНИЯ ОРУЖЕЙНЫМ МАГАЗИНОМ =====");

        while (true) {
            System.out.println("\n===== ГЛАВНОЕ МЕНЮ =====");
            System.out.println("1. Просмотреть весь каталог");
            System.out.println("2. Просмотреть товары в наличии");
            System.out.println("3. Фильтр по типу оружия");
            System.out.println("4. Фильтр по калибру");
            System.out.println("5. Фильтр по цене");
            System.out.println("6. Добавить новый товар");
            System.out.println("7. Оформить заказ");
            System.out.println("8. Просмотреть историю заказов");
            System.out.println("9. Сгенерировать отчет по продажам");
            System.out.println("10. Выйти");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    store.displayInventory();
                    break;
                case "2":
                    store.displayAvailableWeapons();
                    break;
                case "3":
                    System.out.print("Введите тип оружия: ");
                    String type = scanner.nextLine().trim();
                    store.filterByType(type);
                    break;
                case "4":
                    System.out.print("Введите калибр: ");
                    String caliber = scanner.nextLine().trim();
                    store.filterByCaliber(caliber);
                    break;
                case "5":
                    try {
                        System.out.print("Минимальная цена (руб.): ");
                        double minPrice = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Максимальная цена (руб.): ");
                        double maxPrice = Double.parseDouble(scanner.nextLine().trim());
                        store.filterByPriceRange(minPrice, maxPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректные значения цены");
                    }
                    break;
                case "6":
                    store.addNewWeapon();
                    break;
                case "7":
                    System.out.print("Введите имя клиента: ");
                    String customerName = scanner.nextLine().trim();
                    store.placeOrder(customerName);
                    break;
                case "8":
                    store.displayOrders();
                    break;
                case "9":
                    store.generateSalesReport();
                    break;
                case "10":
                    System.out.println("Спасибо за использование системы!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Некорректный выбор. Попробуйте еще раз.");
            }
        }
    }
}