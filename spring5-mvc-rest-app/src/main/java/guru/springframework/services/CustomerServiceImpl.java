package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;


import guru.springframework.controllers.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.exceptions.ResourceNotFoundException;
import guru.springframework.model.CustomerDTO;
import guru.springframework.repositories.CustomerRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerMapper mapper, CustomerRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Nullable
    public CustomerDTO getById(Long id) {
        return repository.findById(id).map(mapper::customerToCustomerDTO)
                .map(customerDTO -> {
                    customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + id);
                    return customerDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return repository.findAll().stream()
        .map(customer -> {
            CustomerDTO customerDTO = mapper.customerToCustomerDTO(customer);
            customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + customer.getId());
            return customerDTO;
        })
        .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return saveAndReturnDTO(mapper.customerDTOToCustomer(customerDTO));
    }

    private CustomerDTO saveAndReturnDTO(Customer customer) {
        Customer savedCustomer = repository.save(customer);
        CustomerDTO customerDTO = mapper.customerToCustomerDTO(savedCustomer);
        customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" +savedCustomer.getId());

        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = mapper.customerDTOToCustomer(customerDTO);
        customer.setId(id);

        return saveAndReturnDTO(customer);
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return repository.findById(id).map(customer -> {
            if(customerDTO.getFirstname() != null){
                customer.setFirstname(customerDTO.getFirstname());
            }

            if(customerDTO.getLastname() != null){
                customer.setLastname(customerDTO.getLastname());
            }

            CustomerDTO returnDTO = mapper.customerToCustomerDTO(repository.save(customer));
            returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + id);

            return returnDTO;
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}
