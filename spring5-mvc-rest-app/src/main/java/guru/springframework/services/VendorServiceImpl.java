package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.exceptions.ResourceNotFoundException;
import guru.springframework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository repository;
    private final VendorMapper mapper = VendorMapper.INSTANCE;

    public VendorServiceImpl(VendorRepository repository) {
        this.repository = repository;
    }

    @Override
    public VendorDTO getById(Long id) {
        return repository.findById(id).map(mapper::VendorToVendorDTO)
                .map(vendorDTO -> {
                    setVendorUrl(vendorDTO);
                    return vendorDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        return repository.findAll().stream().map(vendor -> {
            VendorDTO vendorDTO = mapper.VendorToVendorDTO(vendor);
            setVendorUrl(vendorDTO);
            return vendorDTO;
        })
        .collect(Collectors.toList());
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
       return saveAndReturnDTO(vendorDTO);
    }

    @Override
    public VendorDTO replaceVendor(Long id, VendorDTO vendorDTO) {
        vendorDTO.setId(id);
        return saveAndReturnDTO(vendorDTO);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return repository.findById(id).map(vendor -> {
            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }
            Vendor savedVendor = repository.save(vendor);

            VendorDTO returnDTO = mapper.VendorToVendorDTO(savedVendor);
            setVendorUrl(returnDTO);
            return returnDTO;
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendor(Long id) {
        repository.deleteById(id);

    }

    private VendorDTO saveAndReturnDTO(VendorDTO vendorDTO) {
        Vendor vendor = mapper.VendorDTOToVendor(vendorDTO);
        Vendor savedVendor = repository.save(vendor);
        vendorDTO.setId(savedVendor.getId());
        setVendorUrl(vendorDTO);
        return vendorDTO;
    }

    private void setVendorUrl(VendorDTO vendorDTO) {
        vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + vendorDTO.getId());
    }
}
