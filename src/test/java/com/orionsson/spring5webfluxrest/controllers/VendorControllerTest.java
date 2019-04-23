package com.orionsson.spring5webfluxrest.controllers;

import com.orionsson.spring5webfluxrest.domain.Vendor;
import com.orionsson.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("ftm").lastName("cpr").build(),
                        Vendor.builder().firstName("rcp").lastName("cpr").build()));
        webTestClient
                .get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getById() {
        given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().firstName("rcp").lastName("cpr").build()));
        webTestClient
                .get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));
        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("ftm").lastName("cpr").build());
        webTestClient
                .post()
                .uri("/api/v1/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void update(){
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));
        Mono<Vendor> vendorToUpdate = Mono.just(
                Vendor.builder().firstName("rcp").lastName("cpr").build());
        webTestClient
                .put()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchWithChange(){
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("ftm").lastName("cpr").build()));
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("ftm").lastName("cpr").build()));
        Mono<Vendor> vendorToUpdate = Mono.just(
                Vendor.builder().firstName("rcp").lastName("cpr").build());
        webTestClient
                .patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository).save(any());
    }
    @Test
    public void patchWithNoChange(){
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("ftm").lastName("cpr").build()));
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("ftm").lastName("cpr").build()));
        Mono<Vendor> vendorToUpdate = Mono.just(
                Vendor.builder().firstName("ftm").lastName("cpr").build());
        webTestClient
                .patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository,never()).save(any());
    }
}