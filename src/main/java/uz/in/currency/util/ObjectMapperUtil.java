package uz.in.currency.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import uz.in.currency.exception.CommonException;
import uz.in.currency.service.jsonObject.CurrencyCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtil.class);

    public static Map<String, String> getCcyCodesFromJson() {
        try {
            CurrencyCode currencyCode = objectMapper.readValue(getFileAsByteArray(), CurrencyCode.class);

            String exception;

            if (currencyCode == null) {
                exception = "CurrencyCode is null";
                logger.warn(exception);
                throw new CommonException(exception);
            }

            return currencyCode.getCurrencyCodes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getFileAsByteArray() {
        try {
            InputStream in = new ClassPathResource("currency.json").getInputStream();
            return ByteStreams.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
