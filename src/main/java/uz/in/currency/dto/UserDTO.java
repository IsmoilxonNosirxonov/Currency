package uz.in.currency.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import uz.in.currency.role.UserRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = {"password"}, ignoreUnknown = true, allowGetters = true)
public class UserDTO {

    private String fullName;

    private String email;

    private String password;

    private UserRole role;
}
