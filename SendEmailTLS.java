import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

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

public class SendEmailTLS {
  
  static WebDriver site;
  
  public static void send()
    {
      
      final String username = "dick@cock.li";
      final String password = "123123123";
      
      Properties prop = new Properties();
      prop.put("mail.smtp.host", "mail.cock.li");
      prop.put("mail.smtp.port", "587");
      prop.put("mail.smtp.auth", "true");
      prop.put("mail.smtp.starttls.enable", "true"); //TLS
      
      //System.out.println("Done");
      
      Session session = Session.getInstance(prop,
                                            new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });
      
      try {
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("dick@cock.li"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("billgates@gmail.com"));
        message.setSubject("Yeezy");
        message.setText("Dear Allen,"
                          + "\n\n Something has happened on the site, please go check it out!");
        
        Transport.send(message);
        
        //System.out.println("Done");
        
      } catch (MessagingException e) {
        e.printStackTrace();
      }
      System.exit(0);
    }
  
  public static class delay implements ActionListener
  {
    
    
    public void actionPerformed(ActionEvent ee)
    {
      site.get("https://yeezysupply.com/");
      
      System.out.println("Done");
      
      List<WebElement> texts = site.findElements(By.tagName("div"));
      
      for(WebElement t : texts)
      {
       // System.out.println(t.getText());
        if(t.getText().indexOf("EFLECTIVE") != -1) send();
      }
      
      
      if(site.getCurrentUrl().indexOf("password") == -1)
      {
        send();
      }
    }
  }
    
  
  public static void main(String[] args) {
    site = new ChromeDriver();
    site.get("https://yeezysupply.com/");
    
    Timer times = new Timer(10000, new delay());
    times.start();
    
  }
  
}