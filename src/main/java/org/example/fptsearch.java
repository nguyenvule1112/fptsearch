package org.example;

import com.sun.org.apache.bcel.internal.generic.RETURN;
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

import java.time.Duration;
import java.util.List;

public class fptsearch {
    WebDriver driver;
    @Before
    public void before(){
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.get("https://fptshop.com.vn/");
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

        @After
    public void after() throws InterruptedException {
        Thread.sleep(3000);
        this.driver.quit();
    }
    @Test
    public void suggestion_box_search() throws InterruptedException {
        //webelement
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.click();
        fsearch.sendKeys("Iphone 13 Pro Max");
        Thread.sleep(2000);
        boolean suggestionbox = this.driver.findElement(By.xpath("//div[@class=\"suggest-left\"]/../div")).isDisplayed();
        Assert.assertTrue(suggestionbox);

        WebElement hit_items_1 = this.driver.findElement(By.xpath("(//a[@class=\"item-js\"]//h3)[1]"));
        WebElement hit_items_2 = this.driver.findElement(By.xpath("(//a[@class=\"item-js\"]//h3)[2]"));
        WebElement hit_items_3 = this.driver.findElement(By.xpath("(//a[@class=\"item-js\"]//h3)[3]"));

        WebElement hot_new_1 = this.driver.findElement(By.xpath("(//a[@class=\"news-js\"])[1]"));
        WebElement hot_new_2 = this.driver.findElement(By.xpath("(//a[@class=\"news-js\"])[2]"));
        WebElement hot_new_3 = this.driver.findElement(By.xpath("(//a[@class=\"news-js\"])[3]"));

        //get data
        boolean hit_item_1_contain = hit_items_1.getText().contains("iPhone 13 Pro Max");
        boolean hit_item_2_contain = hit_items_2.getText().contains("iPhone 13 Pro Max");
        boolean hit_item_3_contain = hit_items_3.getText().contains("iPhone 13 Pro Max");

        boolean hot_new_1_contain = hot_new_1.getText().contains("iPhone 13 Pro Max");
        boolean hot_new_2_contain = hot_new_2.getText().contains("iPhone 13 Pro Max");
        boolean hot_new_3_contain = hot_new_3.getText().contains("iPhone 13 Pro Max");

        //Assert
        Assert.assertTrue(hit_item_1_contain && hit_item_2_contain && hit_item_3_contain);
        Assert.assertTrue(hot_new_1_contain && hot_new_2_contain && hot_new_3_contain);

    }
   @Test
    public void keyword_find(){
       WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
       fsearch.click();
       fsearch.sendKeys("Iphone 13 Pro Max");
       fsearch.sendKeys(Keys.ENTER);
   }
}
