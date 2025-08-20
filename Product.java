public class Product {
    private String name;
    private double cost;

    public Product(String name, double cost) {
        setName(name);
        setCost(cost);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Проверка на пустое имя
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }

        // Проверка на длину имени
        if (name.length() < 3) {
            throw new IllegalArgumentException("Название продукта не может быть короче 3 символов");
        }

        // Проверка что название не состоит только из цифр
        boolean onlyDigits = true;
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isDigit(name.charAt(i))) {
                onlyDigits = false;
                break;
            }
        }
        if (onlyDigits) {
            throw new IllegalArgumentException("Название продукта не должно содержать только цифры");
        }

        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        // Проверка что цена положительная
        if (cost <= 0) {
            throw new IllegalArgumentException("Стоимость продукта должна быть положительным числом");
        }
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name + " - " + getCost();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return Double.compare(product.cost, cost) == 0 &&
                name.equals(product.name);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        long costBits = Double.doubleToLongBits(cost);
        result = 31 * result + (int)(costBits ^ (costBits >>> 32));
        return result;
    }
}