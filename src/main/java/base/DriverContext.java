package base;

import driver.MyChromeDriver;

public class DriverContext {


    private static ThreadLocal<MyChromeDriver> driver = new ThreadLocal<>();

    public static synchronized void setDriver(MyChromeDriver inputDriver)
    {
        driver.set(inputDriver);
    }


    public static synchronized MyChromeDriver getDriver()
    {
        return driver.get();
    }
}


