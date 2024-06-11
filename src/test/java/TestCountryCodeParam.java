import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class TestCountryCodeParam {
    Unirest unirest = new Unirest();
    private static String parameter;
    private static String unacceptableRespounse1;
    private static String unacceptableRespounse2;
    private static String unacceptableRespounse3;

    public TestCountryCodeParam(String parameter, String unacceptableRespounse1, String unacceptableRespounse2, String unacceptableRespounse3) {
        this.parameter = parameter;
        this.unacceptableRespounse1 = unacceptableRespounse1;
        this.unacceptableRespounse2 = unacceptableRespounse2;
        this.unacceptableRespounse3 = unacceptableRespounse3;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"ru", "kg", "kz", "cz"},
                {"kg", "ru", "kz", "cz"},
                {"kz", "kg", "ru", "cz"},
                {"cz", "kg", "kz", "ru"},
        };
    }

    @Test
    public void testCountryCodeParam() {
        String a = String.valueOf(unirest.get(CONST.getUrl())
                .queryString("country_code", parameter).asJson().getBody());
        Assert.assertTrue(a.contains(parameter) & !(a.contains(unacceptableRespounse1)
                        || a.contains(unacceptableRespounse2)
                        || a.contains(unacceptableRespounse3)));
    }

}
