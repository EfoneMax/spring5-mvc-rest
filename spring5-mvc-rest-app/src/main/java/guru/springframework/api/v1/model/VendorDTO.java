package guru.springframework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "this is VendorDTO", value = "superVendorDTO")
public class VendorDTO {
    @Builder
    public VendorDTO(Long id, String name, String vendorUrl) {
        this.id = id;
        this.name = name;
        this.vendorUrl = vendorUrl;
    }

    private Long id;
    @ApiModelProperty(required = true, position = 1, value = "this is name")
    private String name;
    @JsonProperty("vendor_url")
    private String vendorUrl;
}
