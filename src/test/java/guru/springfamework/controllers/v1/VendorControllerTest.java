package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.exceptionHandlers.RestResponseEntityExceptionHandler;
import guru.springfamework.repositories.VendorRepository;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
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

@RunWith(MockitoJUnitRunner.class)
public class VendorControllerTest {
    @Mock
    VendorRepository repository;

    @Mock
    VendorService service;

    @InjectMocks
    VendorController controller;

    VendorDTO firstVendor;
    VendorDTO secondVendor;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        // MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(RestResponseEntityExceptionHandler.class)
                .build();
        firstVendor = VendorDTO.builder()
                .id(1L)
                .name("FirstVendor")
                .vendorUrl(VendorController.BASE_URL + "/1")
                .build();

        secondVendor = VendorDTO.builder().
                id(2L).
                name("SecondVendor")
                .vendorUrl(VendorController.BASE_URL + "/2")
                .build();
    }

    @Test
    public void testGetAllVendors() throws Exception {
        when(service.getAllVendors()).thenReturn(Arrays.asList(firstVendor, secondVendor));

        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void testGetVendorById() throws Exception {
        when(service.getById(anyLong())).thenReturn(firstVendor);

        mockMvc.perform(get(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(firstVendor.getName())));
    }

    @Test
    public void testCreateNewVendor() throws Exception {
        when(service.createNewVendor(any(VendorDTO.class))).thenReturn(firstVendor);

        mockMvc.perform(post(VendorController.BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(firstVendor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(firstVendor.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(firstVendor.getVendorUrl())));
    }

    @Test
    public void testReplaceVendor() throws Exception {
        when(service.replaceVendor(anyLong(), any(VendorDTO.class))).thenReturn(firstVendor);

        mockMvc.perform(put(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(firstVendor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(firstVendor.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(firstVendor.getVendorUrl())));
    }

    @Test
    public void testPatchVendor() throws Exception {
        when(service.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(firstVendor);

        mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(firstVendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(firstVendor.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(firstVendor.getVendorUrl())));
    }

    @Test
    public void testDeleteVendor() throws Exception {
        mockMvc.perform(delete(VendorController.BASE_URL + "/1")
        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}