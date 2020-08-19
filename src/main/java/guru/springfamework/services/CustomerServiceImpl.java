package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.controllers.v1.CustomerController;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository repository;
    CustomerMapper mapper;

    public CustomerServiceImpl(CustomerMapper mapper, CustomerRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public CustomerDTO getByFirstname(String firstname) {
        return null;
    }

    @Override
    @Nullable
    public CustomerDTO getById(Long id) {
        return repository.findById(id).map(mapper::customerToCustomerDTO)
                .map(customerDTO -> {
                    customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + id);
                    return customerDTO;
                }).orElseThrow(RuntimeException::new);
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
        return saveAndReturnDTO(customerDTO);
    }

    private CustomerDTO saveAndReturnDTO(CustomerDTO customerDTO) {
        Customer customer = mapper.customerDTOToCustomer(customerDTO);
        Customer savedCustomer = repository.save(customer);

        customerDTO.setCustomerUrl(CustomerController.BASE_URL + "/" +savedCustomer.getId());

        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return saveAndReturnDTO(customerDTO);
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
            returnDTO.setCustomerUrl("/api/v1/customer/" + id);

            return returnDTO;
        }).orElseThrow(RuntimeException::new); //todo implement better exception handling;
    }

    @Override
    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}
