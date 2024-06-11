import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestQParam {
    Unirest unirest = new Unirest();

    private static String parameter;
    private static String cityIdOnThePage;

    public TestQParam(String parameter, String cityIdOnThePage) {
        this.parameter = parameter;
        this.cityIdOnThePage = cityIdOnThePage;
    }
//Параметры для проверки поиска, нечеткого поиска, ограничений на колличество символов
//и игнорирования регистра
    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"рс", "Параметр 'q' должен быть не менее 3 символов"},
                {"рск", "7"},
                {"рск", "26"},
                {"рск", "1"},
                {"рск", "142"},
                {"рск", "91"},
                {"орск", "26"},
                {"орск", "142"},
                {"орск", "91"},
                {"Новосибирск", "1"},
                {"НОВОСИБИРСК", "1"},
                {"новосибирск", "1"},
                {"нОВОСИБИРСК", "1"},
        };
    }

    @Test
    public void test() {
        Assert.assertTrue(String.valueOf(unirest.get(CONST.getUrl())
                .queryString("q", parameter).asJson().getBody()).contains(cityIdOnThePage));
    }

}
