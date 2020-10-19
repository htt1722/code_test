package core;

import core.impl.PaymentServiceImpl;
import utils.CheckPaymentUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public interface PaymentService {
    void init();
    boolean collectData();
    void outputData();
    void setRateValue(Map<String, BigDecimal> rateMap);
}
