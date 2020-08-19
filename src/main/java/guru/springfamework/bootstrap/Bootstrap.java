package guru.springfamework.bootstrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final CategoryRepository repository;
    private final CustomerRepository customerRepository;

    public Bootstrap(CategoryRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
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

        System.out.println("Data Loaded = " + repository.count() );

    }
}
