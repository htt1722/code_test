package core;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentService {
    /**
     * Initialize data loading
     */
    void init();

    /**
     * collect data
     * @return true: the data is correct; false: the data is incorrect
     */
    boolean collectData();

    /**
     * output datas into the console
     */
    void outputData();

    /**
     * set the exchange rate
     * @param rateMap
     */
    void setRateValue(Map<String, BigDecimal> rateMap);
}
