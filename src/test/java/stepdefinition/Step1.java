package stepdefinition;

import Page.Page1;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import utilities.ScreenShotUtil;

public class Step1 {

    private final Page1 page1 = new Page1();

    @Given("User on cypressDoc page")
    public void userOnCypressDoc() {
        page1.CypressDocPage();
    }

    @And("User type {string}")
    public void typeHelloWorld(String text) {
        switch (text) {
            case "Hello World":
                page1.helloWorld();
            break;
        }
    }

    @Given("User clicks first choice")
    public void clickFirstChoice() {
        page1.clickFirstChoice();
    }

    @And("User check {string} is shown")
    public void checkMsg(String text) {
        switch (text) {
            case "Write some text to a txt file":
                page1.checktxtFileMsg();
                break;
        }
    }



}
