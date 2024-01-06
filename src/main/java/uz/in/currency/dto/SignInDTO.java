package uz.in.currency.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
