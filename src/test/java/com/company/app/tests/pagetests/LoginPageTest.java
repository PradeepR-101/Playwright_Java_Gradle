package com.company.app.tests.pagetests;

import com.company.app.base.BaseTest;
import com.company.app.listeners.AllureListeners;
import com.company.app.pages.LoginPage;
import com.company.app.utils.CommonUtils;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

//import static com.company.app.utils.CommonUtils.getExceptionType;
//import static com.company.app.utils.GeneralUtils.AllureAddScreenshot;

@SpringBootTest
public class LoginPageTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginPageTest.class);

//    @Autowired
//    public CommonUtils commonUtils;
    public LoginPage loginPage;

    @Value("${appurl}")
    private String appurl;

    @Value("${environment}")
    private String environment;


    @Description("Verify sample test")
    @Test(groups = {"Regression"})
    public void verify_sample_test() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        BrowserContext browserContext = browser.newContext();
        Page page = browserContext.newPage();
        page.navigate("https://www.google.com");
        System.out.println(page.title());

        BrowserContext browserContext1 = browser.newContext();
        Page page1 = browserContext1.newPage();
        page1.navigate("https://www.naveenautomationlabs.com");
        System.out.println(page1.title());
    }

    @Description("Verify login test")
    @Test(groups = {"Regression"})
    public void verify_Login_Test(){

        Allure.label("testsuite",cont.getCurrentXmlTest().getSuite().getName());
        Allure.label("testingType","uitesting");

        SoftAssert softAssert = new SoftAssert();
        ITestResult result = Reporter.getCurrentTestResult();
        String manNo = "";

        try{
            getPage().navigate(appurl);

            Allure.label("application","default-application");
            softAssert.assertAll();
        } catch (PlaywrightException p){
//            String exceptionName = getExceptionType(p);
//            if(!exceptionName.equalsIgnoreCase("DriverException")){
//                AllureAddScreenshot(getPage());
//            }
//            Allure.label("application_error",exceptionName);
//            throw p;
        } catch (Exception e){
//            AllureAddScreenshot(getPage());
            logger.error("Exception occured in Login method" + e.getMessage());
//            Allure.label("application_error",getExceptionType(e));
//            throw e;
        } finally {
//            if(environment.equalIgnoreCase("QA")){
//                //
//            }
//            AllureListeners.orderNumber.put(result.getMethod().getMethodName(), orderNo);
        }


    }
}
