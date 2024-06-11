import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;

public class TestQ {
    Unirest unirest = new Unirest();

    //Проверка игнорирования других параметров
    @Test
    public void testQ() {
        Assert.assertTrue(String.valueOf(unirest.get(CONST.getUrl())
                .queryString("q", "рск")
                .queryString("country_code", "ru")
                .asJson().getBody()).contains("kz"));
    }

}
