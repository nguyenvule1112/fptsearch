package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class fptsearch {
    WebDriver driver;
    String keyword = "iPhone 13 Pro Max";
    @Before
    public void before(){
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.get("https://fptshop.com.vn/");
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

//    @After
//    public void after() throws InterruptedException {
//        Thread.sleep(3000);
//        this.driver.quit();
//    }
    @Test
    public void suggestion_box_search() throws InterruptedException {
        //webelement
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.sendKeys(this.keyword);

        WebDriverWait waitSuggestion = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        WebElement suggestionbox = waitSuggestion.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"fs-search\"]//div[contains(@class, \"suggest-left\")]/.."))
        );
        Assert.assertTrue(suggestionbox!=null);

        //3 sản phẩm tìm nhiều
        List<WebElement> lstHitItems = this.driver.findElements(By.xpath("//a[@class=\"item-js\"]//h3[contains(@class,\"pr-item__name\")]"));
        Assert.assertEquals(lstHitItems.size(), 3);

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


    //3 bài viết tìm nhiều
    List<WebElement> lstHitnew = this.driver.findElements(By.xpath("//a[@class=\"news-js\"]"));
        Assert.assertEquals(lstHitnew.size(), 3);

        for (WebElement tl: lstHitnew) {
        String itemName = tl.getText();
        if(!itemName.contains(this.keyword))
        {
            isIncludeKeyword = false;
            break;
        }
    }
        Assert.assertTrue(isIncludeKeyword);
}


    public boolean elementExists(By locator)
    {
        return !this.driver.findElements(locator).isEmpty();
    }

   public boolean check_button() {
        boolean isExist = this.elementExists(By.cssSelector("div.c-comment-loadMore>a"));
        if (isExist) {
            WebElement btloadmore = this.driver.findElement(By.cssSelector("div.c-comment-loadMore>a"));
            return btloadmore.isEnabled();
        }
        return false;
   }

    @Test
    public void list_with_keyword() throws InterruptedException {
        //webelement
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.sendKeys(this.keyword);
        fsearch.sendKeys(Keys.ENTER);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        WebDriverWait waitSuggestion = new WebDriverWait(this.driver, Duration.ofSeconds(15));


        while (check_button()){
            boolean isExist = this.elementExists(By.cssSelector("div.c-comment-loadMore>a"));
            if (isExist) {
                WebElement btloadmore = waitSuggestion.until(
                        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.c-comment-loadMore>a")));
                btloadmore.click();
            }
//            Thread.sleep(500);
            if (!check_button()) {break;}

        };
    }
}
