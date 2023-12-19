package uz.in.currency.domain.dto;

import lombok.*;
import uz.in.currency.domain.role.UserRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private String fullName;

    private String email;

    private String password;

    private UserRole role;
}
