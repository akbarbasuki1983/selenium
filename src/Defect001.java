// Additional External libary, Selenium for Java, downloaded from http://www.seleniumhq.org/download/
// Additional driver: geckodriver, downloaded from https://github.com/mozilla/geckodriver/releases.
// The geckodriver driver is configured (set the PATH) in System Variables
import java.util.List;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Defect001 {
	//Test Data
	String email="akbar_test_08@test.com";
	String password="test_08";
	String catalogName1="Printed Dress"; //catalog name must be under DRESSES -> EVENING DRESSES
	String catalogName2="Faded Short Sleeve T-shirts"; //catalog name must be under T-SHIRTS
	
	SoftAssert softAssert = new SoftAssert();
	WebDriver driver = new FirefoxDriver();
	WebDriverWait wait = new WebDriverWait(driver, 10);
	
	private String generateXPATH(WebElement childElement, String current) {
	    String childTag = childElement.getTagName();
	    if(childTag.equals("html")) {
	        return "/html[1]"+current;
	    }
	    WebElement parentElement = childElement.findElement(By.xpath("..")); 
	    List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
	    int count = 0;
	    for(int i=0;i<childrenElements.size(); i++) {
	        WebElement childrenElement = childrenElements.get(i);
	        String childrenElementTag = childrenElement.getTagName();
	        if(childTag.equals(childrenElementTag)) {
	            count++;
	        }
	        if(childElement.equals(childrenElement)) {
	            return generateXPATH(parentElement, "/" + childTag + "[" + count + "]"+current);
	        }
	    }
	    return null;
	}
		
	@BeforeTest
	public void prerequisite(){
		int initWishlist;
		
		//step: open application and login
		driver.get("http://automationpractice.com/index.php");
		wait.until(ExpectedConditions.elementToBeClickable(By.className("login")));
		//comment: sometime, there is issue that the login element is unable to be located. If fail, please run again and it will work
		driver.findElement(By.className("login")).click();
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("passwd")).sendKeys(password);
		driver.findElement(By.id("SubmitLogin")).click();
		//step: access to wishlist page
		driver.findElement(By.className("account")).click();
		driver.findElement(By.className("icon-heart")).click();
		//step: delete all wishlist
		if(driver.findElements(By.xpath(".//*[@id='block-history']")).size()!=0)
		{
			List<WebElement> rows = driver.findElements(By.xpath(".//*[@id='block-history']/table[@class='table table-bordered']/tbody/tr"));
			initWishlist=rows.size();
			for(int i=1;i<=initWishlist;i++){
				driver.findElement(By.xpath(".//*[@id='block-history']/table[@class='table table-bordered']/tbody/tr["+i+"]/td[6]/a")).click();
				WebDriverWait waitPopUp = new WebDriverWait(driver,10);
				Alert alert = waitPopUp.until(ExpectedConditions.alertIsPresent());
				alert.accept();
			}
		}
	}
	
	@AfterTest
	public void closeApplication(){
		if(driver.findElements(By.className("logout")).size()!=0)
			driver.findElement(By.className("logout")).click();
		driver.close();
	}
	
	
	@Test(priority=1)
	public void numOfItemOnWishlish(){
		int actWishListItem;
		int expWishListItem=1;
		
		//Step: access menu: (hover the mouse) DRESSES -> EVENING DRESSES
		WebElement dresses = driver.findElement(By.xpath(".//*[@id='block_top_menu']/ul/li[2]/a"));
		Actions action = new Actions(driver);
		action.moveToElement(dresses).build().perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='block_top_menu']/ul/li[2]/ul/li[2]/a")));
		driver.findElement(By.xpath(".//*[@id='block_top_menu']/ul/li[2]/ul/li[2]/a")).click();
		//Step: access to catalogue
		driver.findElement(By.xpath(".//*/img[@title='"+catalogName1+"']")).click();
		//Step: click the wishlist more than 1 time
		driver.findElement(By.id("wishlist_button")).click();
		driver.findElement(By.id("wishlist_button")).click();
		driver.findElement(By.id("wishlist_button")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@class='fancybox-item fancybox-close']")));
		driver.findElement(By.xpath(".//*[@class='fancybox-item fancybox-close']")).click();
		
		//Step: access to wishlist page to verify total wishlist created
		driver.findElement(By.xpath(".//*[@id='block_top_menu']/ul/li[3]/a")).click(); //no access just trigger for next findElement
		driver.findElement(By.className("account")).click();
		driver.findElement(By.className("icon-heart")).click();
		if(driver.findElements(By.xpath(".//*[@id='block-history']")).size()!=0)
		{
			List<WebElement> rows = driver.findElements(By.xpath(".//*[@id='block-history']/table[@class='table table-bordered']/tbody/tr"));
			actWishListItem=rows.size();
		}
		else
		{
			actWishListItem=0;
		}
		softAssert.assertEquals("Num of item:"+actWishListItem, "Num of item:"+expWishListItem);
		softAssert.assertAll();	
	}
	
	@Test(priority=2)
	public void qtyOfItemOnWishlish(){
		String actQuantity;
		String expQuantity="1";
		
		String xpathWishListCat;
		String xpathQtyWishList="div[2]/div[1]/div[1]/p[1]/input[1]";
		
		//Step: access menu: DRESSES -> EVENING DRESSES
		driver.findElement(By.xpath(".//*[@id='block_top_menu']/ul/li[3]/a")).click();
		//Step: access to catalogue
		driver.findElement(By.xpath(".//*/img[@title='"+catalogName2+"']")).click();
		//Step: click the wishlist more than 1 time
		driver.findElement(By.id("wishlist_button")).click();
		driver.findElement(By.id("wishlist_button")).click();
		driver.findElement(By.id("wishlist_button")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@class='fancybox-item fancybox-close']")));
		driver.findElement(By.xpath(".//*[@class='fancybox-item fancybox-close']")).click();
		
		//Step: access to wishlist page to verify Quantity of item on added wishlist item
		driver.findElement(By.xpath(".//*[@id='block_top_menu']/ul/li[3]/a")).click(); //no access just trigger for next findElement
		driver.findElement(By.className("account")).click();
		driver.findElement(By.className("icon-heart")).click();			
		if(driver.findElements(By.xpath(".//*[@id='block-history']")).size()!=0)
		{
			List<WebElement> rows = driver.findElements(By.xpath(".//*[@id='block-history']/table[@class='table table-bordered']/tbody/tr"));
			driver.findElement(By.xpath(".//*[@id='block-history']/table[@class='table table-bordered']/tbody/tr["+rows.size()+"]/td[1]/a")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@alt='"+catalogName2+"']")));
			WebElement wishID = driver.findElement(By.xpath(".//*[@alt='"+catalogName2+"']"));
			xpathWishListCat=generateXPATH(wishID,"wlp");			
			xpathQtyWishList=xpathWishListCat.substring(0, xpathWishListCat.length()-28)+xpathQtyWishList;
			actQuantity = driver.findElement(By.xpath(xpathQtyWishList)).getAttribute("value");
		}
		else
		{
			actQuantity="0";
		}
		softAssert.assertEquals("QTY:"+actQuantity, "QTY:"+expQuantity);
		softAssert.assertAll();		
	}
}
