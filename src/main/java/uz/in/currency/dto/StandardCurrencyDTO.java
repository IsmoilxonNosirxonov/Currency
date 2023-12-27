package uz.in.currency.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StandardCurrencyDTO {
    private String code;
    private String ccy;
    private String ccyNm_UZ;
    private String rate;
    private String date;
}
