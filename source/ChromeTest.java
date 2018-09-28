import org.apache.tools.ant.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.*;
import java.util.*;
public class ChromeTest extends Task{
	private String chromeExecutablePath = "";
	private String chromeBinaryPath = "";
	private boolean headlessFlag = false;
	private String baseDir;
	private String etcDir;
	private String imageDir;
	private String logDir;
	private CavissonDriver driver = null;
	private Context actions = new Context();
	private ArrayList<String> actionsList = new ArrayList<String>();
	private int clickItem = 0;
        private int getItem = 0;
        private int submitItem = 0;
        private int sendKeyItem = 0;
        private int waitItem = 0;
        private int assertItem = 0;
        private int assertScenarioItem = 0;
        private int selectItem = 0;
	private void setUp(){
		baseDir = getProject().getBaseDir().getAbsolutePath();
        etcDir = baseDir+"/etc";
        imageDir = baseDir+"/image";
        logDir = baseDir+"/log";
	}
	private void verifyTaskConfiguration(){
		if (chromeExecutablePath.isEmpty()){
			throw new BuildException("\"chromeExecutablePath\" is mandatory or can not be left blank");
		}
		if (chromeBinaryPath.isEmpty()){
			throw new BuildException("\"chromeBinaryPath\" is mandatory or can not be left blank");
		}
	}
	private CavissonDriver instantiateDriver(){
		System.setProperty("webdriver.chrome.driver", chromeExecutablePath);
                System.setProperty("webdriver.chrome.logfile", baseDir+"/log/chrome_debug.log");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability("acceptInsecureCerts", true);
	        ChromeOptions options = new ChromeOptions();
		options.setBinary(chromeBinaryPath);
        	if(headlessFlag){
        		options.addArguments("--headless");
        		options.addArguments("--disable-gpu");
        		options.addArguments("--disable-logging");
        		System.out.println("this is not executed for headless mode ");
        	}
        	options.addArguments("window-size=1368x720");
        	options.addArguments("--start-maximized");
        	options.addArguments("--disable-notifications");
                options.addArguments("disable-geolocation");
		options.addArguments("ignore-certificate-errors");
        	System.out.println("***********************************"+options+"***********************");
        	capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        	WebDriver driver = new ChromeDriver(capabilities);
		CavissonDriver cav = null;       
        	try{
        		cav = new CavissonDriver(driver,etcDir,imageDir);
        	}catch(Exception e){log("Error in instantiating the Driver");}
        	try{
        	//Thread.sleep(10000);
        	}catch(Exception e){log(e.getMessage());}
        	return cav;
	}
	private void quitDriver(){
		driver.quit();
	}
	private void parseActions(){
        for(String m:actionsList) {
        	String actionType = m.split("_")[0];
        	if(actionType.equals("get")){
        		GetURL get =actions.get(m,GetURL.class); 
        		driver.get(get.getUrl(),get.getCaseName());
        	}
        	else if(actionType.equals("click")){
        		Click click = actions.get(m,Click.class);
        		driver.click(locateElement(click.getLocator(),click.getValue()),click.getCaseName());
        	} 
        	else if(actionType.equals("submit")){
        		Submit submit=actions.get(m,Submit.class);
        		driver.submit(locateElement(submit.getLocator(),submit.getValue()),submit.getCaseName());
        	} 
        	else if(actionType.equals("sendkey")){
        		SendKey sendKey = actions.get(m,SendKey.class);
        		driver.sendKeys(locateElement(sendKey.getLocator(),sendKey.getValue()),sendKey.getText(),sendKey.getCaseName());
        	}
		else if(actionType.equals("wait")) {
			WaitFor waitFor = actions.get(m,WaitFor.class);
			driver.waitFor(waitFor.getInterval());
		}
		else if(actionType.equals("assert")){
			Assert assertObj = actions.get(m,Assert.class);
			boolean assertResult = driver.assertByValue(locateElement(assertObj.getLocator(),assertObj.getValue()),assertObj.getAssertValue(),assertObj.getCaseName());
			if (assertResult){
				log(String.format("Assertion Passed for testcase name \"%s\" and assertion value \"%s\" ",assertObj.getCaseName(),assertObj.getAssertValue()));
			}else{
				driver.quit();
				throw new BuildException(String.format("Assertion failed for testcase name \"%s\" and assertion value \"%s\" hence stoping the flow",assertObj.getCaseName(),assertObj.getAssertValue()));	
			}
		}
		else if(actionType.equals("select")){
			Select select = actions.get(m,Select.class);
			driver.select(locateElement(select.getLocator(),select.getValue()),select.getSelectValue(),select.getCaseName());
		}
		else if(actionType.equals("assertScenario")){
			AssertScenario assertScenario = actions.get(m,AssertScenario.class);
			log(" [ DEBUG ] ASSERT - Assert Scenario is Logged");
			boolean b = assertScenario.checkAssert();
			if(b){
				driver.log("Keyword : "+ assertScenario.getKeyWord() + " Found in scenario");
			}
			else{
				driver.log("Keyword : " + assertScenario.getKeyWord() + " Not Found in scenario");
			}
		}
        }	
	}
	public void setChromeExecutablePath(String chromeExecutablePath){
		this.chromeExecutablePath=chromeExecutablePath;
	}
	public void setChromeBinaryPath(String chromeBinaryPath){
		this.chromeBinaryPath=chromeBinaryPath;
	}
	public void setHeadlessFlag(String headlessFlag){
		this.headlessFlag=Boolean.parseBoolean(headlessFlag);
	}

