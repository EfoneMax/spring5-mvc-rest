package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {
    public static final CustomerDTO oneCustomer = CustomerDTO.builder()
            .id(1L)
            .firstname("OneFirstname")
            .lastname("OneLastname")
            .build();
    public static final CustomerDTO twoCustomer = CustomerDTO.builder()
            .id(2L)
            .firstname("TwoFirstname")
            .lastname("TwoLastname")
            .build();

    @Mock
    CustomerService service;

    @InjectMocks
    CustomerController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        when(service.getAllCustomers()).thenReturn(Arrays.asList(oneCustomer, twoCustomer));

        mockMvc.perform(get(CustomerController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    public void testGetCustomerById() {
        when(service.getById(any())).thenReturn(oneCustomer);

    }

    @Test
    public void testCreateNewCustomer() throws Exception {
        //given
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(oneCustomer.getFirstname());
        returnDTO.setLastname(oneCustomer.getLastname());
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        oneCustomer.setId(null);
        when(service.createNewCustomer(oneCustomer)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(post(CustomerController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(oneCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(returnDTO.getFirstname())))
                .andExpect(jsonPath("$.customer_url", equalTo(returnDTO.getCustomerUrl())));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        //given
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(oneCustomer.getFirstname());
        returnDTO.setLastname(oneCustomer.getLastname());
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        oneCustomer.setId(null);
        when(service.updateCustomer(1L, oneCustomer)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(oneCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(returnDTO.getFirstname())))
                .andExpect(jsonPath("$.customer_url", equalTo(returnDTO.getCustomerUrl())));
    }

    @Test
    public void testPatchCustomer() throws Exception {
        //given
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(oneCustomer.getFirstname());
        returnDTO.setLastname("NewLastname");
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        oneCustomer.setId(null);
        when(service.patchCustomer(1L, oneCustomer)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(oneCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname", equalTo("NewLastname")))
                .andExpect(jsonPath("$.customer_url", equalTo(returnDTO.getCustomerUrl())));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        //when/then
        mockMvc.perform(delete(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}