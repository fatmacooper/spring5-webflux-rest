package com.orionsson.spring5webfluxrest.controllers;

import com.orionsson.spring5webfluxrest.domain.Vendor;
import com.orionsson.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> list(){
        return  vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream){
        return  vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor){
        Vendor vendorToUpdate = vendorRepository.findById(id).block();
        if(!vendorToUpdate.getFirstName().equals(vendor.getFirstName()) ||
        !vendorToUpdate.getLastName().equals(vendor.getLastName())){
            vendorToUpdate.setFirstName(vendor.getFirstName());
            vendorToUpdate.setLastName(vendor.getLastName());
            return vendorRepository.save(vendorToUpdate);
        }
        return Mono.just(vendorToUpdate);
    }
}
