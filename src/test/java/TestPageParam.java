import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestPageParam {
    Unirest unirest = new Unirest();
    private static String parameter;
    private static String lastCityIdOnTheFirstPage;

    public TestPageParam(String parameter, String lastCityIdOnTheFirstPage) {
        this.parameter = parameter;
        this.lastCityIdOnTheFirstPage = lastCityIdOnTheFirstPage;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"0", "null"},
                {"1", "171"},
                {"2", "17"},
        };
    }

    @Test
    public void testPageParam() {
        Assert.assertTrue(String.valueOf(unirest.get(CONST.getUrl())
                .queryString("page", parameter).asJson().getBody()).contains(lastCityIdOnTheFirstPage));
    }
}
