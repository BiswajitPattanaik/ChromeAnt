import org.openqa.selenium.TimeoutException;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import java.io.*;
import java.util.Properties;
import org.openqa.selenium.support.ui.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
class CavissonDriver
{
	String url;
	Logger log = Logger.getLogger(CavissonDriver.class.getName());
	WebDriver driver;
	WebDriverWait wait;
        CavissonDriver(WebDriver driver)throws IOException
		{
		this.driver=driver;
		Properties p=new Properties();
		p.load(new FileInputStream("/home/netstorm/Automation_Projects/java/cavisson/Python/log4j-test.properties"));
		PropertyConfigurator.configure(p);

		}
	public WebElement findElement(By by)
		{
		return driver.findElement(by);
		}
        public int sendKeys(By by,String str,String testcase)
		{
		try{
			waitForLoad(driver);
			driver.findElement(by).sendKeys(str);
			waitForLoad(driver);
			captureScreenShot(driver);
			//System.out.println("inside try");
			log.info(String.format("Keys sent to the TextBox for case : \"%s\" : Screenshot captured ",testcase));

		}
		catch(NoSuchElementException e)
		{
		 log.error(String.format("Element not found : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));return 1;
		}
		catch(ElementNotVisibleException e)
		{
		 log.error(String.format("Element not Visible : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));	return 2;
		}
		catch(NotFoundException e)
		{
		 log.error(String.format("Element not Found : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));	return 3;
		}
		catch(Exception e){
                log.error(String.format("Mescelleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 3;
		}
		return 0;
		}

		public int get(String url,String testcase)
                {
                try{
                        waitForLoad(driver);
                        driver.get(url);
                        waitForLoad(driver);
                        captureScreenShot(driver);
                        //System.out.println("inside try");
                        log.info(String.format("navigation to page \"%s\" done for : \"%s\" : Screenshot captured ",url,testcase));

                }
                catch(Exception e){
                log.error(String.format("Mescelleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 3;
                }
                return 0;
                }

	public int click(By by,String testcase)
		{
                try{
			waitForLoad(driver);
                	driver.findElement(by).click();
                        waitForLoad(driver);
			captureScreenShot(driver);
                        //System.out.println("inside try2");
			log.info(String.format("Succesfully Clicked : \"%s\" : Screenshot captured ",testcase));

                }
        catch(NoSuchElementException e)
        {
         log.error(String.format("Element not found : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));return 1;
        }
        catch(ElementNotVisibleException e)
        {
         log.error(String.format("Element not Visible : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));   return 2;
        }
        catch(NotFoundException e)
        {
         log.error(String.format("Element not Found : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 3;
        }
        catch(Exception e){
        log.error(String.format("Mescelleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 4;
	}
		return 0;
        }
                public int submit(By by,String testcase)
                {
			url=null;
                try{    //System.out.println("inside submit");
			WebElement element=driver.findElement(by);
			url=driver.getCurrentUrl();
                        waitForLoad(driver);
			System.out.println("Initial wait is over");
                        driver.findElement(by).click();
			captureScreenShot(driver);
                        wait = new WebDriverWait(driver, 10);
                        wait.until(ExpectedConditions.stalenessOf(element));
			captureScreenShot(driver);
                        //System.out.println("inside try3");
			log.info(String.format("Succesfully Submitted for case: \"%s\" : Screenshot captured ",testcase));
                }
                catch(NoSuchElementException e)
        {
         log.error(String.format("Element not found : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));return 1;
        }
        catch(ElementNotVisibleException e)
        {
         log.error(String.format("Element not Visible : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));   return 2;
        }
        catch(NotFoundException e)
        {
         log.error(String.format("Element not Found : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 3;
        }
        catch(TimeoutException te){
			try{
			   //System.out.println("inside inherited try");
			 wait = new WebDriverWait(driver, 30);
                        wait.until(new ExpectedCondition<Boolean>(){
                                public Boolean apply(WebDriver driver){
                                return (url.equals(driver.getCurrentUrl()));}
				});
				captureScreenShot(driver);
				}
                        catch(TimeoutException e){e.printStackTrace();System.out.println("inside try3333333333");}   
			     }
               catch(Exception te){
               log.error(String.format("Mesceleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,te.getMessage()));    return 4;
                 }
		 return 0;
                }

	public void captureScreenShot(WebDriver Idriver){
       		File src =(File)((TakesScreenshot)Idriver).getScreenshotAs(OutputType.FILE);
     		try {
     		 FileUtils.copyFile(src, new File("/home/netstorm/biswajit/images/Img"+System.currentTimeMillis()+".png"));
		 System.out.println("Screen shot captured");
     			//Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
                        //ImageIO.write(fpScreenshot.getImage(),"PNG",new File("/home/netstorm/biswajit/images/biswajit.png"));
			}
      		catch (IOException e)
     		{
       			System.out.println(e.getMessage());
      		}
     		}		
       private void waitForLoad(WebDriver driver) 
 		{
 	 	ExpectedCondition pageLoads = new ExpectedCondition<Boolean>(){
        	public Boolean apply(WebDriver driver){
                return (Boolean)((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
        	}
        	};
	        wait = new WebDriverWait(driver, 30);
		wait.until(pageLoads);
		}

}
