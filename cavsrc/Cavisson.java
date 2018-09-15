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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/*import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;*/
import java.util.concurrent.TimeUnit;
class CavissonDriver
{
	String url;
    private String imageDir ;
	Logger log = Logger.getLogger(CavissonDriver.class.getName());
	WebDriver driver;
	WebDriverWait wait;
        CavissonDriver(WebDriver driver,String etcDir,String imgDir)throws IOException
		{
		this.driver=driver;
		Properties p=new Properties();
        String proprtiesFile = etcDir+"/log4j-test.properties";
        this.imageDir=imgDir;
        if (new File(etcDir+"/log4j-test.properties").exists()){
	       p.load(new FileInputStream(proprtiesFile));
		   PropertyConfigurator.configure(p);
        }
        else{
            log.warn("properties file not found hence continueing with out logging");
        }
		}
	public WebElement findElement(By by)
		{
		return driver.findElement(by);
		}
        public int sendKeys(By by,String str,String testcase)
		{
		try{
			waitForLoad(driver);
                        WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(str);
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
        public int select(By by,String str,String testcase)
		{
		try{
			waitForLoad(driver);
			new Select(driver.findElement(by)).selectByVisibleText(str);
			waitForLoad(driver);
			captureScreenShot(driver);
			//System.out.println("inside try");
			log.info(String.format("Element selected for case : \"%s\" : Screenshot captured ",testcase));

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
		public boolean assertByValue(By by,String str,String testcase)
		{
		boolean assertResult = false;
		try{
			waitForLoad(driver);
			assertResult = driver.findElement(by).getText().contains(str);
                        System.out.println("Inside assert "+driver.findElement(by).getText());
			waitForLoad(driver);
			captureScreenShot(driver);
			//System.out.println("inside try");
			log.info(String.format("Assertion done for case : \"%s\" : Screenshot captured  Assert result = \"%s\"",testcase,String.valueOf(assertResult)));
			 
		}
		catch(NoSuchElementException e)
		{
		 log.error(String.format("Element not found : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));return false;
		}
		catch(ElementNotVisibleException e)
		{
		 log.error(String.format("Element not Visible : \"%s\" : [Exception] :\"%s\" ",testcase,e.getMessage()));	return false;
		}
		catch(NotFoundException e)
		{
		 log.error(String.format("Element not Found : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));	return false;
		}
		catch(Exception e){
                log.error(String.format("Mescelleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));e.printStackTrace();    return false;
		}
		return assertResult;
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
			waitForLoad(driver,by);
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
        catch(UnhandledAlertException te){
            driver.switchTo().alert().accept();
            waitForLoad(driver);
        }
        catch(Exception e){
        log.error(String.format("Mescelleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,e.getMessage()));    return 4;
	}
		return 0;
        }
        public int submit(By by,String testcase)
        {
			url=null;
            WebElement element =  null;
            try{    //System.out.println("inside submit");
    			element=driver.findElement(by);
    			url=driver.getCurrentUrl();
    			System.out.println("Initial wait started");
                	waitForLoad(driver);
    			System.out.println("Initial wait is over");
                	element.click();
    			System.out.println("going to capture screenshot");
    			captureScreenShot(driver);
    			System.out.println("Completed capture screenshot");
    			System.out.println("Secondary wait started");
                	wait = new WebDriverWait(driver, 60);
                	wait.until(ExpectedConditions.stalenessOf(element));
    			System.out.println("Secondary wait Completed");
    			captureScreenShot(driver);
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
            catch(UnhandledAlertException te){
                driver.switchTo().alert().accept();
                wait.until(ExpectedConditions.stalenessOf(element));
                captureScreenShot(driver);
                log.info(String.format("Succesfully Submitted for case: \"%s\" : Screenshot captured ",testcase));
            }
            catch(TimeoutException te){
    			try{
                    wait = new WebDriverWait(driver, 30);
		    System.out.println("inside last wait");
                    wait.until(new ExpectedCondition<Boolean>(){
                        public Boolean apply(WebDriver driver){
                        return (url.equals(driver.getCurrentUrl()));}
    				});
				System.out.println("inside last wait done");
				waitForLoad(driver);
				System.out.println("going to capture screenshot");
    				captureScreenShot(driver);
				System.out.println("going to capture screenshot done");
    				}
                            catch(Exception e){e.printStackTrace();System.out.println("inside try3333333333");}   
    			}
                catch(Exception te){
                    te.printStackTrace();
                    log.error(String.format("Mesceleneous Exception : \"%s\" : [Exception] : \"%s\" ",testcase,te.getMessage()));    return 4;
                }
    		return 0;
        }

	public void captureScreenShot(WebDriver Idriver){
            File src = null;
            try{
		 try{
                 	switchWindow(driver);
		 }catch(Exception e){}
       		 src =(File)((TakesScreenshot)Idriver).getScreenshotAs(OutputType.FILE);
            }catch(UnhandledAlertException ae){
                driver.switchTo().alert().accept();
            }
	    catch(Exception e){
		
		log.error(String.format("Mescelleneous Exception  : [Exception] : \"%s\" ",e.getMessage()));	
	    }
     		try {
				
			if(src!=null){
     		 	FileUtils.copyFile(src, new File(imageDir+"/Img"+System.currentTimeMillis()+".png"));}
		     }
      		catch (IOException e)
     		{
       			System.out.println(e.getMessage());
      		}
     	}
       public void switchWindow(WebDriver driver) throws IOException
	{
        	String parentWindow = driver.getWindowHandle();
        	new WebDriverWait(driver,1).until(ExpectedConditions.numberOfWindowsToBe(2));
    		for (String winHandle : driver.getWindowHandles()) 
        	{
    			if (!parentWindow.equalsIgnoreCase(winHandle))
        		driver.switchTo().window(winHandle);
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
       private void waitForLoad(WebDriver driver,By by) 
 		{
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
 	 	ExpectedCondition pageLoads = new ExpectedCondition<Boolean>(){
        	public Boolean apply(WebDriver driver){
                return (Boolean)((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
        	}
        	};
	        wait = new WebDriverWait(driver, 30);
		wait.until(pageLoads);
		wait.until(ExpectedConditions.elementToBeClickable(by));
		}
        public void waitFor(int interval){
       		try{
			Thread.sleep(interval*1000);
		}catch(Exception e){System.out.println("Thread is interrupted");}
       }
       public void quit(){
            try{
                driver.quit();
            }catch(Exception e){
                log.error(String.format("Driver Quit Exception : [Exception] : \"%s\" ",e.getMessage()));
            }    
        }

}
