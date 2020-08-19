package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    public static final String API_V_1_CUSTOMERS = "/api/v1/customers";
    CustomerRepository repository;
    CustomerMapper mapper = CustomerMapper.INSTANCE;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerDTO getByFirstname(String firstname) {
        return null;
    }

    @Override
    @Nullable
    public CustomerDTO getById(Long id) {
        Optional<Customer> customer = repository.findById(id);
        return customer.map(value -> mapper.customerToCustomerDTO(value)).orElse(null);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return repository.findAll().stream()
        .map(customer -> {
            CustomerDTO customerDTO = mapper.customerToCustomerDTO(customer);
            customerDTO.setCustomerUrl(API_V_1_CUSTOMERS + "/" + customer.getId());
            return customerDTO;
        })
        .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return saveAndReturnDTO(customerDTO);
    }

    private CustomerDTO saveAndReturnDTO(CustomerDTO customerDTO) {
        Customer customer = mapper.customerDTOToCustomer(customerDTO);
        Customer savedCustomer = repository.save(customer);

        customerDTO.setCustomerUrl(API_V_1_CUSTOMERS + "/" +savedCustomer.getId());

        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return saveAndReturnDTO(customerDTO);
    }
}
