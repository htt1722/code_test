import core.PaymentService;
import core.impl.PaymentServiceImpl;
import thread.CollectDataThread;
import utils.CheckPaymentUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.FutureTask;

public class PaymentMain {
    public static void main(String[] args){
        System.out.println("---------------- Do you want to set the exchange rate ?(Y/N)--------");
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        PaymentService paymentService = new PaymentServiceImpl();
        if("Y".equals(nextLine)){
            // Gets the set exchange rate
            Map<String, BigDecimal> rateMap = getRateMap(scanner);
            paymentService.setRateValue(rateMap);
        }
        //paymentService.init();
        CollectDataThread collectDataThread = new CollectDataThread();
        collectDataThread.setPaymentService(paymentService);
        FutureTask<Boolean> inspector = new FutureTask<Boolean>(collectDataThread);

        new Thread(inspector).start();
        Timer timer = new Timer();
        try{
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    paymentService.outputData();
                    System.out.println("--------- Please enter the payments : --------------");
                }
            }, 10000, 20000);
            // If the input data is incorrect, the output is terminated
            if(!inspector.get()){
                timer.cancel();
                System.out.println("==========THE END========");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Map<String, BigDecimal> getRateMap(Scanner scanner) {
        Map<String, BigDecimal> rateMap = new HashMap<>();
        System.out.println("--------- Please enter the exchange rate(enter 'quit' to stop) : --------------");
        String nextLine = scanner.nextLine();
        while (nextLine != null && !nextLine.equals("") && !nextLine.equals("quit")) {
            if(CheckPaymentUtil.checkIsValid(nextLine)){
                String[] inputArr = nextLine.split(" ");
                rateMap.put(inputArr[0].trim(), new BigDecimal(inputArr[1].trim()));
                nextLine = scanner.nextLine();
            }else{
                System.out.println("The data entered is not in the correct format!");
                break;
            }
        }
        return rateMap;
    }
}
