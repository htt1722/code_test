package core;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentService {
    void init();
    boolean collectData();
    void outputData();
    void setRateValue(Map<String, BigDecimal> rateMap);
}
