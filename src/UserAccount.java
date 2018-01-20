// Additional External libary, Selenium for Java, downloaded from http://www.seleniumhq.org/download/
// Additional driver: geckodriver, downloaded from https://github.com/mozilla/geckodriver/releases.
// The geckodriver driver is configured (set the PATH) in System Variables
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserAccount {
	//Data for valid New User Account
	String email="akbar_test_08@test.com"; //ensure as new email
	String password="test_08";
	boolean male=true; //set: true or false
	String firstName="Akbar"; //without number
	String secondName="Basuki"; //without number
	String dateOfBirth="5  "; //must contain 2 spaces
	String monthOfBirth="May "; //must contain 1 space
	String yearOfBirth="1980  "; //must contain 2 spaces 
	String address="Cluster Darul Halim 2 kav 22";
	String city="Bandung Barat";
	String state="Arizona"; //refer to State option
	String zip="12345";
	String country="United States"; //refer to Country option
	String mobilePhone="0123456789";
	String alias="Rumah";
	
	WebDriver driver = new FirefoxDriver();
	WebDriverWait wait = new WebDriverWait(driver, 5);
	
	//Data for Invalid Login cases
	@DataProvider
	public String[][] getInvalidUserLogin(){
		String[][] invalidUserLogin = new String[3][2];
		//valid password but invalid user
		invalidUserLogin[0][0]="invalid"+email;
		invalidUserLogin[0][1]=password;		
		//valid user but invalid password
		invalidUserLogin[1][0]=email;
		invalidUserLogin[1][1]="invalid"+password;			
		//invalid user and password
		invalidUserLogin[2][0]="invalid"+email;
		invalidUserLogin[2][1]="invalid"+password;
		
		return invalidUserLogin;
	}

	@BeforeTest
	public void openBrowser(){
		driver.get("http://automationpractice.com/index.php");
	}
	
	@AfterTest
	public void closeBrower(){
		driver.close();
	}
	
	@BeforeMethod
	public void login(){
		driver.findElement(By.className("login")).click();
	}
	
	@AfterMethod
	public void logout(){
		if(driver.findElements(By.className("logout")).size()!=0)
			driver.findElement(By.className("logout")).click();
	}
	
	@Test(priority=1)
	public void registerNewAccount(){
		//Step: input an email to create a new account
		driver.findElement(By.id("email_create")).sendKeys(email);
		driver.findElement(By.id("SubmitCreate")).click();
		//Step: input all required fields for new account
		if (male){
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_gender1"))); //select Male
			driver.findElement(By.id("id_gender1")).click();
		}
		else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_gender2"))); //select Female
			driver.findElement(By.id("id_gender2")).click();
		}		
		driver.findElement(By.id("customer_firstname")).sendKeys(firstName); //First Name
		driver.findElement(By.id("customer_lastname")).sendKeys(secondName); //Second Name
		driver.findElement(By.id("passwd")).sendKeys(password); //Password
		Select day = new Select(driver.findElement(By.id("days")));
		day.selectByVisibleText(dateOfBirth); //Select day of birth
		Select month = new Select(driver.findElement(By.id("months")));
		month.selectByVisibleText(monthOfBirth); //Select month of birth
		Select year = new Select(driver.findElement(By.id("years")));
		year.selectByVisibleText(yearOfBirth); //Select year of birth
		driver.findElement(By.id("address1")).sendKeys(address); //Address
		driver.findElement(By.id("city")).sendKeys(city); //City
		Select stateList = new Select(driver.findElement(By.id("id_state")));
		stateList.selectByVisibleText(state); //Select State
		driver.findElement(By.id("postcode")).sendKeys(zip); //ZIP
		Select countryList = new Select(driver.findElement(By.id("id_country")));
		countryList.selectByVisibleText(country); //Select Country
		driver.findElement(By.id("phone_mobile")).sendKeys(mobilePhone); //Mobile phone
		driver.findElement(By.id("alias")).sendKeys(alias); //Alias
		//Step: click Register button
		driver.findElement(By.id("submitAccount")).click();
		//Verify the result Case1
		Assert.assertTrue(driver.findElement(By.className("info-account")).isDisplayed(),"fail to REGISTER");
	}
	
	@Test(priority=2)
	public void loginSuccess(){
		//Step: input email and password
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("passwd")).sendKeys(password);
		//Step: click SignIn button
		driver.findElement(By.id("SubmitLogin")).click();
		//Verify the result Case2
		Assert.assertTrue(driver.findElement(By.className("info-account")).isDisplayed(),"fail to LOGIN");
	}
	
	@Test(priority=3,dataProvider="getInvalidUserLogin")
	public void loginFail(
			String emailAcct, 
			String passAcct
			){
		//Step: input email and password
		driver.findElement(By.id("email")).sendKeys(emailAcct);
		driver.findElement(By.id("passwd")).sendKeys(passAcct);
		//Step: click SignIn button
		driver.findElement(By.id("SubmitLogin")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((".//*[@id='center_column']/div[1]/ol/li"))));
		//Verify the result Case3
		Assert.assertTrue(driver.findElement(By.xpath(".//*[@id='center_column']/div[1]/ol/li")).isDisplayed(),"No validation for Authentication");
	}
	
	@Test(priority=4)
	public void duplicateAccount(){
		//Step: input an email to create a new account		
		driver.findElement(By.id("email_create")).sendKeys(email);
		driver.findElement(By.id("SubmitCreate")).click();
		//Verify the result Case4
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((".//*[@id='create_account_error']/ol/li"))));
		Assert.assertTrue(driver.findElement(By.xpath(".//*[@id='create_account_error']/ol/li")).isDisplayed(),"No Validation for duplicate account");
	}
}
