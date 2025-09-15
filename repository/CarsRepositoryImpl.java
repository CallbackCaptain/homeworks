package repository;

import model.Car;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CarsRepositoryImpl implements CarsRepository {

    @Override
    public List<Car> loadCarsFromFile(String filename) {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    cars.add(new Car(parts[0], parts[1], parts[2],
                            Long.parseLong(parts[3]), Long.parseLong(parts[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public void saveCarsToFile(List<Car> cars, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Car car : cars) {
                writer.println(car.getNumber() + "|" + car.getModel() + "|" +
                        car.getColor() + "|" + car.getMileage() + "|" + car.getCost());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> findCarNumbersByColorOrMileage(List<Car> cars, String color, long mileage) {
        return cars.stream()
                .filter(car -> car.getColor().equals(color) || car.getMileage() == mileage)
                .map(Car::getNumber)
                .collect(Collectors.toList());
    }

    @Override
    public long countUniqueModelsInPriceRange(List<Car> cars, long minPrice, long maxPrice) {
        return cars.stream()
                .filter(car -> car.getCost() >= minPrice && car.getCost() <= maxPrice)
                .map(Car::getModel)
                .distinct()
                .count();
    }

    @Override
    public String findColorOfCheapestCar(List<Car> cars) {
        return cars.stream()
                .min(Comparator.comparingLong(Car::getCost))
                .map(Car::getColor)
                .orElse("");
    }

    @Override
    public double calculateAveragePrice(List<Car> cars, String model) {
        return cars.stream()
                .filter(car -> car.getModel().equals(model))
                .mapToLong(Car::getCost)
                .average()
                .orElse(0.0);
    }
}