import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> persons = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Ввод покупателей
        System.out.println("Введите покупателей в формате: Имя=СуммаДенег");
        System.out.println("Разделяйте покупателей точкой с запятой (;)");
        System.out.println("Пример: Иван=100;Мария=50");

        String personsInput = scanner.nextLine();
        String[] personsData = personsInput.split(";");

        for (String personData : personsData) {
            try {
                String[] parts = personData.split("=");
                if (parts.length != 2) {
                    System.out.println("Неверный формат ввода для покупателя: " + personData);
                    continue;
                }
                String name = parts[0].trim();
                double money = Double.parseDouble(parts[1].trim());
                Person person = new Person(name, money);
                persons.add(person);
                System.out.println("Добавлен покупатель: " + name + " с " + money + " руб.");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                return;
            } catch (Exception e) {
                System.out.println("Ошибка при создании покупателя: " + personData);
                return;
            }
        }

        // Ввод продуктов
        System.out.println("\nТеперь введите продукты.");
        System.out.println("Обычный продукт: Название=Цена");
        System.out.println("Продукт со скидкой: Название=Цена=Скидка=ДатаОкончания");
        System.out.println("Разделяйте продукты точкой с запятой (;)");
        System.out.println("Пример: Хлеб=30;Молоко=50=20=31.12.2024");

        String productsInput = scanner.nextLine();
        String[] productsData = productsInput.split(";");

        for (String productData : productsData) {
            try {
                String[] parts = productData.split("=");

                if (parts.length == 2) {
                    // Обычный продукт
                    String name = parts[0].trim();
                    double cost = Double.parseDouble(parts[1].trim());
                    Product product = new Product(name, cost);
                    products.add(product);
                    System.out.println("Добавлен продукт: " + product);

                } else if (parts.length == 4) {
                    // Продукт со скидкой
                    String name = parts[0].trim();
                    double cost = Double.parseDouble(parts[1].trim());
                    double discount = Double.parseDouble(parts[2].trim());
                    LocalDate expirationDate = LocalDate.parse(parts[3].trim(), dateFormat);

                    DiscountProduct discountProduct = new DiscountProduct(name, cost, discount, expirationDate);
                    products.add(discountProduct);
                    System.out.println("Добавлен продукт со скидкой: " + discountProduct);

                } else {
                    System.out.println("Неверный формат ввода для продукта: " + productData);
                    continue;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка валидации: " + e.getMessage());
                return;
            } catch (Exception e) {
                System.out.println("Ошибка при создании продукта: " + productData);
                System.out.println("Проверьте формат даты (дд.мм.гггг)");
                return;
            }
        }

        // Процесс покупок
        System.out.println("\n=== ПРОЦЕСС ПОКУПОК ===");
        System.out.println("Вводите: ИмяПокупателя-НазваниеПродукта");
        System.out.println("Для завершения введите: END");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("END")) {
                break;
            }

            String[] parts = input.split("-");
            if (parts.length != 2) {
                System.out.println("Неверный формат. Используйте: ИмяПокупателя-НазваниеПродукта");
                continue;
            }

            String personName = parts[0].trim();
            String productName = parts[1].trim();

            // Поиск покупателя
            Person buyer = null;
            for (Person person : persons) {
                if (person.getName().equals(personName)) {
                    buyer = person;
                    break;
                }
            }

            if (buyer == null) {
                System.out.println("Покупатель " + personName + " не найден");
                continue;
            }

            // Поиск продукта
            Product productToBuy = null;
            for (Product product : products) {
                if (product.getName().equals(productName)) {
                    productToBuy = product;
                    break;
                }
            }

            if (productToBuy == null) {
                System.out.println("Продукт " + productName + " не найден");
                continue;
            }

            // Попытка покупки
            double price = productToBuy.getCost();
            if (buyer.buyProduct(productToBuy)) {
                System.out.print(buyer.getName() + " купил " + productToBuy.getName());

                // Если это продукт со скидкой, показываем информацию о скидке
                if (productToBuy instanceof DiscountProduct) {
                    DiscountProduct dp = (DiscountProduct) productToBuy;
                    if (dp.isDiscountValid()) {
                        System.out.print(" со скидкой " + dp.getDiscount() + "%");
                        System.out.print(" (цена: " + price + " вместо " + dp.getOriginalCost() + ")");
                    }
                } else {
                    System.out.print(" за " + price);
                }
                System.out.println();
            } else {
                System.out.println(buyer.getName() + " не может позволить себе " +
                        productToBuy.getName() + " (цена: " + price + ", денег: " +
                        buyer.getMoney() + ")");
            }
        }

        // Вывод результатов
        System.out.println("\n=== РЕЗУЛЬТАТЫ ПОКУПОК ===");
        for (Person person : persons) {
            System.out.println(person.toString());
        }

        // Показываем статистику
        System.out.println("\n=== СТАТИСТИКА ===");
        for (Person person : persons) {
            if (!person.getBag().isEmpty()) {
                System.out.println(person.getName() + " потратил: " + person.getTotalSpent() + " руб.");
            }
        }

        scanner.close();
    }
}