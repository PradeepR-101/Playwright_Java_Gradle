package com.company.app.base;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import org.testng.log4testng.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;


@Configuration
public class BaseTest extends AbstractTestNGSpringContextTests {

    private static final Logger logger = Logger.getLogger(BaseTest.class);

    private static ThreadLocal<Playwright> playwrightThreadLocal = ThreadLocal.withInitial(Playwright::create);

    private static ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<BrowserContext> browserContextThreadLocal = new ThreadLocal<>();

    private static ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public static synchronized Playwright getPlaywright(){
        return playwrightThreadLocal.get();
    }

    public static Browser getBrowser(){
        return browserThreadLocal.get();
    }

    public static BrowserContext getBrowserContext(){
        return browserContextThreadLocal.get();
    }

    public static Page getPage(){
        return pageThreadLocal.get();
    }

    private String Headless;
    public static Properties prop;
    public static long start = System.currentTimeMillis();
    public static ITestContext cont;

    public void setup(){
        logger.info("start time in milliseconds = " + start);
        try{
            Headless = prop.getProperty("headless").toLowerCase();
            boolean head = Boolean.parseBoolean(Headless);
            Browser browser = null;
            switch (prop.getProperty("browser").toLowerCase()){
                case "chromium":
                    browser = (getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("chromium")));
                    break;
                case "chrome":
                    browser = getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("chrome"));
                    break;
                case "msedge":
                    browser = getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("msedge"));
                    break;
                case "firefox":
                    browser = getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("firefox"));
                    break;
                case "safari":
                    browser = getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("safari"));
                    break;
                case "mobile":
                    browser = getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(head).setChannel("chromium"));
                    break;
                default:
                    logger.info("please pass browser name...........");
                    break;
            }
            browserThreadLocal.set(browser);
            //browserContextThreadLocal.set(browserThreadLocal.get().newContext(/*new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/"))*/));
            //browserContextThreadLocal.set(browserThreadLocal.get().newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true)));

            browserContextThreadLocal.set(browserThreadLocal.get().newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("SessionStorage.json"))));

            //browserContextThreadLocal.get().storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("SessionStorage.json")));

            browserContextThreadLocal.get().tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
            pageThreadLocal.set(browserContextThreadLocal.get().newPage());
        }catch(Exception e){
            logger.error("" + Long.toString(Thread.currentThread().getId()) + "-Exception occured in setup method- "+ e.getMessage());
            Allure.label("Exception","setup method - "+e.getLocalizedMessage());
            closeThreads();
        }

    }

    public void tearDown(ITestResult iTestResult) throws IOException{
        try{
            if(iTestResult.getStatus() == ITestResult.SUCCESS || iTestResult.getStatus() == ITestResult.SKIP){
                getBrowserContext().tracing().stop();
            } else{
                getBrowserContext().tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("traces/" + iTestResult.getMethod().getMethodName().replace("()","") + ".zip")));
            }
        } catch (Exception e){
        }
        //closeThreads();
        try{
            if(pageThreadLocal.get()!=null){
                pageThreadLocal.get().close();
                pageThreadLocal.remove();
            }
        } catch(Exception e){
        }
    }

    @BeforeSuite(groups = {"QA","PROD","Regression","E2E","TEST"})
    public void load(){
        prop = new Properties();
        String Env = System.getProperty("env");
        try{
            prop.load(new FileInputStream("./src/main/resources/application-QA.properties"));
            //prop.load(new FileInputStream("./src/main/resources/application-"+Env+".properties"));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @BeforeMethod(groups = {"QA","PROD","Regression","E2E","TEST"})
    public void tearup(){
        setup();
    }

    @AfterMethod(groups = {"QA","PROD","Regression","E2E","TEST"})
    public void teardown(ITestResult iTestResult) throws IOException {
        tearDown(iTestResult);
    }

    @AfterSuite(groups = {"QA","PROD","Regression","E2E","TEST"})
    public void afterSuite(){
    }

    public void closeThreads(){
        try{
            getBrowserContext().tracing().stop();
        } catch (Exception e){
        }
        if(pageThreadLocal.get()!=null){
            pageThreadLocal.get().close();
            pageThreadLocal.remove();
        }
        if(browserThreadLocal.get().isConnected() || browserThreadLocal.get()!=null){
            browserThreadLocal.get().close();
            browserThreadLocal.remove();
        }
        if(playwrightThreadLocal.get()!=null){
            playwrightThreadLocal.get().close();
            playwrightThreadLocal.remove();
        }
    }

}
