package utils;

import java.math.BigDecimal;
import java.util.Optional;

public class CheckPaymentUtil {
    public static boolean checkIsValid(String inputPayment){
        Optional<String> paymentStr = Optional.ofNullable(inputPayment);
        if(!paymentStr.isPresent()){
            return false;
        }
        String[] paymentArr = inputPayment.split(" ");
        if(paymentArr.length == 2){
            if(paymentArr[0].length() != 3 || testAllUpperCase(paymentArr[0]) == false){
                return false;
            }
            if(!isNumeric(paymentArr[1].trim())){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean testAllUpperCase(String str){
        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if(c >= 97 && c <= 122) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
