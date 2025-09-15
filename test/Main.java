package test;

import model.Car;
import repository.CarsRepository;
import repository.CarsRepositoryImpl;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Main {
    public static void main(String[] args) {
        CarsRepository repository = new CarsRepositoryImpl();

        List<Car> cars = repository.loadCarsFromFile("data/cars.txt");

        System.out.println("Автомобили в базе:");
        System.out.println("Number\tModel\tColor\tMileage\tCost");
        for (Car car : cars) {
            System.out.println(car);
        }

        String colorToFind = "Black";
        long mileageToFind = 0L;
        List<String> carNumbers = repository.findCarNumbersByColorOrMileage(cars, colorToFind, mileageToFind);
        System.out.println("Номера автомобилей по цвету или пробегу:");
        for (String number : carNumbers) {
            System.out.print(number + " ");
        }
        System.out.println();

        long minPrice = 700_000L;
        long maxPrice = 800_000L;
        long uniqueModels = repository.countUniqueModelsInPriceRange(cars, minPrice, maxPrice);
        System.out.println("Уникальные автомобили: " + uniqueModels + " шт.");

        String cheapestColor = repository.findColorOfCheapestCar(cars);
        System.out.println("Цвет автомобиля с минимальной стоимостью: " + cheapestColor);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        String modelToFind1 = "Toyota";
        double avgPrice1 = repository.calculateAveragePrice(cars, modelToFind1);
        System.out.println("Средняя стоимость модели " + modelToFind1 + ": " + df.format(avgPrice1));

        String modelToFind2 = "Volvo";
        double avgPrice2 = repository.calculateAveragePrice(cars, modelToFind2);
        System.out.println("Средняя стоимость модели " + modelToFind2 + ": " + df.format(avgPrice2));

        saveResultsToFile(cars, repository, colorToFind, mileageToFind, minPrice, maxPrice, "data/output.txt");
    }

    private static void saveResultsToFile(List<Car> cars, CarsRepository repository,
                                          String colorToFind, long mileageToFind,
                                          long minPrice, long maxPrice, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Автомобили в базе:");
            writer.println("Number\tModel\tColor\tMileage\tCost");
            for (Car car : cars) {
                writer.println(car);
            }
            writer.println();

            List<String> carNumbers = repository.findCarNumbersByColorOrMileage(cars, colorToFind, mileageToFind);
            writer.println("Номера автомобилей по цвету или пробегу:");
            for (String number : carNumbers) {
                writer.print(number + " ");
            }
            writer.println();
            writer.println();

            long uniqueModels = repository.countUniqueModelsInPriceRange(cars, minPrice, maxPrice);
            writer.println("Уникальные автомобили: " + uniqueModels + " шт.");
            writer.println();

            String cheapestColor = repository.findColorOfCheapestCar(cars);
            writer.println("Цвет автомобиля с минимальной стоимостью: " + cheapestColor);
            writer.println();

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("#0.00", symbols);

            String modelToFind1 = "Toyota";
            double avgPrice1 = repository.calculateAveragePrice(cars, modelToFind1);
            writer.println("Средняя стоимость модели " + modelToFind1 + ": " + df.format(avgPrice1));

            String modelToFind2 = "Volvo";
            double avgPrice2 = repository.calculateAveragePrice(cars, modelToFind2);
            writer.println("Средняя стоимость модели " + modelToFind2 + ": " + df.format(avgPrice2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}