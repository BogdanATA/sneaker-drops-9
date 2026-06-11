package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.SneakerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final SneakerService sneakerService;

    public StartupRunner(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @Override
    public void run(String... args) {
        sneakerService.seedData();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Sneaker Drops Menu ===");
            System.out.println("1) List all sneakers");
            System.out.println("2) Search by model");
            System.out.println("3) Filter by max price");
            System.out.println("4) Filter by release year");
            System.out.println("5) Search by max price + min year");
            System.out.println("6) View sneaker by id");
            System.out.println("7) Add a sneaker");
            System.out.println("8) Update sneaker price");
            System.out.println("9) Delete sneaker");
            System.out.println("10) Search by brand");
            System.out.println("0) Quit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> listSneakers();
                case 2 -> searchByModel(scanner);
                case 3 -> filterByPrice(scanner);
                case 4 -> filterByYear(scanner);
                case 5 -> searchByPriceAndYear(scanner);
                case 6 -> viewById(scanner);
                case 7 -> addSneaker(scanner);
                case 8 -> updatePrice(scanner);
                case 9 -> deleteSneaker(scanner);
                case 10 -> searchByBrand(scanner);
                case 0 -> running = false;
                default -> System.out.println("Invalid option, try again.");
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }

    private void listSneakers() {
        System.out.println("\nTotal sneakers: " + sneakerService.count());
        for (Sneaker s : sneakerService.listAll()) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear() + " | " + s.getBrand().getName());
        }
    }

    private void searchByModel(Scanner scanner) {
        System.out.print("Enter model text to search: ");
        String text = scanner.next();
        for (Sneaker s : sneakerService.searchByModel(text)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void filterByPrice(Scanner scanner) {
        System.out.print("Enter max price: ");
        double price = scanner.nextDouble();
        for (Sneaker s : sneakerService.filterByPrice(price)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void filterByYear(Scanner scanner) {
        System.out.print("Enter release year: ");
        int year = scanner.nextInt();
        for (Sneaker s : sneakerService.filterByYear(year)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void searchByPriceAndYear(Scanner scanner) {
        System.out.print("Enter max price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter minimum release year: ");
        int year = scanner.nextInt();
        for (Sneaker s : sneakerService.searchByPriceAndYear(price, year)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void viewById(Scanner scanner) {
        System.out.print("Enter sneaker id: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerService.findById(id);
        System.out.println(sneaker.getId() + " - " + sneaker.getModel() + " | $" + sneaker.getPrice() + " | " + sneaker.getReleaseYear());
    }

    private void addSneaker(Scanner scanner) {
        System.out.println("Available brands:");
        for (Brand b : sneakerService.listBrands()) {
            System.out.println(b.getId() + ") " + b.getName());
        }
        System.out.print("Choose brand id: ");
        long brandId = scanner.nextLong();
        System.out.print("Enter model: ");
        String model = scanner.next();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter release year: ");
        int year = scanner.nextInt();
        Sneaker sneaker = sneakerService.add(model, price, year, brandId);
        System.out.println("Sneaker added with id " + sneaker.getId() + ".");
    }

    private void updatePrice(Scanner scanner) {
        System.out.print("Enter sneaker id to update: ");
        long id = scanner.nextLong();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        sneakerService.updatePrice(id, price);
        System.out.println("Price updated.");
    }

    private void deleteSneaker(Scanner scanner) {
        System.out.print("Enter sneaker id to delete: ");
        long id = scanner.nextLong();
        sneakerService.delete(id);
        System.out.println("Sneaker deleted.");
    }

    private void searchByBrand(Scanner scanner) {
        System.out.print("Enter brand name: ");
        String name = scanner.next();
        for (Sneaker s : sneakerService.listByBrand(name)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear() + " | " + s.getBrand().getName());
        }
    }
}
