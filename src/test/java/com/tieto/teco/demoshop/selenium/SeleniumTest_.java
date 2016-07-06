package com.tieto.teco.demoshop.selenium;

import junit.framework.TestCase;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class SeleniumTest_ extends TestCase {
    private WebDriver driver;
    private String Sauce_username;
    private String Sauce_access_key;
    private String Sauce_host = "ondemand.saucelabs.com";
    private String Sauce_port = "80";
    private String appUrl;
    private String browser;
    private String appUserPass;
    
    public SeleniumTest_(String browser) {
    	this.browser = browser;
    }
    
    @Parameterized.Parameters
    public static Collection data() {
    	return Arrays.asList(new Object[][]{{"chrome"}});
    }

    @Before
    public void setUp() throws Exception {
    	Sauce_username = System.getenv("SAUCE_USERNAME");
    	Sauce_access_key = System.getenv("SAUCE_ACCESS_KEY");
    	appUrl = System.getenv("APP_URL");
    	appUserPass = System.getenv("APP_USER_PASSWORD");
    	DesiredCapabilities capabilities = null;
    	switch(browser) {
    	case "chrome":
    		capabilities = DesiredCapabilities.chrome();
    		capabilities.setCapability("version", "latest");
    		capabilities.setCapability("platform", Platform.WIN10);
    		capabilities.setCapability("name", "Chrome latest in win10");
    		break;
    	case "ie":
    		capabilities = DesiredCapabilities.internetExplorer();
    		capabilities.setCapability("version", "latest");
    		capabilities.setCapability("platform", Platform.WIN8_1);
    		capabilities.setCapability("name", "IE latest in win8.1");
    		break;
    	case "edge":
    		capabilities = DesiredCapabilities.edge();
    		capabilities.setCapability("version", "latest");
    		capabilities.setCapability("platform", Platform.WIN10);
    		capabilities.setCapability("name", "Edge latest in win10");
    		break;
    	case "firefox":
    		capabilities = DesiredCapabilities.firefox();
    		capabilities.setCapability("version", "latest");
    		capabilities.setCapability("platform", Platform.WIN10);
    		capabilities.setCapability("name", "Firefox latest");
    		break;
    	case "safari":
    		capabilities = DesiredCapabilities.safari();
    		capabilities.setCapability("version", "latest");
    		capabilities.setCapability("platform", Platform.YOSEMITE);
    		capabilities.setCapability("name", "Safari latest with OSX yosemite");
    		break;
    	default:
    		break;	
    	}
       
        this.driver = new RemoteWebDriver(
           new URL("http://"+Sauce_username+":"+Sauce_access_key+"@"+Sauce_host+":"+Sauce_port+"/wd/hub"),
           capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testSimple() throws Exception {
        this.driver.get(appUrl);
        assertEquals("Welcome to Demo Shop", this.driver.findElement(By.tagName("h1")).getText());
    }
    
    @Test
    public void testLogin() throws Exception {
    	WebElement element = null;
    	this.driver.get(appUrl);
    	this.driver.findElement(By.id("menu_login")).click();
    	this.driver.findElement(By.id("email")).sendKeys("testi@testi.org");
    	this.driver.findElement(By.id("password")).sendKeys(appUserPass);
    	this.driver.findElement(By.id("login_button")).click();
    	try {
    		element = this.driver.findElement(By.id("menu_logout"));
    	} catch (Exception e) {
    		
    	}
    	Assert.assertNotNull(element);
    	Assert.assertEquals(element.getText(), "Logout");
    	//System.out.println("Elementin teksti: "+element.getText());
    }
    
	@Test
	public void testLoginFailure() throws Exception {
		WebElement element = null;
		this.driver.get(appUrl);
		this.driver.findElement(
				By.id("menu_login")).click();
		this.driver.findElement(By.id("email")).sendKeys("testi@testi.org");
		this.driver.findElement(By.id("password")).sendKeys("joku");
		this.driver.findElement(By.id("login_button"))
				.click();
		try {
			element = this.driver.findElement(By
					.id("error_text"));
		} catch (Exception e) {

		}
		Assert.assertNotNull(element);
		Assert.assertEquals(element.getText(), "The email or password you have entered is invalid, try again.");
	}
	
	@Test
	public void testProductsPage() throws Exception {
		WebElement element = null;
		this.driver.get(appUrl);
		this.driver.findElement(By.id("menu_products")).click();
		
		try {
			element = this.driver.findElement(By.xpath("/html/body/div/div/table/tbody/tr[2]/td[3]"));
		} catch (Exception e) {
			
		}
		Assert.assertNotNull(element);
		Assert.assertEquals(element.getText(), "Product 1");
	}

    @After
    public void tearDown() throws Exception {
        this.driver.quit();
    }
}