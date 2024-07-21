package com.company.app.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtils {

    //--------------------Declaration-----------------------------
    public Page page;

    //-------------------Page Constructor-------------------------
    public GenericUtils(Page page) {
        this.page = page;
    }
    private static final Logger logger = LoggerFactory.getLogger(GenericUtils.class);

    //-------------------------xPaths--------------------
    private String xLoader = "//div[@class='loader']";

    //---------------------Locator---------------------------
    public Locator oLoader(){
        return page.locator(xLoader);
    }

    //-------------Generic methods------------------
    public void waitForResults(){
        try{
            int counter = 0;
            boolean Loader = page.locator(xLoader).isVisible();
            while (Loader && counter<60){
                System.out.println(counter);
                page.waitForTimeout(500);
                Loader = page.locator(xLoader).isVisible();
                counter++;
            }
            page.waitForTimeout(2000);
        }catch (Throwable ignored){}
    }
}
