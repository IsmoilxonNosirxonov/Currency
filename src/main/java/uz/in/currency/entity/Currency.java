package uz.in.currency.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "currencies")
public class Currency {

    @Id
    private Long code; //valyutaning sonli kodi

    private String ccy; //valyutaning ramzli kodi

    private String ccyNm_UZ; //valyutaning o'zbek(lotin) tilidagi nomi

    private Double rate; //valyuta kursi

    private LocalDate date; //kursning amal qilish sanasi

}
