package com.example.app.welcome;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory
            .getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    @TransactionTokenCheck(value = "tokenTest", type = TransactionTokenType.BEGIN)
    public String home() {
        return "welcome/home";
    }

    @RequestMapping(value = "/token", method = {RequestMethod.POST})
    @TransactionTokenCheck(value = "tokenTest", type = TransactionTokenType.IN)
    public String token() {
        return "welcome/home";
    }
}
