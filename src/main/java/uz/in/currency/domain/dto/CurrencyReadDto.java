package uz.in.currency.domain.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyReadDto {

    private String code;

    private String ccy;

    private String ccyNm_RU;

    private String ccyNm_UZ;

    private String ccyNm_UZC;

    private String ccyNm_EN;

    private String nominal;

    private String rate;

    private String diff;

    private String date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyReadDto that = (CurrencyReadDto) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(ccy, that.ccy) &&
                Objects.equals(ccyNm_RU, that.ccyNm_RU) &&
                Objects.equals(ccyNm_UZ, that.ccyNm_UZ) &&
                Objects.equals(ccyNm_UZC, that.ccyNm_UZC) &&
                Objects.equals(ccyNm_EN, that.ccyNm_EN) &&
                Objects.equals(nominal, that.nominal) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(diff, that.diff) &&
                Objects.equals(date, that.date);
    }
}

