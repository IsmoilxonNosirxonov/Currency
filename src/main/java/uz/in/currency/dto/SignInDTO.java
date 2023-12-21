package uz.in.currency.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
