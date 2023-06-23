package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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

    @Test
    void testCreateVendor(){
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(new Vendor()));

        Vendor vendor = new Vendor();
        vendor.setFirstName("First name");
        vendor.setLastName("Last name");
        Mono<Vendor> vendorToSaveMono = Mono.just(vendor);

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    void testUpdateVendor(){
        //PUT updates an existing object
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(new Vendor()));

        Mono<Vendor> vendorMonoToUpdate = Mono.just(new Vendor());

        webTestClient.put()
                .uri("/api/v1/vendors/someid")
                .body(vendorMonoToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    public void testPatch() {
        Vendor vendorToReturn = new Vendor();
        vendorToReturn.setFirstName("First name");

        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(vendorToReturn));
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(new Vendor()));

        Vendor vendorToUp = new Vendor();
        vendorToUp.setFirstName("Some Vendor");
        Mono<Vendor> venToUpdateMono = Mono.just(vendorToUp);

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(venToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        BDDMockito.verify(vendorRepository).save(any());
    }
}