package core.impl;

import core.PaymentService;
import pojo.Payment;
import utils.CheckPaymentUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentServiceImpl implements PaymentService {
    Map<String, BigDecimal> dataMap = new ConcurrentHashMap<>();
    public boolean collectData(){
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        while (nextLine != null && !nextLine.equals("") && !nextLine.equals("quit")) {
            if(CheckPaymentUtil.checkIsValid(nextLine)){
                putData(dataMap, nextLine);
                nextLine = scanner.nextLine();
            }else{
                System.out.println("The data entered is not in the correct format!");
                break;
            }
        }
        return false;
    }

    public void outputData(){
        dataMap.forEach((k, v) -> {
            Payment payment = new Payment();
            payment.setCurrency(k);
            payment.setAmount(v);
            System.out.println(payment.toString());
        });
    }

    @Override
    public void init() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the initialization file name:");
        String fileName = sc.nextLine();  //读取字符串型输入
        String filePath = PaymentServiceImpl.class.getResource("/") + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            file = new File(PaymentServiceImpl.class.getResource("/") + "payment_list.txt");
            System.out.println("File exists:" + file.exists());
            System.out.println("The file name entered does not exist. The default file will be used!");
        }
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if(CheckPaymentUtil.checkIsValid(lineTxt)){
                    putData(dataMap, lineTxt);
                }else{
                    System.out.println("The file format is incorrect!");
                }
            }
            br.close();
        }catch(Exception e){
            System.out.println("Error reading file!");
        }
    }

    private void putData(Map<String, BigDecimal> dataMap, String inputStr){
        String[] inputArr = inputStr.split(" ");
        String key = inputArr[0];
        BigDecimal oldValue = dataMap.get(key);
        BigDecimal newValue = new BigDecimal(inputArr[1].trim()).add(oldValue == null ? BigDecimal.ZERO : oldValue);
        dataMap.put(key, newValue);
    }
}
