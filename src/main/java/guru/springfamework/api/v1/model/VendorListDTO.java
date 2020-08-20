package guru.springfamework.api.v1.model;

import guru.springfamework.domain.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VendorListDTO {
    List<VendorDTO> vendors;
}
