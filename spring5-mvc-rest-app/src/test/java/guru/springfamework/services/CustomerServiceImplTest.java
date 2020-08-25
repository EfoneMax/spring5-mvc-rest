package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controllers.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {
    public static final Customer oneCustomer = Customer.builder()
            .id(1L)
            .firstname("OneFirstname")
            .lastname("OneLastname")
            .build();
    public static final Customer twoCustomer = Customer.builder()
            .id(2L)
            .firstname("TwoFirstname")
            .lastname("TwoLastname")
            .build();

    @Mock
    CustomerRepository repository;

    CustomerService service;

    CustomerMapper mapper = CustomerMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.service = new CustomerServiceImpl(CustomerMapper.INSTANCE, repository);
    }

    @Test
    public void testGetByFirstname() {
        //given
        when(repository.findById(1L)).thenReturn(Optional.of(oneCustomer));

        //when
        CustomerDTO customer = service.getById(1L);

        //then
        assertEquals(oneCustomer.getFirstname(), customer.getFirstname());
        assertEquals(oneCustomer.getId(), customer.getId());
    }

    @Test
    public void testGetAllCustomers() {
        //given
        List<Customer> customers = Arrays.asList(oneCustomer, twoCustomer);
        when(repository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> allCustomers = service.getAllCustomers();

        //then
        assertEquals(2, allCustomers.size());
    }

    @Test
    public void testCreateNewCustomer() {
        //given
        oneCustomer.setId(3L);
        CustomerDTO saveCustomerDTO = mapper.customerToCustomerDTO(oneCustomer);
        when(repository.save(any(Customer.class))).thenReturn(oneCustomer);

        //when
        CustomerDTO savedCustomerDTO = service.createNewCustomer(saveCustomerDTO);

        //then
        assertEquals(saveCustomerDTO.getFirstname(), savedCustomerDTO.getFirstname());
        assertEquals(CustomerController.BASE_URL + "/3", savedCustomerDTO.getCustomerUrl());
    }

    @Test
    public void testUpdateCustomer() {
        //given
        CustomerDTO customerDTO = mapper.customerToCustomerDTO(oneCustomer);
        when(repository.save(any(Customer.class))).thenReturn(oneCustomer);

        //when
        CustomerDTO updatedCustomer = service.updateCustomer(1L, customerDTO);

        //then
        assertEquals(customerDTO.getFirstname(), updatedCustomer.getFirstname());
        assertEquals(CustomerController.BASE_URL + "/1", updatedCustomer.getCustomerUrl());
    }

    @Test
    public void testDeleteCustomerById() {
        //given
        Long id = 1L;

        //when
        service.deleteCustomer(id);

        //then
        verify(repository, times((1))).deleteById(anyLong());
    }
}