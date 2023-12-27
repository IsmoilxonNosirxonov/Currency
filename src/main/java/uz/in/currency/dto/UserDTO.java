package uz.in.currency.dto;

import lombok.*;
import uz.in.currency.enumeration.UserRole;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String fullName;

    private String email;

    private String password;

    private UserRole role;

}
