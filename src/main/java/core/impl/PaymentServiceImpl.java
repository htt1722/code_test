package core.impl;

import core.PaymentService;
import exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Payment;
import utils.CheckPaymentUtil;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);
    Map<String, BigDecimal> dataMap = new ConcurrentHashMap<>();
    private Map<String, BigDecimal> rateMap = new HashMap<>();

    public void init() {
        Scanner sc = new Scanner(System.in);
        LOG.info("--------- Please enter the initialization file name: ------------------ ");
        String fileName = sc.nextLine();
        // Read user specified file
        //String filePath = this.getClass().getResource("/").getPath() + fileName;
        InputStream inputFile = this.getClass().getResourceAsStream("/" + fileName);
        //File file = new File(filePath);
        if(null == inputFile){
            // If the specified file cannot be found, read from the default file
            //file = new File(this.getClass().getResource("/").getPath() + "payment_list.txt");
            inputFile = this.getClass().getResourceAsStream("/payment_list.txt");
            if(null == inputFile){
                throw new MyException("Cannot find the default file");
            }
            LOG.info("INFO: The file name entered does not exist. The default file will be used!");
        }
        InputStreamReader isr = null;
        BufferedReader br = null;
        try{
            isr = new InputStreamReader(inputFile, "utf-8");
            br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if(CheckPaymentUtil.checkIsValid(lineTxt)){
                    // set the data into a map
                    putData(dataMap, lineTxt);
                }else{
                    LOG.error("ERROR: The file format is incorrect!");
                    break;
                }
            }
        }catch(Exception e){
            LOG.error("ERROR: Error reading file!");
        }finally {
            try{
                if(null != isr){
                    isr.close();
                }
                if(null != br){
                    br.close();
                }
            }catch (Exception e){
                LOG.error("ERROR: Error closing file!");
            }
        }
    }

    public boolean collectData(){
        Scanner scanner = new Scanner(System.in);
        LOG.info("--------- Please enter the payments : --------------");
        String nextLine = scanner.nextLine();
        while (nextLine != null && !nextLine.equals("") && !nextLine.equals("quit")) {
            if(CheckPaymentUtil.checkIsValid(nextLine)){
                putData(dataMap, nextLine);
                nextLine = scanner.nextLine();
            }else{
                LOG.error("ERROR: The data entered is not in the correct format!");
                break;
            }
        }
        return false;
    }

    @Override
    public void outputData(){
        LOG.info("------------The calculation result is: ------------- ");
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
                LOG.info(payment.toString());
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
