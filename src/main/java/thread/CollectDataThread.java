package thread;

import core.PaymentService;

import java.util.concurrent.Callable;

public class CollectDataThread implements Callable<Boolean> {
    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private PaymentService paymentService;

    @Override
    public Boolean call() throws Exception {
        return paymentService.collectData();
    }
}