	public By locateElement(String name , String value){
		try{
            switch(name){
               case "id":
               return By.id(value);
               //break;
               case "name":
               return By.name(value);
               //break;
               case "className":
               return By.className(value);
               //break;
               case "xpath":
               return By.xpath(value);
               //break;
               case "cssSelector":
               return By.cssSelector(value);
               //break;
               default:
               return By.id(value);
               //break;
	    	}
	    }catch(Exception e){System.out.println("[ ERROR ] Some error occured with the entered element name and value");}
        return null;
	}

	public void execute()throws BuildException{
		setUp();
		verifyTaskConfiguration();
		driver = instantiateDriver();
		parseActions();
		quitDriver();
	}
        public WaitFor createWaitFor(){
		waitItem++;
		WaitFor waitFor = new WaitFor();
		actions.put("wait_"+waitItem,waitFor,WaitFor.class);	
                actionsList.add("wait_"+waitItem);
                return waitFor;
	}
	public Assert createAssert(){
		assertItem++;
		Assert assertObj= new Assert();
		actions.put("assert_"+assertItem,assertObj,Assert.class);
		actionsList.add("assert_"+assertItem);
        	return assertObj;
	}
	public Select createSelect(){
		selectItem++;
		Select select= new Select();
		actions.put("select_"+selectItem,select,Select.class);
		actionsList.add("select_"+selectItem);
        	return select;
	}	
	public Click createClick(){
		clickItem++;
		Click click = new Click();
		actions.put("click_"+clickItem,click,Click.class);
		actionsList.add("click_"+clickItem);
        	return click;
	}
	public Submit createSubmit(){
		submitItem++;
		Submit submit = new Submit();
		actions.put("submit_"+submitItem,submit,Submit.class);
		actionsList.add("submit_"+submitItem);
        	return submit;
	}

	public SendKey createSendKey(){
		sendKeyItem++;
		SendKey sendKey = new SendKey();
		actions.put("sendkey_"+sendKeyItem,sendKey,SendKey.class);
		actionsList.add("sendkey_"+sendKeyItem);
        	return sendKey;
	}
	public GetURL createGetURL(){
		getItem++;
		GetURL get = new GetURL();
		actions.put("get_"+getItem,get,GetURL.class);
		actionsList.add("get_"+getItem);
        	return get;
	}

	public AssertScenario createAssertScenario(){
		assertScenarioItem++;
		AssertScenario assertScenario = new AssertScenario();
		actions.put("assertScenario_"+assertScenarioItem,assertScenario,AssertScenario.class);
		actionsList.add("assertScenario_"+assertScenarioItem);
        	return assertScenario;
	}

