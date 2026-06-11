package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {
    private final BrandRepository brandRepository;
    private final SneakerRepository sneakerRepository;

    public StartupRunner(BrandRepository brandRepository, SneakerRepository sneakerRepository) {
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
    }

    @Override
    public void run(String... args) {
        seedData();

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

    private void searchByBrand(Scanner scanner) {
        System.out.print("Enter brand name: ");
        String name = scanner.next();
        for (Sneaker s : sneakerRepository.findByBrandName(name)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear() + " | " + s.getBrand().getName());
        }
    }

    private void searchByModel(Scanner scanner) {
        System.out.print("Enter model text to search: ");
        String text = scanner.next();

        for (Sneaker s : sneakerRepository.findByModelContaining(text)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void filterByPrice(Scanner scanner) {
        System.out.print("Enter max price: ");
        double price = scanner.nextDouble();

        for (Sneaker s : sneakerRepository.findByPriceLessThan(price)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void filterByYear(Scanner scanner) {
        System.out.print("Enter release year: ");
        int year = scanner.nextInt();

        for (Sneaker s : sneakerRepository.findByReleaseYear(year)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void searchByPriceAndYear(Scanner scanner) {
        System.out.print("Enter max price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter minimum release year: ");
        int year = scanner.nextInt();

        for (Sneaker s : sneakerRepository.findByPriceAndYear(price, year)) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear());
        }
    }

    private void viewById(Scanner scanner) {
        System.out.print("Enter sneaker id: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerRepository.findById(id).orElse(null);
        if (sneaker == null) {
            System.out.println("No sneaker with that id.");
        } else {
            System.out.println(sneaker.getId() + " - " + sneaker.getModel() + " | $" + sneaker.getPrice() + " | " + sneaker.getReleaseYear());
        }
    }

    private void addSneaker(Scanner scanner) {
        System.out.println("Available brands:");
        for (Brand b : brandRepository.findAll()) {
            System.out.println(b.getId() + ") " + b.getName());
        }
        System.out.print("Choose brand id: ");
        long brandId = scanner.nextLong();
        Brand brand = brandRepository.findById(brandId).orElse(null);
        if (brand == null) {
            System.out.println("No brand with that id.");
            return;
        }
        System.out.print("Enter model: ");
        String model = scanner.next();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter release year: ");
        int year = scanner.nextInt();
        sneakerRepository.save(new Sneaker(model, price, year, brand));
        System.out.println("Sneaker added.");
    }

    private void updatePrice(Scanner scanner) {
        System.out.print("Enter sneaker id to update: ");
        long id = scanner.nextLong();
        Sneaker sneaker = sneakerRepository.findById(id).orElse(null);
        if (sneaker == null) {
            System.out.println("No sneaker with that id.");
        } else {
            System.out.print("Enter new price: ");
            double price = scanner.nextDouble();
            sneaker.setPrice(price);
            sneakerRepository.save(sneaker);
            System.out.println("Price updated.");
        }
    }

    private void deleteSneaker(Scanner scanner) {
        System.out.print("Enter sneaker id to delete: ");
        long id = scanner.nextLong();
        if (sneakerRepository.existsById(id)) {
            sneakerRepository.deleteById(id);
            System.out.println("Sneaker deleted.");
        } else {
            System.out.println("No sneaker with that id.");
        }
    }

    private void seedData() {
        if (brandRepository.count() > 0 && sneakerRepository.count() > 0) return;

        Brand nike = brandRepository.save(new Brand("Nike"));
        Brand adidas = brandRepository.save(new Brand("Adidas"));
        Brand newBalance = brandRepository.save(new Brand("New Balance"));

        sneakerRepository.save(new Sneaker("Air Max 90", 120.00, 2023, nike));
        sneakerRepository.save(new Sneaker("Ultraboost 22", 180.00, 2022, adidas));
        sneakerRepository.save(new Sneaker("990v5", 185.00, 2023, newBalance));
    }

    private void listSneakers() {
        System.out.println("\nTotal sneakers: " + sneakerRepository.count());
        for (Sneaker s : sneakerRepository.findAll()) {
            System.out.println(s.getId() + " - " + s.getModel() + " | $" + s.getPrice() + " | " + s.getReleaseYear() + " | " + s.getBrand().getName());
        }
    }
}
