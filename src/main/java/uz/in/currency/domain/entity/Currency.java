package uz.in.currency.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "currencies")
public class Currency {

    @Id
    private Long id; //tartib raqami

    @Column(unique = true,nullable = false)
    private String code; //valyutaning sonli kodi

    private String ccy; //valyutaning ramzli kodi

    private String ccyNm_RU; //valyutaning rus tilidagi nomi

    private String ccyNm_UZ; //valyutaning o'zbek(lotin) tilidagi nomi

    private String ccyNm_UZC; //valyutaning o'zbek(kirillitsa) tilidagi nomi

    private String ccyNm_EN; //valyutaning ingliz tilidagi nomi

    private String nominal; //valyutaning birliklar soni

    private String rate; //valyuta kursi

    private String diff; //valyutaning kursining o'zgarishi

    private String date; //kursning amal qilish sanasi

}