	public abstract class Action{
		String caseName;
		String type;
        	public void setCaseName(String caseName){
			this.caseName=caseName;
		}
		public String getCaseName(){
			return this.caseName;
		}
		public String getType(){
			return this.type;
		}
	}
        public class WaitFor extends Action{
		int interval;
		public WaitFor(){
			super();
			type = "Wait";
		}
                public void setInterval(String value){
			if (value.matches("^[0-9]*$") && value.length() > 0){
				this.interval = Integer.parseInt(value);
			}
                        else{
				 throw new BuildException("\" "+value+" \" is not a valid integer");	
			}
		}
                public int getInterval(){
			return this.interval;
		}
	}
        public class Assert extends Action{
		String locator;
		String value;
		String assertValue;
		public Assert(){
			super();
			type =  "Assert";
		}	
	        public void setLocator(String locator){
			this.locator=locator;
		}
		public String getLocator(){
			return this.locator;
		}
		public void setValue(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
		public void setAssertValue(String assertValue){
			this.assertValue = assertValue;
		}
		public String getAssertValue(){
			return this.assertValue;
		}
	}
        public class Select extends Action{
		String locator;
		String value;
		String selectValue;
		public Select(){
			super();
			type =  "Select";
		}	
	        public void setLocator(String locator){
			this.locator=locator;
		}
		public String getLocator(){
			return this.locator;
		}
		public void setValue(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
		public void setSelectValue(String selectValue){
			this.selectValue = selectValue;
		}
		public String getSelectValue(){
			return this.selectValue;
		}
	}
	public class Click extends Action{
		String locator;
		String value;
		public Click(){
			super();
			type =  "Click";
		}
	        public void setLocator(String locator){
			this.locator=locator;
		}
		public String getLocator(){
			return this.locator;
		}
		public void setValue(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
	}
	public class Submit extends Action{
		String locator;
		String value;
		public Submit(){
			super();
			type =  "submit";
		}
        public void setLocator(String locator){
			this.locator=locator;
		}
		public String getLocator(){
			return this.locator;
		}
		public void setValue(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
	}

	public class SendKey extends Action{
		String locator;
		String value;
		String text;
		public SendKey(){
			super();
			type =  "sendkey";
		}
                public void setLocator(String locator){
			this.locator=locator;
		}
		public String getLocator(){
			return this.locator;
		}
		public void setValue(String value){
			this.value=value;
		}
		public String getValue(){
			return this.value;
		}
		public void setText(String text){
			this.text=text;
		}
		public String getText(){
			return this.text;
		}
	}
	public class GetURL extends Action{
		String url;
		public GetURL(){
			super();
			type = "get";
		}
		public void setUrl(String url){
			this.url=url;
		}
		public String getUrl(){
			return this.url;
		}
	}
	public class AssertScenario extends Action{
		public String machineAddress;
		public String keyWord;
		public String controllerName;
		public String projectName;
		public String subProjectName;
		public String scenarioName;

		public AssertScenario(){
			type = "assertScenario";
		}

		public String getMachineAddress(){
			return this.machineAddress;
		}
		public void setMachineAddress(String machineAddress){
			this.machineAddress = machineAddress;
		}
		public String getKeyWord(){
			return this.keyWord;
		}
		public void setKeyWord(String keyWord){
			this.keyWord = keyWord;
		}
		public void setControllerName(String controllerName){
			this.controllerName = controllerName;
		}
		public String getControllerName(){
			return this.controllerName;
		}
		public String getProjectName(){
			return this.projectName;
		}
		public void setProjectName(String projectName){
			this.projectName = projectName;
		}
		public String getSubProjectName(){
			return this.subProjectName;
		}
		public void setSubProjectName(String subProjectName){
			this.subProjectName = subProjectName;
		}
		public void setScenarioName(String scenarioName){
			this.scenarioName = scenarioName;
		}
		public String getScenarioName(String scenarioName){
			return this.scenarioName;
		}
		public boolean checkAssert(){
			String remoteCommand = "grep" + " " + "'" + keyWord + "'" + " " + "/home/cavisson/" + controllerName + "/scenarios/" + projectName + "/" + subProjectName + "/" + scenarioName;
			try{
			System.out.println(remoteCommand);
			return RemoteHandler.remoteCmd("cavisson",machineAddress,22,"cavisson",remoteCommand).equals(keyWord);
			}catch(Exception e){return false;}
		}
	}

}
