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
        // The user can set the exchange rate by entering "Y"
        if("Y".equals(nextLine)){
            // Gets the set exchange rate
            Map<String, BigDecimal> rateMap = getRateMap(scanner);
            paymentService.setRateValue(rateMap);
        }
        // Reading information from a file
        // The user can input the file name. If the file does not exist, it will be replaced by the default file
        paymentService.init();

        // Start the thread to collect data
        CollectDataThread collectDataThread = new CollectDataThread();
        collectDataThread.setPaymentService(paymentService);
        FutureTask<Boolean> inspector = new FutureTask<Boolean>(collectDataThread);
        Thread collectThread = new Thread(inspector);
        collectThread.start();

        Timer timer = new Timer();
        try{
            // Set the output time to one minute after the current time
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, 1);
            // Output to the console at a frequency of once a minute
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    paymentService.outputData();
                    System.out.println("--------- Please enter the payments : --------------");
                }
            }, nowTime.getTime(), 60 * 1000);
            // If the input data is incorrect, the output is terminated
            if(!inspector.get()){
                timer.cancel();
                //collectThread.interrupt();
                System.out.println("==========THE END========");
            }
        }catch (Exception e){
            System.out.println("ERROR: Exception message : " + e.getMessage());
        }
    }

    private static Map<String, BigDecimal> getRateMap(Scanner scanner) {
        Map<String, BigDecimal> rateMap = new HashMap<>();
        System.out.println("--------- Please enter the exchange rate(enter 'F' to finish) : --------------");
        String nextLine = scanner.nextLine();
        while (nextLine != null && !nextLine.equals("") && !nextLine.equals("F")) {
            if(CheckPaymentUtil.checkIsValid(nextLine)){
                String[] inputArr = nextLine.split(" ");
                rateMap.put(inputArr[0].trim(), new BigDecimal(inputArr[1].trim()));
                nextLine = scanner.nextLine();
            }else{
                System.out.println("ERROR: The data entered is not in the correct format!");
                break;
            }
        }
        return rateMap;
    }
}
