import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestPageSizeParam {
    Unirest unirest = new Unirest();
    private static String parameter;
    private static String lastCityIdOnThePage;

    public TestPageSizeParam(String parameter, String lastCityIdOnThePage) {
        this.parameter = parameter;
        this.lastCityIdOnThePage = lastCityIdOnThePage;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"5", "114"},
                {"10", "32"},
                {"15", "171"},
                {"4", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
                {"6", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
                {"9", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
                {"11", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
                {"14", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
                {"16", "Параметр 'page_size' может быть одним из следующих значений: 5, 10, 15"},
        };
    }

    @Test
    public void test() {
        Assert.assertTrue(String.valueOf(unirest.get(CONST.getUrl())
                .queryString("page_size", parameter).asJson().getBody()).contains(lastCityIdOnThePage));
    }
}

