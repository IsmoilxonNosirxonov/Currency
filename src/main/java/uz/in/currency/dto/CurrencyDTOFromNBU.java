package uz.in.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrencyDTOFromNBU extends StandardCurrencyDTO {

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
