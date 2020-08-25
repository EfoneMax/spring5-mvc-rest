package guru.springframework.controllers.v1;


import guru.springframework.exceptionHandlers.RestResponseEntityExceptionHandler;
import guru.springframework.exceptions.ResourceNotFoundException;
import guru.springframework.model.CustomerDTO;
import guru.springframework.services.CustomerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springframework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {
    CustomerDTO oneCustomer = new CustomerDTO();
    CustomerDTO twoCustomer = new CustomerDTO();

    @Mock
    CustomerService service;

    @InjectMocks
    CustomerController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(RestResponseEntityExceptionHandler.class).build();
        oneCustomer.setFirstname("OneFirstname");
        oneCustomer.setLastname("OneLastname");

        twoCustomer.setFirstname("TwoFirstname");
        twoCustomer.setLastname("TwoLastname");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        when(service.getAllCustomers()).thenReturn(Arrays.asList(oneCustomer, twoCustomer));

        mockMvc.perform(get(CustomerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
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

        when(service.createNewCustomer(any())).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(post(CustomerController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(oneCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(returnDTO.getFirstname())))
                .andExpect(jsonPath("$.customerUrl", equalTo(returnDTO.getCustomerUrl())));
    }

    @Ignore
    @Test
    public void testUpdateCustomer() throws Exception {
        //given
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(oneCustomer.getFirstname());
        returnDTO.setLastname(oneCustomer.getLastname());
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(service.updateCustomer(1L, any(CustomerDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(oneCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(returnDTO.getFirstname())))
                .andExpect(jsonPath("$.customerUrl", equalTo(returnDTO.getCustomerUrl())));
    }

    @Ignore
    @Test
    public void testPatchCustomer() throws Exception {
        //given
        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(oneCustomer.getFirstname());
        returnDTO.setLastname("NewLastname");
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(service.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(oneCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname", equalTo("NewLastname")))
                .andExpect(jsonPath("$.customerUrl", equalTo(returnDTO.getCustomerUrl())));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        //when/then
        mockMvc.perform(delete(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByIdNotFoundException() throws Exception {
        when(service.getById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.BASE_URL +"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}