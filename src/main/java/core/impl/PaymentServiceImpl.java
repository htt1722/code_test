package core.impl;

import core.PaymentService;
import pojo.Payment;
import utils.CheckPaymentUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentServiceImpl implements PaymentService {
    Map<String, BigDecimal> dataMap = new ConcurrentHashMap<>();
    private Map<String, BigDecimal> rateMap = new HashMap<>();

    public void init() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--------- Please enter the initialization file name: ------------------ ");
        String fileName = sc.nextLine();
        // Read user specified file
        String filePath = this.getClass().getResource("/").getPath() + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            // If the specified file cannot be found, read from the default file
            file = new File(this.getClass().getResource("/").getPath() + "payment_list.txt");
            System.out.println("INFO: The file name entered does not exist. The default file will be used!");
        }
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if(CheckPaymentUtil.checkIsValid(lineTxt)){
                    // set the data into a map
                    putData(dataMap, lineTxt);
                }else{
                    System.out.println("ERROR: The file format is incorrect!");
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("ERROR: Error reading file!");
        }finally {
            try{
                if(null != isr){
                    isr.close();
                }
                if(null != br){
                    br.close();
                }
            }catch (Exception e){
                System.out.println("ERROR: Error closing file!");
            }
        }
    }

    public boolean collectData(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("--------- Please enter the payments : --------------");
        String nextLine = scanner.nextLine();
        while (nextLine != null && !nextLine.equals("") && !nextLine.equals("quit")) {
            if(CheckPaymentUtil.checkIsValid(nextLine)){
                putData(dataMap, nextLine);
                nextLine = scanner.nextLine();
            }else{
                System.out.println("ERROR: The data entered is not in the correct format!");
                break;
            }
        }
        return false;
    }

    @Override
    public void outputData(){
        System.out.println("------------The calculation result is: ------------- ");
        dataMap.forEach((k, v) -> {
            Payment payment = new Payment();
            payment.setCurrency(k);
            payment.setAmount(v);
            Optional<Payment> optional = Optional.ofNullable(payment);
            // If the amount is 0, no output is required
            if(optional.isPresent() && BigDecimal.ZERO.compareTo(optional.get().getAmount()) != 0){
                // Calculate the value of the exchange rate
                if(null != rateMap){
                    BigDecimal rate = rateMap.get(k);
                    if(null != rate){
                        payment.setUsdAmount(v.multiply(rate));
                    }
                }
                System.out.println(payment.toString());
            }
        });
    }

    private void putData(Map<String, BigDecimal> dataMap, String inputStr){
        String[] inputArr = inputStr.split(" ");
        String key = inputArr[0];
        BigDecimal oldValue = dataMap.get(key);
        // Amount accumulation
        BigDecimal newValue = new BigDecimal(inputArr[1].trim()).add(oldValue == null ? BigDecimal.ZERO : oldValue);
        dataMap.put(key, newValue);
    }

    @Override
    public void setRateValue(Map<String, BigDecimal> rateMap) {
        this.rateMap = rateMap;
    }

}
