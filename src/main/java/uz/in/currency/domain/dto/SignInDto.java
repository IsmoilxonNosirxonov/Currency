package uz.in.currency.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    private String email;

    private String password;
}
