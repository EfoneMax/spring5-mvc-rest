package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final CategoryRepository repository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository repository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        repository.save(fruits);
        repository.save(dried);
        repository.save(fresh);
        repository.save(exotic);
        repository.save(nuts);

        Customer customer1 = Customer.builder().id(1L).firstname("one").lastname("lst").build();
        Customer customer2 = Customer.builder().id(2L).firstname("two").lastname("2cnd").build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        Vendor firstVendor = Vendor.builder().id(1L).name("FirstVendor").build();
        Vendor secondVendor = Vendor.builder().id(2L).name("SecondVendor").build();

        vendorRepository.save(firstVendor);
        vendorRepository.save(secondVendor);

        System.out.println("Data Loaded = " + repository.count() );

    }
}
