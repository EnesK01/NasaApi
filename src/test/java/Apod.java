import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class Apod {

    @Test
    public void ApodTry() {
        //Günün uzay fotoğrafı response unu döndüren endpoint e isteği atıp ve dönen resmin url ini alıp chrome da açarak ekran görüntüsü alır.
        String url = "https://api.nasa.gov/planetary/apod";


        String param_Date = "2023-10-22";
        String param_apikey = "6ohpfGLkdhxScRk67WMOOGX5wGJJQd8gPKiiKkHH";
        Response response = given().when()
                .param("api_key", param_apikey)
                // .param("date",param_Date)
                .get(url);
        response.then().assertThat().statusCode(200)
                .body("media_type",Matchers.is("image"))
                .body("url",Matchers.notNullValue());
        response.prettyPrint();
        JsonPath responversionsecond=response.jsonPath();
        String imagelink=responversionsecond.get("hdurl");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();
        Playwright playwright=Playwright.create();
        Browser browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page= browser.newPage();
        page.navigate(imagelink);
        page.setViewportSize(width, height);
        page.waitForLoadState();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("space_photo.jpeg")));
        page.close();
        browser.close();
        playwright.close();
    }}
