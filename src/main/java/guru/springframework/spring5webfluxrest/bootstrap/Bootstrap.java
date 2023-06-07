package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Command Line Runner is a class from SpringBoot that is executed on startup
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("HERE" + categoryRepository.count().block());
        if(categoryRepository.count().block() == 0){
            // load data
            System.out.println("Loading Data on Bootstrap");

           /* categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();*/

            Category category1 = new Category();
            category1.setDescription("Fruits");
            categoryRepository.save(category1).block();

            System.out.println("Loaded categories: "+ categoryRepository.count().block());

           /* vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Black").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Michael").lastName("Weston").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jessie").lastName("Waters").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Bill").lastName("Nershi").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jimmy").lastName("Buffet").build()).block();*/

            Vendor vendor1 = new Vendor();
            vendor1.setFirstName("Joe");
            vendor1.setLastName("Black");
            vendorRepository.save(vendor1).block();

            System.out.println("Loaded vendors: "+ vendorRepository.count().block());
        }

    }
}
