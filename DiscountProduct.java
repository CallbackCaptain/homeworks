import java.time.LocalDate;

public class DiscountProduct extends Product {
    private double discount; // Скидка в процентах (0-100)
    private LocalDate expirationDate; // Дата окончания скидки

    public DiscountProduct(String name, double cost, double discount, LocalDate expirationDate) {
        super(name, cost);
        setDiscount(discount);
        setExpirationDate(expirationDate);
    }

    public double getDiscount() {
        // Проверяем, действует ли еще скидка
        if (isDiscountValid()) {
            return discount;
        } else {
            return 0; // Скидка истекла
        }
    }

    public void setDiscount(double discount) {
        // Проверка что скидка в правильном диапазоне
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до 100 процентов");
        }
        this.discount = discount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Дата окончания скидки не может быть пустой");
        }
        this.expirationDate = expirationDate;
    }

    // Проверка действительности скидки
    public boolean isDiscountValid() {
        LocalDate today = LocalDate.now();
        return !today.isAfter(expirationDate);
    }

    // Переопределяем метод получения цены с учетом скидки
    @Override
    public double getCost() {
        double originalCost = super.getCost();
        double currentDiscount = getDiscount();

        if (currentDiscount > 0) {
            double discountAmount = originalCost * (currentDiscount / 100);
            return originalCost - discountAmount;
        }

        return originalCost;
    }

    // Метод для получения оригинальной цены без скидки
    public double getOriginalCost() {
        return super.getCost();
    }

    @Override
    public String toString() {
        if (isDiscountValid()) {
            return getName() + " - " + getCost() +
                    " (скидка " + discount + "%, до " + expirationDate + ")";
        } else {
            return getName() + " - " + getCost() + " (скидка истекла)";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        DiscountProduct that = (DiscountProduct) obj;
        return Double.compare(that.discount, discount) == 0 &&
                expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long discountBits = Double.doubleToLongBits(discount);
        result = 31 * result + (int)(discountBits ^ (discountBits >>> 32));
        result = 31 * result + expirationDate.hashCode();
        return result;
    }
}