package uz.in.currency.domain.dto;

import lombok.*;
import uz.in.currency.domain.role.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDto {

    private UUID id;

    private String fullName;

    private String email;

    private UserRole role;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
