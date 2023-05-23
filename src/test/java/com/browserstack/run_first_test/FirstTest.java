package com.browserstack.run_first_test;

import CSVReader.Rows;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

//import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tools.ant.taskdefs.Get;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
//import io.appium.java_client.TouchAction;

public class FirstTest extends BrowserStackTestNGTest {

    int SimulateWait=1000;

    @Test
    public void test() throws Exception {

        System.out.println("#############Test Started#######");
        List<String>Addresses =ReadCsv();

        String[] Parts= Addresses.get(1).split(",");


//        SkipLogin();
        int i=1;
        do  {
            SetWait();
            Parts = Addresses.get(i).split(",");
            Login(Parts);
            //SkipLogin();
            performTest(Parts);
            i++;
            if(i<Addresses.size())
            {
                if(driver!=null)
                { driver.quit();}
                NewDriver();
            }
        }while(i<Addresses.size());
        System.out.println("#############Test Finished#############");

    }

    void SetWait()
    {
        wait = new FluentWait(driver)
                .withTimeout(25, SECONDS)
                .pollingEvery(10, MILLISECONDS)
                .ignoring(Exception.class);
    }

    Wait wait;

    void Login(String[] Parts) throws Exception
    {
        WebElement SignInButton = GetElement(wait,0,"Sign in","android.widget.Button","");
        SignInButton.click();

        WebElement AddAccount = GetElement(wait,0,"add account","android.widget.TextView","");
        AddAccount.click();

        WebElement EmailIdTextBox = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View/android.widget.EditText");
        EmailIdTextBox.click();
        Thread.sleep(200);
        EmailIdTextBox.sendKeys(Parts[0]);

        WebElement SINextButton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[3]/android.view.View/android.widget.Button");
        SINextButton.click();

        WebElement EIdPassword = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText");
        EIdPassword.sendKeys(Parts[1]);

        WebElement PSNextButton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[3]/android.view.View/android.widget.Button");
        PSNextButton.click();



        WebElement EnterAuthCodeLabel = GetElement(wait,0,"Get a verification code from the Google Authenticator app@Check your","android.widget.TextView","");

        if(EnterAuthCodeLabel!=null)
        {
            if (EnterAuthCodeLabel.getText().contains("Check your"))
            {
                //Try Another
                FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.widget.Button").click();

            }
            EnterAuthCodeLabel = GetElement(wait,0,"Get a verification code from the Google Authenticator app","android.widget.TextView","");

            EnterAuthCodeLabel.click();
        }

        WebElement AuthCodeTextBox = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.view.View/android.widget.EditText");
        AuthCodeTextBox.sendKeys(TOTPGenerator.getTwoFactorCode(Parts[2]));
        FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[3]/android.view.View/android.widget.Button").click();


        //Add phone Number
        try {
            WebElement Add_Phn_No = GetElement(wait, 0, "Add phone number?", "android.widget.TextView", "");
            if (Add_Phn_No != null) {
                if (Add_Phn_No.getText().contains("Add phone number?")) {
                    //Skip Button
                    ScrollForLoctionHistory();
                    FindEle(wait, SearchBy.ByXPath, "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.widget.Button").click();

                }
            }
        }catch (Exception e)
        {

        }

        //Never lose your contacts
        try{
            WebElement Never_lose_Contact = GetElement(wait,0,"Never lose your contacts","android.widget.TextView","");
            if(Never_lose_Contact!=null)
            {
                if (Never_lose_Contact.getText().contains("Never lose your contacts"))
                {
                    //Don't turn On Button
                    FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View[4]/android.view.View/android.widget.Button").click();

                }
            }
        }catch (Exception e){

        }


        //I Agree
        WebElement IAgree = FindEle(wait,SearchBy.ByButton,"I agree");
        if(IAgree!=null)
        {
            IAgree.click();
        }

        // Accept Backup and Storage
        FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.Button").click();

    }

