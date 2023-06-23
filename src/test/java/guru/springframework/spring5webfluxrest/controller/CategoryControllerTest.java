package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
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

class CategoryControllerTest {
    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        Category cat1 = new Category();
        cat1.setDescription("Cat1");

        Category cat2 = new Category();
        cat2.setDescription("Cat2");

        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(cat1,cat2));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        Category cat1 = new Category();
        cat1.setDescription("Cat1");

        BDDMockito.given(categoryRepository.findById("someid"))
                .willReturn(Mono.just(cat1));

        webTestClient.get()
                .uri("/api/v1/categories/someid")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setDescription("Some Cat");
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(new Category()));

        Mono<Category> catToSaveMono = Mono.just(category);

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
    @Test
    public void testUpdate() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Category catToUp = new Category();
        catToUp.setDescription("Some Cat");
        Mono<Category> catToUpdateMono = Mono.just(catToUp);

        webTestClient.put()
                .uri("/api/v1/categories/asdf")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatch() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(new Category()));
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(new Category()));

        Category catToUp = new Category();
        catToUp.setDescription("Some Cat");
        Mono<Category> catToUpdateMono = Mono.just(catToUp);

        webTestClient.patch()
                .uri("/api/v1/categories/asdf")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        BDDMockito.verify(categoryRepository).save(any());
    }

}