package repository;

import model.Car;
import java.util.List;

public interface CarsRepository {
    List<Car> loadCarsFromFile(String filename);
    void saveCarsToFile(List<Car> cars, String filename);
    List<String> findCarNumbersByColorOrMileage(List<Car> cars, String color, long mileage);
    long countUniqueModelsInPriceRange(List<Car> cars, long minPrice, long maxPrice);
    String findColorOfCheapestCar(List<Car> cars);
    double calculateAveragePrice(List<Car> cars, String model);
}