    void SkipLogin() throws Exception
    {
        WebElement SkipButton = GetElement(wait,0,"skip","android.widget.Button","");
        SkipButton.click();
    }
    public void performTest(String[] Parts) throws Exception
    {
        //Get list of lat long
        List<LatLng> latLngs = DirectionPolyline.GetDirections(Parts[5], Parts[6]);

        Scrolls=0;
        float Lat,Lng,Alt;
        //System.out.println("Location: "+Double.parseDouble(Parts[5])+","+ Double.parseDouble(Parts[6]));
        System.out.println("Location: "+Parts[5]+","+ Parts[6]);

        driver.setLocation(new Location(latLngs.get(0).getLat(), latLngs.get(0).getLng(),0));

        locationActivity();
        // LocHistoryNWebActivity();
        // LoctionHistory();
        SearchKeyword(Parts[3]);
        if(!FindShop(Parts[4],Parts[6],Integer.parseInt(Parts[8]))){ return; }

        StartDirectionsFromList();
        SimulateLocations(Parts, latLngs );
        RestartDirection();
        FinishDirections();
        SaveToTimeLine(Parts[4]);
        Review(Parts);
        Thread.sleep(5000);
    }

    WebElement ActivateSearchBox() throws InterruptedException
    {
        WebElement SearchLocationTextView = GetElement(wait,0,"Search here","android.widget.TextView","");
        SearchLocationTextView.click();

        WebElement SearchLocationEditText = GetElement(wait,0,"Search here","android.widget.EditText","");
        SearchLocationEditText.click();
        Thread.sleep(200);
        return SearchLocationEditText;
    }
    void SearchKeyword(String Keyword) throws InterruptedException {
        WebElement SearchLocationEditText = ActivateSearchBox();
        SearchLocationEditText.sendKeys(Keyword);
        Thread.sleep(1000);
        driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));

    }

    int Scrolls=0;
    boolean FindShop(String ShopName,String Address,int MaxScrollCount) throws InterruptedException {

        System.out.println("*****************Searching for:"+ShopName+"*********");
        try {
            WebElement SearchLocationTextView = GetElement(wait,0,ShopName,"android.widget.TextView","/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.support.v7.widget.RecyclerView");
            if(SearchLocationTextView!=null)
            {
                SearchLocationTextView.click();
                // new code to match address------------------
                ScrollForShopAddress();
                List<AndroidElement> ListElement = driver.findElements(By.className("android.widget.TextView"));
                for(WebElement we : ListElement)
                {
                    if(we.getText().replace(","," ").toLowerCase().contains(Address.substring(0,20).toLowerCase()))
                    {
                        System.out.println("*****************Address Matched*********");
                        return true;
                    }
                }

                System.out.println("*****************Address not Match*********");
                //Click Back Button
                FindEle(wait,SearchBy.ByXPath,"//*[(@content-desc='Back to Search')]").click();
                Thread.sleep(2000);
                Scroll();
                Scrolls++;
                return FindShop(ShopName,Address,MaxScrollCount);
                // new code to match address------------------
            }
        }catch (Exception e){
            if(Scrolls<MaxScrollCount)
            {
                Scroll();
                Scrolls++;
                return FindShop(ShopName,Address,MaxScrollCount);
            }
            else{
                driver.navigate().back();
                driver.navigate().back();
                System.out.println("Location Not found: "+ShopName);
                //go to home
                //FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ImageView").click();

            }
        }

        return false;
    }

    void Scroll() throws InterruptedException {
        //WebElement RecyclerView = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.support.v7.widget.RecyclerView");

        TouchAction action = new TouchAction(driver);

        int height = driver.manage().window().getSize().height;
        int width = driver.manage().window().getSize().width;

//        action. press(PointOption.point(RecyclerView.getLocation().getX()+150,RecyclerView.getLocation().getY()-150)).release().perform();
//        WebElement ShowList = GetElement(wait,0,"show list","android.widget.TextView");
//        ShowList.click();

       // List<AndroidElement> ListElementa =  RecyclerView.findElements(By.className("android.widget.TextView"));

//        PointOption to= PointOption.point(ListElementa.get(0).getLocation()) ;
//        PointOption from= PointOption.point(ListElementa.get(0).getLocation().getX(),height-100) ;

        PointOption to= PointOption.point(100,100) ;
        PointOption from= PointOption.point(100,height-100) ;
        action.longPress(from)
                .moveTo(to).release().perform();
        //Thread.sleep(100);
//        action.longPress(from)
//                .moveTo(to).release().perform();
//        Thread.sleep(100);
//        action.longPress(from)
//                .moveTo(to).release().perform();
//        Thread.sleep(100);
//        action.longPress(from)
//                .moveTo(to).release().perform();

    }
    void StartDirectionsFromList()
    {
        WebElement DirectionsButton = GetElement(wait,0,"Directions","android.widget.TextView","");
        if(DirectionsButton!=null)
        {
            DirectionsButton.click();
            ChooseStartLocation();
        }
    }

    void StartDirections()
    {
        //Click on Direction
        FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout[1]/android.support.design.chip.Chip").click();
        ChooseStartLocation();

    }

    void ChooseStartLocation()
    {
        //Click on Choose your starting point
        FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout[3]/android.widget.LinearLayout/android.widget.EditText[1]/android.widget.LinearLayout/android.widget.TextView").click();

        //Select your current location
        WebElement YourLocation = GetElement(wait,0,"Your location","android.widget.TextView","");
        YourLocation.click();

        //Click On Start
        FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[4]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.TextView").click();

        //Got It
        try {
            FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.Button").click();

        }catch(Exception e)
        {}
    }

    void RestartDirection()
    {
        try {
            //Close navigation
            FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.ImageView[1]").click();

            //Click On Start
            FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[4]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.TextView").click();

        }catch (Exception e)
        {}
    }


    void SimulateLocations(String[] Parts,List<LatLng> latLngs) throws Exception
    {
        for (int i=0;i<latLngs.size();i++)
        {
            driver.setLocation(new Location(latLngs.get(i).getLat(), latLngs.get(i).getLng(),0));
            Thread.sleep(Integer.parseInt(Parts[7]));
        }

//        driver.setLocation(new Location(latLngs.get(latLngs.size()-1).getLat(), latLngs.get(latLngs.size()-1).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));
//        driver.setLocation(new Location(latLngs.get((latLngs.size()/6)*1).getLat(), latLngs.get((latLngs.size()/6)*1).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));
//
//        driver.setLocation(new Location(latLngs.get((latLngs.size()/6)*2).getLat(), latLngs.get((latLngs.size()/6)*2).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));
//
//        driver.setLocation(new Location(latLngs.get((latLngs.size()/6)*3).getLat(), latLngs.get((latLngs.size()/6)*3).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));
//
//        driver.setLocation(new Location(latLngs.get((latLngs.size()/6)*4).getLat(), latLngs.get((latLngs.size()/6)*4).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));
//
//        driver.setLocation(new Location(latLngs.get((latLngs.size()/6)*5).getLat(), latLngs.get((latLngs.size()/6)*5).getLng(),0));
//        Thread.sleep(Integer.parseInt(Parts[7]));


        Thread.sleep(1000);
    }

    void FinishDirections()
    {
        //After Reaching Click on Done
//       WebElement DoneButton= FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView");
        WebElement DoneButton= FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView");
        if(DoneButton!=null)
       {
           try {
               FindEle(wait,SearchBy.ByXPath,"\t/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView");

           }catch (Exception e)
           {}
           DoneButton.click();
       }
//        WebElement element= GetElement(wait,0,"Done","android.widget.TextView");
//        element
    }

    void SaveToTimeLine(String ShopName) throws InterruptedException {
    WebElement SearchLocationEditText = ActivateSearchBox();
    //Click on first element from search history
    GetElement(wait,0,ShopName,"android.widget.TextView","").click();
    Thread.sleep(1000);
    GetElement(wait,1,ShopName,"android.widget.TextView","").click();

    WebElement hereNow= GetElement(wait,0,"Are you here now?","android.widget.TextView","");
    hereNow.click();

    WebElement YesButton= GetElement(wait,0,"yes","android.widget.Button","");
    YesButton.click();
    Thread.sleep(1000);

}

    void Review(String[] Parts) throws InterruptedException {
        if(Parts.length>9 && !Parts[9].isEmpty())
        {
            //old xpath review
//            WebElement ReviewButton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.HorizontalScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.TextView");
//            ReviewButton.click();

            WebElement ReviewButton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.HorizontalScrollView/android.widget.LinearLayout/android.widget.LinearLayout[3]/android.widget.TextView");
            ReviewButton.click();

            WebElement Five_Star = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout[2]/android.support.v4.view.ViewPager/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.view.ViewGroup/android.widget.ImageView[5]");
            Five_Star.click();

            WebElement Add_comment = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[3]/android.widget.RelativeLayout/android.widget.EditText");
            Add_comment.click();
            Add_comment.sendKeys(Parts[9]);

            WebElement Post_Button = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button");
            Post_Button.click();
            Thread.sleep(1000);

        }
    }

    public static List<String> ReadCsv() throws URISyntaxException {
        Path fileName = Paths.get("src/test/resources/Directions.csv");
//      Path fileName = Paths.get("C:\\Users\\Patil\\Downloads\\Directions.csv");
        List<String> Rowss=null;
        // Now calling Files.readString() method to
        // read the file
        try {
            Rowss = Files.readAllLines(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Rows rws=new Rows();
        rws.ParseData(Rowss);
        return Rowss;
    }

    enum SearchBy {
        ById,
        ByXPath,
        ByLinkText,
        ByButton
    }

    public WebElement FindEle(Wait wait,SearchBy searchBy,String XpathOrId)
    {
        return (WebElement) wait.until(new Function<AndroidDriver, WebElement>() {
            @Override
            public WebElement apply(AndroidDriver driver) {


                if(searchBy==SearchBy.ById)
                {
                    return driver.findElement(By.id(XpathOrId));
                }
                else if (searchBy==SearchBy.ByXPath){
                    return driver.findElement(By.xpath(XpathOrId));
                }
                else if (searchBy==SearchBy.ByLinkText){
                    List<WebElement> ListElement=  driver.findElements(By.className("android.widget.TextView"));

                    WebElement ele=  ListElement.stream().filter((item -> ((WebElement)item).getText().contains(XpathOrId))).collect(Collectors.toList()).get(0);
                    //  driver.findElements(By.className("android.widget.TextView")).stream().filter((item -> ((WebElement)item).getText().contains("Google Authenticator"))).collect(Collectors.toList());
                    return ele;
                    // return ListElement.get(0);
                    //(WebElement) driver.findElements(By.className("android.widget.TextView")).stream().filter((item -> ((WebElement)item).getText().equals("google"))).collect(Collectors.toList());
                }
                else if (searchBy==SearchBy.ByButton){
                    List<WebElement> ListElement=  driver.findElements(By.className("android.widget.Button"));

                    for(WebElement we : ListElement)
                    {
                        for(String str :XpathOrId.split("@"))
                        {
                            if(we.getText().contains(str))
                            {
                                return  we;
                            }
                        }
                    }
                    return null;
                }

                return null;
            }
        });
    }


    public WebElement GetElement(Wait wait,int index,String Label,String Xpath,String Parent)
    {

        return (WebElement) wait.until(new Function<AndroidDriver, WebElement>() {
            @Override
            public WebElement apply(AndroidDriver driver) {
                String[] XpathParts=Xpath.split("/");
                WebElement ParentElement=null;
                List<WebElement> ListElement ;
                if(!Parent.isEmpty())
                {
//                    System.out.println("----------Parent Found-------------");
                    ParentElement  =driver.findElement(By.xpath(Parent));
                    ListElement =  ParentElement.findElements(By.className(XpathParts[XpathParts.length-1]));
                }
                else {
                    ListElement =  driver.findElements(By.className(XpathParts[XpathParts.length-1]));
                }

                int indx=index;
                for(WebElement we : ListElement)
                {
//                    System.out.println("Webelement:"+we.getText());
                    for(String str :Label.split("@"))
                    {

                        if(we.getText().toLowerCase().contains(str.toLowerCase()))
                        {

                            if(indx==0)
                            {
                                return  we;
                            }
                            indx-=indx;

                        }
                    }
                }
                return null;
            }
        });
    }

    void locationActivity() throws InterruptedException {

        String AppPackage= driver.getCurrentPackage();
        ScreenOrientation orientation=  driver.getOrientation();


        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"//*[(@content-desc='Menu')]");
        More_Options.click();
        Thread.sleep(2000);

//        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ImageView");
//        More_Options.click();

//        WebElement Expand_Acc_Details_Option = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ImageButton");
//        Expand_Acc_Details_Option.click();
        ScrollForLoctionHistory();
        WebElement Setting_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.TextView[3]");
        Setting_Options.click();

        WebElement Google_Location_Setting = FindEle(wait,SearchBy.ByXPath,"\t/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.TextView");
        Google_Location_Setting.click();

//        WebElement Timeline_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.RelativeLayout[2]/android.widget.LinearLayout/android.widget.TableLayout/android.widget.TableRow/android.widget.TextView");
//        Timeline_Options.click();

        WebElement Advance_Options = GetElement(wait,0,"Advanced","android.widget.TextView","");
        Advance_Options.click();

        ScrollForLoctionHistory();

        WebElement Google_Loc_History_Options = GetElement(wait,0,"Google Location History","android.widget.TextView","");
//        WebElement Google_Loc_History_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[7]/android.widget.LinearLayout[2]/android.widget.TextView");
        Thread.sleep(1000);
        Google_Loc_History_Options.click();

        WebElement Loc_History_TurnOn_Options = GetElement(wait,0,"Turn","/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.Button","");
        String LocationStatus = Loc_History_TurnOn_Options.getText();

        if(LocationStatus.equals("Turn on"))
        {
            Loc_History_TurnOn_Options.click();
            WebElement Turn_On_Button = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.app.Dialog/android.view.View/android.view.View[3]/android.view.View[2]/android.widget.Button");
            do {
                ScrollForLoctionHistory();
            }while(!Turn_On_Button.isEnabled());

            Turn_On_Button.click();
            System.out.println("*****************Web Activity and Location History Turned On*********");

            WebElement Gotit_Loc_On_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.app.Dialog/android.view.View/android.view.View[3]/android.view.View/android.widget.Button");
            Gotit_Loc_On_Options.click();

            Thread.sleep(5000);
            //driver.pressKeyCode(187);
            driver.activateApp(AppPackage);
            driver.rotate(orientation);
            //driver.pressKeyCode(4);

        }else {
            Thread.sleep(5000);
            driver.activateApp(AppPackage);
            driver.rotate(orientation);
            System.out.println("*****************Web Activity and Location History is Already On*********");
//            driver.pressKeyCode(4);
        }

        Thread.sleep(2000);
        WebElement Back_button_Loc_On = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ImageButton");
        Back_button_Loc_On.click();
    }

//    void LocHistoryNWebActivity() throws InterruptedException {
//
////        WebElement SearchLocationTextView = GetElement(wait,0,"Search here","android.widget.TextView","");
////        SearchLocationTextView.click();
////
////        WebElement SearchLocationEditText = GetElement(wait,0,"Search here","android.widget.EditText","");
////        SearchLocationEditText.click();
//
////        WebElement GPSLoc = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.widget.ImageView");
////        GPSLoc.click();
//
//        Thread.sleep(1000);
//        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"//*[(@content-desc='Menu')]");
//        More_Options.click();
////        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout");
////        More_Options.click();
//
//        WebElement Timeline_Option = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.RelativeLayout[2]/android.widget.LinearLayout/android.widget.TableLayout/android.widget.TableRow/android.widget.TextView");
//        Timeline_Option.click();
//
//        WebElement Explore_Timeline_Letsgo = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button[2]");
//        Explore_Timeline_Letsgo.click();
//
//        WebElement More_Options1 = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.support.v7.widget.LinearLayoutCompat/android.widget.ImageView");
//        More_Options1.click();
//
//        WebElement SettingsTimeline = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
//        SettingsTimeline.click();
//
//        // Web & App Activity
//        WebElement Web_App_Activity_settings = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[4]/android.widget.RelativeLayout/android.widget.TextView[1]");
//        Web_App_Activity_settings.click();
//
//        WebElement Web_App_Activity_TurnOn = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.Button");
//        String TurnOnButton = Web_App_Activity_TurnOn.getText();
//
//        if(TurnOnButton.equals("Turn on"))
//        {
//            Web_App_Activity_TurnOn.click();
//            WebElement Turn_On_Button = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.app.Dialog/android.view.View/android.view.View[3]/android.view.View[2]/android.widget.Button");
//            do {
//                ScrollForLoctionHistory();
//            }while(!Turn_On_Button.isEnabled());
//            Turn_On_Button.click();
//
//            WebElement Gotit_Loc_On_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.app.Dialog/android.view.View/android.view.View[3]/android.view.View/android.widget.Button");
//            Gotit_Loc_On_Options.click();
//
//            Thread.sleep(3000);
//        }
//        else
//        {
//            WebElement Backbutton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ImageButton");
//            Backbutton.click();
//        }
//
//        ScrollForLoctionHistory();
//
//        WebElement 	Location_History = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[5]/android.widget.RelativeLayout/android.widget.TextView[1]");
//        Location_History.click();
//    }

//    void LoctionHistory() throws InterruptedException {
//
//        Thread.sleep(1000);
//        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"//*[(@content-desc='Menu')]");
//        More_Options.click();
////        WebElement More_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout");
////        More_Options.click();
//        WebElement Timeline_Option = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.RelativeLayout[2]/android.widget.LinearLayout/android.widget.TableLayout/android.widget.TableRow/android.widget.TextView");
//        Timeline_Option.click();
//
//        WebElement Explore_Timeline_Letsgo = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.Button[2]");
//        Explore_Timeline_Letsgo.click();
//
//        WebElement More_Options1 = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.support.v7.widget.LinearLayoutCompat/android.widget.ImageView");
//        More_Options1.click();
//
//        WebElement SettingsTimeline = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView");
//        SettingsTimeline.click();
//
//        WebElement Location_History_TurnOn = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.Button");
//        String Loc_TurnOnButton = Location_History_TurnOn.getText();
//
//        if(Loc_TurnOnButton.equals("Turn on"))
//        {
//            Location_History_TurnOn.click();
//
//            WebElement Gotit_Loc_On_Options = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View/android.view.View/android.app.Dialog/android.view.View/android.view.View[3]/android.view.View/android.widget.Button");
//            Gotit_Loc_On_Options.click();
//
//            Thread.sleep(3000);
//        }
//        else
//        {
//            WebElement Backbutton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ImageButton");
//            Backbutton.click();
//        }
//
//        WebElement Backbutton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ImageButton");
//        Backbutton.click();
//
//        WebElement Time_Backbutton = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ImageButton");
//        Time_Backbutton.click();
//    }


    void ScrollForLoctionHistory() throws InterruptedException {
        //WebElement RecyclerView = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.support.v7.widget.RecyclerView");

        TouchAction action = new TouchAction(driver);

        int height = driver.manage().window().getSize().height;
        int width = driver.manage().window().getSize().width;


        PointOption to= PointOption.point(100,100) ;
        PointOption from= PointOption.point(100,height-300) ;
        action.longPress(from)
                .moveTo(to).release().perform();

    }

    void ScrollForShopAddress() throws InterruptedException {
        //WebElement RecyclerView = FindEle(wait,SearchBy.ByXPath,"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.view.ViewGroup/android.support.v7.widget.RecyclerView");

        TouchAction action = new TouchAction(driver);

        int height = driver.manage().window().getSize().height;
        int width = driver.manage().window().getSize().width;


        PointOption to= PointOption.point(100,height-500) ;
        PointOption from= PointOption.point(100,height-300) ;
        action.longPress(from)
                .moveTo(to).release().perform();

    }
}