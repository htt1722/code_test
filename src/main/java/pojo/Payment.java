package pojo;

import java.math.BigDecimal;

public class Payment {
    private String currency;
    private BigDecimal amount;
    private BigDecimal usdAmount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount.setScale(0, BigDecimal.ROUND_HALF_UP) : amount;
    }

    public BigDecimal getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(BigDecimal usdAmount) {
        this.usdAmount = usdAmount != null ? usdAmount.setScale(2, BigDecimal.ROUND_HALF_UP) : usdAmount;
    }

    @Override
    public String toString() {
        String str = currency + " " + amount;
        if(null != usdAmount){
            str += " (USD " + usdAmount + ")";
        }
        return str;
    }
}
