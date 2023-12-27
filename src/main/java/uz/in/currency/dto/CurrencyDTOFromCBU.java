package uz.in.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CurrencyDTOFromCBU extends StandardCurrencyDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("CcyNm_RU")
    private String ccyNm_RU;

    @JsonProperty("CcyNm_UZC")
    private String ccyNm_UZC;

    @JsonProperty("CcyNm_EN")
    private String ccyNm_EN;

    @JsonProperty("Nominal")
    private String nominal;

    @JsonProperty("Diff")
    private String diff;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Ccy")
    private String ccy;

    @JsonProperty("CcyNm_UZ")
    private String ccyNm_UZ;

    @JsonProperty("Rate")
    private String rate;

    @JsonProperty("Date")
    private String date;
}
