package com.pluralsight.sneakerdrops.service;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SneakerService {

    private final SneakerRepository sneakerRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public SneakerService(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    public long count() {
        return sneakerRepository.count();
    }

    public List<Sneaker> listAll() {
        return sneakerRepository.findAll();
    }

    public List<Sneaker> searchByModel(String text) {
        return sneakerRepository.findByModelContaining(text);
    }

    public List<Sneaker> filterByPrice(double price) {
        return sneakerRepository.findByPriceLessThan(price);
    }

    public List<Sneaker> filterByYear(int year) {
        return sneakerRepository.findByReleaseYear(year);
    }

    public List<Sneaker> searchByPriceAndYear(double price, int year) {
        return sneakerRepository.findByPriceAndYear(price, year);
    }

    public Sneaker findById(long id) {
        return sneakerRepository.findById(id)
                .orElseThrow(() -> new SneakerNotFoundException("No sneaker found with id: " + id));
    }

    public Sneaker add(String model, double price, int year, long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new SneakerNotFoundException("No brand found with id: " + brandId));
        return sneakerRepository.save(new Sneaker(model, price, year, brand));
    }

    public void updatePrice(long id, double price) {
        Sneaker sneaker = findById(id);
        sneaker.setPrice(price);
        sneakerRepository.save(sneaker);
    }

    public void delete(long id) {
        if (!sneakerRepository.existsById(id)) {
            throw new SneakerNotFoundException("No sneaker found with id: " + id);
        }
        sneakerRepository.deleteById(id);
    }

    public List<Sneaker> listByBrand(String name) {
        return sneakerRepository.findByBrandName(name);
    }

    public List<Brand> listBrands() {
        return brandRepository.findAll();
    }

    public void seedData() {
        if (brandRepository.count() > 0 && sneakerRepository.count() > 0) return;

        Brand nike = brandRepository.save(new Brand("Nike"));
        Brand adidas = brandRepository.save(new Brand("Adidas"));
        Brand newBalance = brandRepository.save(new Brand("New Balance"));

        sneakerRepository.save(new Sneaker("Air Max 90", 120.00, 2023, nike));
        sneakerRepository.save(new Sneaker("Ultraboost 22", 180.00, 2022, adidas));
        sneakerRepository.save(new Sneaker("990v5", 185.00, 2023, newBalance));
    }
}
