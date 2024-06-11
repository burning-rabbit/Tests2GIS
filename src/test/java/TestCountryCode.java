import kong.unirest.core.Unirest;
import org.example.CONST;
import org.junit.Assert;
import org.junit.Test;

public class TestCountryCode {
    Unirest unirest = new Unirest();

    //Проверка кода страны по умолчанию
    @Test
    public void testCountryCode() {
        String a = String.valueOf(unirest.get(CONST.getUrl()).asJson().getBody());
        Assert.assertTrue(a.contains("ru") || a.contains("kg")
                || a.contains("kz") || a.contains("cz"));
    }
}
