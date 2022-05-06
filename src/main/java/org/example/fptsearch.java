package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

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

//    @After
//    public void after() throws InterruptedException {
//        Thread.sleep(3000);
//        this.driver.quit();
//    }
    @Test
    public void suggestion_box_search() throws InterruptedException {
        //webelement
        WebElement fsearch = this.driver.findElement(By.cssSelector("input[class=\"fs-stxt\"]"));
        fsearch.sendKeys("Iphone 13 Pro Max");
        Thread.sleep(2000);
        WebElement suggestionbox = this.driver.findElement(By.xpath("(//div[@class=\"fs-search\"]/form/div)[4]"));
        String suggestion = driver.getPageSource();
        System.out.println(suggestion);

    }
}
