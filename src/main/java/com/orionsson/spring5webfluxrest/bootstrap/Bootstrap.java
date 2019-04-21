package com.orionsson.spring5webfluxrest.bootstrap;

import com.orionsson.spring5webfluxrest.domain.Category;
import com.orionsson.spring5webfluxrest.domain.Vendor;
import com.orionsson.spring5webfluxrest.repositories.CategoryRepository;
import com.orionsson.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {
    private CategoryRepository categoryRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadVendors();
    }

    private void loadCategories(){
        if(categoryRepository.count().block()==0){
            //load data
            System.out.println("#### LOADING CATEGORIES ON BOOTSTRAP #####");

            categoryRepository.save(Category.builder()
                    .description("Fruits").build()).block();

            categoryRepository.save(Category.builder()
                    .description("Nuts").build()).block();

            categoryRepository.save(Category.builder()
                    .description("Breads").build()).block();

            categoryRepository.save(Category.builder()
                    .description("Meats").build()).block();

            categoryRepository.save(Category.builder()
                    .description("Eggs").build()).block();

            System.out.println("Loaded Categories: " + categoryRepository.count().block());
        }
    }
    private void loadVendors(){
        if(vendorRepository.count().block()==0) {
            //load data
            System.out.println("#### LOADING VENDORS ON BOOTSTRAP #####");
            vendorRepository.save(Vendor.builder()
                    .firstName("Joe")
                    .lastName("Buck").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Micheal")
                    .lastName("Weston").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Jessie")
                    .lastName("Waters").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Bill")
                    .lastName("Nershi").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Jimmy")
                    .lastName("Buffett").build()).block();

            System.out.println("Loaded Vendors: " + vendorRepository.count().block());
        }
    }
}
