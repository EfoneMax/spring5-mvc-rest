package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
        this.service = new CustomerServiceImpl(repository);
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
        assertEquals("/api/v1/customers/3", savedCustomerDTO.getCustomerUrl());
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
        assertEquals("/api/v1/customers/1", updatedCustomer.getCustomerUrl());
    }
}