package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorDTO {
    @Builder
    public VendorDTO(Long id, String name, String vendorUrl) {
        this.id = id;
        this.name = name;
        this.vendorUrl = vendorUrl;
    }

    private Long id;
    private String name;
    @JsonProperty("vendor_url")
    private String vendorUrl;
}
