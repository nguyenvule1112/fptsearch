package CN;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FPTShop {
    WebDriver driver;
    String keyword = "iPhone 13";
    @Before
    public void Setup()
    {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.get("https://fptshop.com.vn/");
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @After
    public void TearDown() throws InterruptedException {
        Thread.sleep(2000);
        this.driver.quit();
    }

    @Test
    public void suggestion_box_should_show_after_keyword_inputted()
    {
        WebElement tbSearch = this.driver.findElement(By.xpath("//input[@id=\"key\"]"));
        tbSearch.sendKeys(this.keyword);

        WebDriverWait waitSuggestion = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        WebElement pnlSuggestionBox = waitSuggestion.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"fs-search\"]//div[contains(@class, \"suggest-left\")]/.."))
        );
        Assert.assertTrue(pnlSuggestionBox!=null);
    }

    @Test
    public void top_3_hit_items_should_show_on_suggestion_box()
    {
        WebElement tbSearch = this.driver.findElement(By.xpath("//input[@id=\"key\"]"));
        tbSearch.sendKeys(this.keyword);

        WebDriverWait waitSuggestion = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        WebElement pnlSuggestionBox = waitSuggestion.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"fs-search\"]//div[contains(@class, \"suggest-left\")]/.."))
        );

        //Lấy 3 hits items
        List<WebElement> lstHitItems = this.driver.findElements(By.xpath("//li[@class=\"ais-Hits-item\"]//h3[contains(@class,\"pr-item__name\")]"));
        /*
        //Kiểm tra hititems có phải 3 items
        Assert.assertEquals(lstHitItems.size(), 3);

        //Kiểm tra xem 3 tên có chứa từ khoá hay không?
        boolean isIncludeKeyword = true;
        for (WebElement tl: lstHitItems) {
            String itemName = tl.getText();
            if(!itemName.contains(this.keyword))
            {
                isIncludeKeyword = false;
                break;
            }
        }

        Assert.assertTrue(isIncludeKeyword);
         */

        List<WebElement> lstFinterItems = lstHitItems.stream().filter((item)->{
            //Có chứa từ khoá
            //return item.getText().contains(this.keyword);

            //Có chứa từ khoá, và từ khoá phải bold
            List<WebElement> lstEmTag = item.findElements(By.xpath("//em"));
            String nameInEm = "";
            for (WebElement emTag: lstEmTag) {
                nameInEm += emTag.getText() + " ";
            }
            return nameInEm.contains(this.keyword);
        }).collect(Collectors.toList());

        Assert.assertEquals(lstFinterItems.size(), 3);

    }

    @Test
    public void pager_should_have_8items_per_page() throws InterruptedException {
        WebElement tbSearch = this.driver.findElement(By.xpath("//input[@id=\"key\"]"));
        tbSearch.sendKeys(this.keyword);
        tbSearch.sendKeys(Keys.ENTER);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        WebElement lbTotal = this.driver.findElement(By.xpath("(//h1[contains(@class, \"fs-search-result\")]/span)[1]"));
        System.out.println(lbTotal.getText());
        int totalItems = Integer.parseInt(lbTotal.getText()); //Get total and convert to int type.
        int totalPage = (int)Math.ceil((double) totalItems/(double) 8);

        WebDriverWait waitLoadMore = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        for (int i = 0; i < totalPage-1; i++) {
            WebElement btnLoadMore = waitLoadMore.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"c-comment-loadMore\"]/a"))
            );
            btnLoadMore.click();
        }

        Thread.sleep(2000);
        //Kiem tra tong so item hien thi ket qua co = 51 hay khong?
        List<WebElement> lstProduct = this.driver.findElements(By.xpath("//div[contains(@class, \"row-flex\")]/div[contains(@class, \"cdt-product\")]"));

        Assert.assertEquals(totalItems, lstProduct.size());
    }

    @Test
    public void Buy_Product() throws InterruptedException {
        WebElement tbSearch = this.driver.findElement(By.xpath("//input[@id=\"key\"]"));
        tbSearch.sendKeys(this.keyword);
        tbSearch.sendKeys(Keys.ENTER);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        Thread.sleep(2000);
        //Kiem tra tong so item hien thi ket qua co = 51 hay khong?
        List<WebElement> lstProduct = this.driver.findElements(By.xpath("//div[contains(@class, \"row-flex\")]/div[contains(@class, \"cdt-product\")]"));

        Random rdn = new Random();
        int buyIndex = rdn.nextInt(lstProduct.size());

        Actions mouseAction = new Actions(this.driver);
        mouseAction.moveToElement(lstProduct.get(buyIndex)).perform();

        WebElement buyButton = lstProduct.get(buyIndex).findElement(By.xpath("//a[text()=\"MUA NGAY\"]"));

        mouseAction.moveToElement(buyButton).click().perform();
    }
}
