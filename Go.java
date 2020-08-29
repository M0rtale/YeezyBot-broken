/* Author: Allen Qiu
 * 
 * Automatically allocates 100 bots to wait on the yeezy website
 */

//test
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;
//import org.openqa.selenium.DesiredCapabilities;

import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.net.URL;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.InputStream;
//import java.io.BufferedReader;

//import java.util.Scanner;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.Calendar;
//import java.util.Date;
//import java.io.File;

import javax.swing.*;

public class Go
{
  static int count = 10;
  final static String site = "https://yeezysupply.com/";
  static String size = "12.5";
  static String email = "pro.juda@gmail.com";
  static String firstN = "John";
  static String lastN = "Titor";
  static String addy = "34 Trump Lane";
  static String apt = "";
  static String city = "New York";
  static String zip = "11040";
  //Note: Country and state is automatically filled in
  static String phone = "5165198769";
  static String cardNum = "4391000000000000";
  static String cardName = "John Titor";
  static String expiry = "0524"; // NO SLASH
  static String CVV = "696";
  static boolean[] success;
  
  static WebDriver[] bots;
  
  static class Destroy extends Thread {
    public void run() {
      for(WebDriver b : bots)
      {
        b.close();
        b.quit();
      }
    }
  }
  
  public static void keepAlive()
  {
    while(true)
    {
      int o = 0;
      for(boolean p : success) o += p ? 1 : 0;
      if(o == count) System.exit(0);
      
      for(int i = 0; i < count; i++)
      {
        if(success[i]) continue;
        String title = bots[i].getCurrentUrl();
        if(title.indexOf("?_ga=") != -1) // on address page
        {
          bots[i].findElements(By.xpath("//*[@id=\"checkout_email\"]")).get(0).sendKeys(email);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_first_name\"]")).get(0).sendKeys(firstN);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_last_name\"]")).get(0).sendKeys(lastN);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_address1\"]")).get(0).sendKeys(addy);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_address2\"]")).get(0).sendKeys(apt);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_city\"]")).get(0).sendKeys(city);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_zip\"]")).get(0).sendKeys(zip);
          bots[i].findElements(By.xpath("//*[@id=\"checkout_shipping_address_phone\"]")).get(0).sendKeys(phone);
          bots[i].findElements(By.id("salesFinal")).get(0).click();
          
          WebDriverWait wait = new WebDriverWait(bots[i], 10l);
          WebElement continuar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[1]/div[2]/div[2]/form/div[2]/button")));
          continuar.click();
        }
        if(title.indexOf("?previous_step=contact_information&step=shipping_method") != -1) // on shipping method page
        {
          //NOTE: the option will already be selected.
          WebDriverWait wait = new WebDriverWait(bots[i], 10l);
          WebElement continuar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[1]/div[2]/div[2]/form/div[2]/button")));
          continuar.click();
        }
        if(title.indexOf("?previous_step=shipping_method&step=payment_method") != -1) // on payment page
        {
          //calculating tax
          if(bots[i].findElements(By.xpath("//*[contains(@id,\"card-fields-number\")]")).size() == 0) continue;
          
          {
            bots[i].switchTo().frame(bots[i].findElements(By.xpath("//*[contains(@id,\"card-fields-number\")]")).get(0));
            WebElement credit_card = bots[i].findElements(By.id("number")).get(0);
            credit_card.click();
            credit_card.sendKeys(cardNum);
            bots[i].switchTo().defaultContent();
          }
          
          {
            bots[i].switchTo().frame(bots[i].findElements(By.xpath("//*[contains(@id,\"card-fields-name\")]")).get(0));
            WebElement credit_card = bots[i].findElements(By.id("name")).get(0);
            credit_card.click();
            credit_card.sendKeys(cardName);
            bots[i].switchTo().defaultContent();
          }
          
          {
            bots[i].switchTo().frame(bots[i].findElements(By.xpath("//*[contains(@id,\"card-fields-expiry\")]")).get(0));
            WebElement credit_card = bots[i].findElements(By.id("expiry")).get(0);
            credit_card.click();
            credit_card.sendKeys(expiry.substring(0,1));
            credit_card.sendKeys(expiry.substring(1,2));
            credit_card.sendKeys(expiry.substring(2,4));
            bots[i].switchTo().defaultContent();
          }
          
          {
            bots[i].switchTo().frame(bots[i].findElements(By.xpath("//*[contains(@id,\"card-fields-verification_value\")]")).get(0));
            WebElement credit_card = bots[i].findElements(By.id("verification_value")).get(0);
            credit_card.click();
            credit_card.sendKeys(CVV);
            bots[i].switchTo().defaultContent();
          }
          
          bots[i].findElements(By.xpath("//*[contains(@id,\"checkout_different_billing_address\")]")).get(0).click();
          bots[i].findElements(By.xpath("//*[contains(@id,\"checkout_different_billing_address\")]")).get(0).sendKeys(Keys.RETURN);
          
          success[i] = true;
        }
      }
    }
  }
  
  
  //Cart XPath: //*[@id="js-wrap"]/header/div[2]/a/span[1]
  //Checkout XPath: //*[@id="js-main"]/div/form/div/div[3]/ul/li[3]/input
  public static void main(String[] args)
  {
    String emergency = JOptionPane.showInputDialog(null, "Emergency Drop? (Y/N)");
    if(!emergency.equals("Y"))
    {
      count = Integer.parseInt(JOptionPane.showInputDialog(null, "How many bot"));
      size = JOptionPane.showInputDialog(null, "What size?");
      email = JOptionPane.showInputDialog(null, "What Gmail?");
      firstN = JOptionPane.showInputDialog(null, "What First name?");
      lastN = JOptionPane.showInputDialog(null, "what last name?");
      addy = JOptionPane.showInputDialog(null, "what adress street?");
      apt = JOptionPane.showInputDialog(null, "What apt? (leave blank if nothing)");
      city = JOptionPane.showInputDialog(null, "What city?");
      zip = JOptionPane.showInputDialog(null, "What zip?");
      //Note: Country and state is automatically filled in
      phone = JOptionPane.showInputDialog(null, "What phone?");
      cardNum = JOptionPane.showInputDialog(null, "What credit card number (16 digit)?");
      cardName = JOptionPane.showInputDialog(null, "What cardholder name?");
      expiry = JOptionPane.showInputDialog(null, "What expire date? (NO SLASH, Ex. 07/24 = 0724)");
    }
    CVV = JOptionPane.showInputDialog(null, "What CVV?");
    success = new boolean[count];
    for(int i = 0; i < success.length; i++) success[i] = false;
    
    
    bots = new WebDriver[count];
    Runtime.getRuntime().addShutdownHook(new Destroy());
    for(int i = 0; i < count; i++)
    {
      bots[i] = new ChromeDriver();
      bots[i].get("https://yeezysupply.com/");
      
      //choose size
      Select options = new Select(bots[i].findElements(By.tagName("select")).get(0));
      options.selectByVisibleText(size);
      
      List<WebElement> purchaseCart = bots[i].findElements(By.tagName("input"));
      for(WebElement elem : purchaseCart) // Click first purchase
      {
        try
        {
          if(elem.getAttribute("placeholder").equals("ENTER EMAIL")) continue;
          elem.click();
        }
        catch(Exception e){ continue; }
        break;
      }
      
      //click cart
      WebDriverWait wait = new WebDriverWait(bots[i], 1l);
      WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-wrap\"]/header/div[2]/a/span[1]")));
      //System.out.println(cart.getAttribute("value"));
      cart.click();
      
      //click checkout
      WebDriverWait wait2 = new WebDriverWait(bots[i], 1l);
      WebElement checkout = wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-main\"]/div/form/div/div[3]/ul/li[3]/input")));
      checkout.click();
      
      JOptionPane.showMessageDialog(null, "captcha");
      
      //YOU ARE IN LINE
      //https://yeezysupply.com
      
      while(bots[i].getTitle() != "YOU ARE IN LINE")
      {
        if(bots[i].getTitle().indexOf("https://yeezysupply.com") == -1) break;
        //click cart
        WebDriverWait wait3 = new WebDriverWait(bots[i], 3l);
        WebElement cart1 = wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-wrap\"]/header/div[2]/a/span[1]")));
        //System.out.println(cart.getAttribute("value"));
        cart1.click();
        
        //click checkout
        WebDriverWait wait4 = new WebDriverWait(bots[i], 3l);
        WebElement checkout1 = wait4.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"js-main\"]/div/form/div/div[3]/ul/li[3]/input")));
        checkout1.click();
      }
      
    }
    keepAlive();
  }
}