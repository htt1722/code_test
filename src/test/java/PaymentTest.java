import core.PaymentService;
import core.impl.PaymentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import utils.CheckPaymentUtil;

public class PaymentTest {
    @Test
    public void test(){
        // test methods of tool class
        Assert.assertTrue(CheckPaymentUtil.testAllUpperCase("ABC"));
        Assert.assertFalse(CheckPaymentUtil.testAllUpperCase("AbC"));
        Assert.assertFalse(CheckPaymentUtil.isNumeric("a"));
        Assert.assertTrue(CheckPaymentUtil.isNumeric("-12.2"));
        Assert.assertTrue(CheckPaymentUtil.checkIsValid("USD 12 "));
        Assert.assertFalse(CheckPaymentUtil.checkIsValid("jjj 12 "));
        Assert.assertFalse(CheckPaymentUtil.checkIsValid("HSK 12 USD"));

    }
}
