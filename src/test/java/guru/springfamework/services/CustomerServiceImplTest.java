package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.service = new CustomerServiceImpl(repository);
    }

    @Test
    public void getByFirstname() {
        //given
        when(repository.findById(1L)).thenReturn(Optional.of(oneCustomer));

        //when
        CustomerDTO customer = service.getById(1L);

        //then
        assertEquals(oneCustomer.getFirstname(), customer.getFirstname());
        assertEquals(oneCustomer.getId(), customer.getId());
    }

    @Test
    public void getAllCustomers() {
        //given
        List<Customer> customers = Arrays.asList(oneCustomer, twoCustomer);
        when(repository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> allCustomers = service.getAllCustomers();

        //then
        assertEquals(2, allCustomers.size());
    }
}