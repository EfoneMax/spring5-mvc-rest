package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VendorServiceImplTest {
    @Mock
    VendorRepository repository;

    VendorService service;

    VendorMapper mapper = VendorMapper.INSTANCE;

    Vendor firstVendor;
    Vendor secondVendor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new VendorServiceImpl(repository);

        firstVendor = Vendor.builder().id(1L).name("FirstVendor").build();
        secondVendor = Vendor.builder().id(2L).name("secondVendor").build();
    }

    @Test
    public void testGetById() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.of(firstVendor));

        //when
        VendorDTO vendorDTO = service.getById(1L);

        //then
        assertNotNull(vendorDTO);
        assertEquals(firstVendor.getId(), vendorDTO.getId());
        assertEquals(firstVendor.getName(), vendorDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", vendorDTO.getVendorUrl());
    }

    @Test
    public void testGetAllVendors() {
        //given
        when(repository.findAll()).thenReturn(Arrays.asList(firstVendor, secondVendor));
        
        //when
        List<VendorDTO> allVendors = service.getAllVendors();
        
        //then
        assertEquals(2, allVendors.size());
    }

    @Test
    public void testCreateNewVendor() {
        //given
        firstVendor.setId(null);
        when(repository.save(any(Vendor.class))).thenReturn(firstVendor);
        
        //when
        VendorDTO savedVendor = service.createNewVendor(mapper.VendorToVendorDTO(firstVendor));

        //then
        assertEquals(firstVendor.getName(), savedVendor.getName());
        assertEquals(VendorController.BASE_URL + "/1", savedVendor.getVendorUrl());
    }

    @Test
    public void testReplaceVendor() {
        //given
        VendorDTO vendorDTO = mapper.VendorToVendorDTO(firstVendor);
        when(repository.save(any(Vendor.class))).thenReturn(firstVendor);

        //when
        VendorDTO newVendor = service.replaceVendor(1L, vendorDTO);

        //then
        assertEquals(firstVendor.getName(), newVendor.getName());
    }

    @Test
    public void testPatchVendor() {
        //given
        VendorDTO vendorDTO = mapper.VendorToVendorDTO(firstVendor);
        vendorDTO.setName(null);
        when(repository.save(any(Vendor.class))).thenReturn(firstVendor);

        //when
        VendorDTO patchedVendor = service.patchVendor(1L, vendorDTO);

        //then
        assertNotNull(patchedVendor);
        assertNotNull(patchedVendor.getName());
        assertEquals(VendorController.BASE_URL + "/1", patchedVendor.getVendorUrl());
    }

    @Test
    public void testDeleteVendor() {
        //given/when
        service.deleteVendor(1L);

        //then
        verify(repository, times(1)).deleteById(anyLong());
    }
}