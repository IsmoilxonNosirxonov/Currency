package uz.in.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTOFromNBU extends StandartCurrencyDTO{

    @JsonProperty("nbu_buy_price")
    private String buyPrice;

    @JsonProperty("nbu_cell_price")
    private String cellPrice;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("code")
    private String ccy;

    @JsonProperty("title")
    private String ccyNm_UZ;

    @JsonProperty("cb_price")
    private String rate;

    @JsonProperty("date")
    private String date;
}
