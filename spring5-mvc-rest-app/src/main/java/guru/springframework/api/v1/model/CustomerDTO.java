package guru.springframework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    @Builder
    public CustomerDTO(Long id, String firstname, String lastname, String customerUrl) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.customerUrl = customerUrl;
    }

    private Long id;
    @ApiModelProperty(value = "This is first name rly", required = true)
    private String firstname;
    @ApiModelProperty(required = false)
    private String lastname;
    @JsonProperty("customer_url")
    private String customerUrl;
}
