package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        Vendor vendor1 = new Vendor();
        vendor1.setFirstName("Fred");
        vendor1.setLastName("Thompson");

        Vendor vendor2 = new Vendor();
        vendor2.setFirstName("Barney");
        vendor2.setLastName("Bubble");

        // BDD: Behavior Driven Development
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(vendor1, vendor2));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        Vendor vendor1 = new Vendor();
        vendor1.setFirstName("Fred");
        vendor1.setLastName("Thompson");

        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(vendor1));

        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBody(Vendor.class);
    }
}