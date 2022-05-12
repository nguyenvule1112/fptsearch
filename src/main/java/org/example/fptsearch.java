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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class fptsearch {
    WebDriver driver;
    String keyword = "iPhone 12 Pro Max";
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

    @Test
    public void list_with_keyword_and_8_product_per_page() throws InterruptedException {
        //webelement
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.sendKeys(this.keyword);
        fsearch.sendKeys(Keys.ENTER);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        WebElement lbTotal = this.driver.findElement(By.xpath("(//h1[contains(@class, \"fs-search-result\")]/span)[1]"));
        int totalItems = Integer.parseInt(lbTotal.getText());
        int totalPage = (int)Math.ceil((double) totalItems/(double) 8);

        WebDriverWait waitLoadMore = new WebDriverWait(this.driver, Duration.ofSeconds(15));
        for (int i = 0; i < totalPage-1; i++) {
            WebElement btnLoadMore = waitLoadMore.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.c-comment-loadMore>a"))
            );
            btnLoadMore.click();
        }

        Thread.sleep(2000);
        //Check danh sách bao gồm tên sản phẩm
        List<WebElement> nameprd = this.driver.findElements(By.cssSelector("div.cdt-product__info>h3>a"));
        Assert.assertEquals(nameprd.size(), totalItems);

        boolean isIncludeKeyword = true;
        for (WebElement tl: nameprd) {
            String itemName = tl.getAttribute("title");
            if(!itemName.contains(this.keyword))
            {
                isIncludeKeyword = false;
                break;
            }
        }
        Assert.assertTrue(isIncludeKeyword);

        //Kiem tra tong so item hien thi ket qua co = 51 hay khong?
//        List<WebElement> lstProduct = this.driver.findElements(By.xpath("//div[contains(@class, \"row-flex\")]/div[contains(@class, \"cdt-product\")]"));
//
//        Assert.assertEquals(totalItems, lstProduct.size());
    }

    @Test
    public void buy(){
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.sendKeys(this.keyword);
        fsearch.sendKeys(Keys.ENTER);
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        List<WebElement> lstproduct = this.driver.findElements(By.cssSelector("div.cdt-product"));
        Random rdn = new Random();
        int buyIndex = rdn.nextInt(lstproduct.size());

        Actions mouseaction = new Actions(this.driver);
        mouseaction.moveToElement(lstproduct.get(buyIndex)).perform();

        WebElement buynow = this.driver.findElement(By.xpath("//a[text()=\"MUA NGAY\"]"));
        mouseaction.moveToElement(buynow).click().perform();
    }
}
