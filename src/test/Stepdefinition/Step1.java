package Stepdefinition;

import Page.Page1;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class Step1 {

    private final Page1 page1 = new Page1();

    @Given("User on google chrome page")
    public void userOnGoogleChrome() {
        page1.googleChrome();
    }

//    @And("User type cypress and search")
//    public void userType() {
//        page1.typeing();
//    }

}
