import core.PaymentService;
import core.impl.PaymentServiceImpl;

import java.util.Timer;
import java.util.TimerTask;

public class PaymentMain {
    public static void main(String[] args){
        PaymentService paymentService = new PaymentServiceImpl();
        //paymentService.init();
        new Thread(){
            @Override
            public void run() {
                paymentService.collectData();
            }
        }.start();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                paymentService.outputData();
            }
        }, 10000, 200000);
    }
}
