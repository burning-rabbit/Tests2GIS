import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;

public class TestPage {
    Unirest unirest = new Unirest();
    public static String lastCityIdOnTheFirstPage = "171";

    //Проверка значения по умолчанию
    @Test
    public void testPage() {
        Assert.assertTrue(String.valueOf(unirest.get(CONST.getUrl())
                .asJson().getBody()).contains(lastCityIdOnTheFirstPage));
    }
}
