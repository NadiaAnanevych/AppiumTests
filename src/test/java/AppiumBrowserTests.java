import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppiumBrowserTests {
    //start: appium --allow-insecure chromedriver_autodownload
    private static final String SERVER = "http://127.0.0.1:4723/";
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    private static final String webForm_url = "web-form.html";
    private AndroidDriver driver;


    @BeforeEach
    void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options
                .setPlatformName("Android")
                .setPlatformVersion("14")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .noReset()
                .withBrowserName("Chrome");

        driver = new AndroidDriver(new URL(SERVER), options);
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    void openWebFormTest(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement webFormLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href = 'web-form.html']")));
        //new Actions(driver).moveToElement(webFormLink).perform();
        webFormLink.click();
        String currentUrl = driver.getCurrentUrl();
        WebElement title = driver.findElement(By.className("display-6"));  //actual result

        assertEquals(BASE_URL + webForm_url, currentUrl);
        assertEquals("Web form", title.getText());
    }

    @Test
    void textInputTest() throws InterruptedException {
        driver.get(BASE_URL + webForm_url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(By.id("my-text-id")));
        WebElement passwordField = driver.findElement(By.name("my-password"));
        inputField.click();
        inputField.sendKeys("test");
        Thread.sleep(3000);
        passwordField.click();
        passwordField.sendKeys("pass");
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String inputValue = (String) js.executeScript("return document.getElementById('my-text-id').value;");
        String passwordValue = (String) js.executeScript("return document.getElementsByName('my-password')[0].value;");

        System.out.println("Input value via JS: " + inputValue);
        System.out.println("Password value via JS: " + passwordValue);

        assertEquals("test", inputValue);
        assertEquals("pass", passwordValue);
    }

    @Test
    void checkboxTest() {
        driver.get(BASE_URL +  webForm_url);
        WebElement checkedCheckbox = driver.findElement(AppiumBy.xpath("//input[@checked and @type = \"checkbox\"]"));
        //assertTrue(checkedCheckbox.isSelected());
        boolean isSelected = checkedCheckbox.isSelected();
        if (!isSelected) {
            checkedCheckbox.click();
        }
        assertTrue(isSelected);
    }

    @Test
    void submitButtonTest() {
        driver.get(BASE_URL +  webForm_url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(By.name("my-textarea")));
        textField.sendKeys("mytext");
        WebElement submitButton = driver.findElement(By.tagName("button"));
        new Actions(driver).moveToElement(submitButton).perform();
        submitButton.click();
        WebElement title = driver.findElement(By.className("display-6"));
        assertEquals("Form submitted", title.getText());
    }
}
