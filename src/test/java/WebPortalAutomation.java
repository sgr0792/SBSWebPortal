import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import static java.lang.Integer.parseInt;

public class WebPortalAutomation {
    WebDriver driver;
    public void launchBrowser() {
        try{
            String driverPath = System.getProperty("user.dir");
            System.setProperty("webdriver.chrome.driver",
                    driverPath + "/src/test/resources/driver/chromedriver");
            driver = new ChromeDriver();
            driver.get("https://www.sbs.com.au/language/mandarin/zh-hans/audio/cultural-\n" +
                    "conflict-meet-someone-causing-you-trouble");
            driver.manage().window().fullscreen();
        } catch (Exception e){
            System.out.println("Browser cannot start and launch the home page due to : "+e);
        }

    }
    public void verifyElemntsUntilplayer() throws InterruptedException {
        try {
//            Verify the title of the page
            String titleValue = driver.findElement(By.xpath("//h1[@id='podcast-episode-headline']")).getText();
            System.out.println("The home page's title is : " + titleValue);
            Assert.assertEquals(titleValue, "【文化苦丁茶】人生的林子里，你有大概率会遇到“鸟人”", "The title value didn't match" );

//            Verify the apple prodcast and Google prodcast is present on the page and click on play button to start the audio
            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href, 'tunes.apple.com/au/podcast') and @tabindex='0']")).isDisplayed(), "Apple prod cast is not present on the web page");
            Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href, 'google.com/podcasts') and @tabindex='0']")).isDisplayed(), "Apple prod cast is not present on the web page");
            driver.findElement(By.xpath("//button[@data-testid='audio-button' and @aria-label='Play']")).click();

//            Wait for the player to appear
            if (driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div[1]/div[2]")).isDisplayed() == false) {
                driver.manage().wait(3000);
            } else {
                Assert.assertTrue(driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div[1]/div[2]")).isDisplayed(), "Player is not visible yet");
                Assert.assertTrue(driver.findElement(By.xpath("//button[@aria-label='Pause']")).isDisplayed(), "Pause button is not visible yet");
                try {
//                    Close the chatbot dialog
                    System.out.println("Dialog box present ? : " + driver.findElement(By.xpath("//div[@role='dialog']")).isDisplayed());
                    if (driver.findElement(By.xpath("//div[@role='dialog']")).isDisplayed()){
                        driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
                    }
                } catch (Exception e) {
                    System.out.println("Dialog is not present yet and exception is : " + e);
                }
            }
        } catch (Exception e){
            System.out.println("Exception message is : " +e);
            driver.close();
        }
    }
    public void verifyPlayerelements() throws InterruptedException {
        try{
//            Click on Pause button
            driver.findElement(By.xpath("//button[@aria-label='Pause']")).click();
            Assert.assertTrue(driver.findElement(By.xpath("//button[@aria-label='Play']")).isDisplayed(), "Play button is not visible yet");
            Assert.assertTrue(driver.findElement(By.xpath("//button[@aria-label='volume button']")).isDisplayed(), "Volume button button is not visible yet");
//      Get the play time
            String secondsInitial = driver.findElement(By.cssSelector("p[class = 'MuiTypography-root MuiTypography-body2 css-1lqkmh2']")).getText().split(":")[1];
            System.out.println("Seconds before forwarding : " + secondsInitial);
            driver.findElement(By.xpath("//button[@aria-label='Forward 30 seconds']")).click();
            Thread.sleep(3000);
            String secondsAfter = driver.findElement(By.cssSelector("p[class = 'MuiTypography-root MuiTypography-body2 css-1lqkmh2']")).getText().split(":")[1];
            System.out.println("The seconds value after is forwarding 30 seconds : " + secondsAfter);
            int difference = parseInt(secondsAfter) - parseInt(secondsInitial);
            Assert.assertTrue((parseInt(secondsAfter) - parseInt(secondsInitial)) == 30, "The difference value is != than 30 seconds");
            driver.findElement(By.xpath("//button[contains(@class, 'textSizeMedium css-1o99ns6')]")).click();
            Thread.sleep(3000);
            System.out.println("The difference value after 30 sec forward: " + difference);
//       Scroll the webpage to identify the different language weblist
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,150)", driver.findElement(By.xpath("//div[@class = 'MuiBox-root css-y4be7z']")));
            Thread.sleep(3000);
            driver.findElement(By.xpath("//div[@class = 'MuiBox-root css-y4be7z']")).click();
            System.out.println("The languages are present and one of the value is : " + driver.findElement(By.xpath("//div[@class = 'css-qfia1s MuiPopperUnstyled-root']/div/a")).getText());
            System.out.println("All elements are present");
        } catch (Exception e){
            System.out.println(" Exception is : "+e);
            driver.close();
        }
        driver.close();
    }
    @Test
    public void runAllFunctions() throws InterruptedException {
        launchBrowser();
        verifyElemntsUntilplayer();
        verifyPlayerelements();
    }
}
