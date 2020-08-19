package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String firstname;
    private String lastname;
    @JsonProperty("customer_url")
    private String customerUrl;
}
