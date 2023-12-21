package uz.in.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandartCurrencyDTO {
    private String code;
    private String ccy;
    private String ccyNm_UZ;
    private String rate;
    private String date;
}
