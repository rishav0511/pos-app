package com.increff.pos.controller;

import com.increff.pos.dto.AdminApiDto;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SignUpApiController extends AbstractUiController {

    @Autowired
    private AdminApiDto adminApiDto;
    @Autowired
    private InfoData info;

    @ApiOperation(value = "Initializes application")
    @RequestMapping(path = "/site/signup", method = RequestMethod.GET)
    public ModelAndView showPage() {
        if (info.getEmail() != "") {
            return mav("redirect:/ui/home");
        }
        info.setMessage("");
        return mav("signup.html");
    }

    @ApiOperation(value = "Initializes application")
    @RequestMapping(path = "/site/signup", method = RequestMethod.POST)
    public ModelAndView initSite(UserForm form) throws ApiException {
        if (info.getEmail() != "") {
            return mav("redirect:/ui/home");
        }
        try {
            adminApiDto.add(form);
        } catch (ApiException ex) {
            info.setMessage(ex.getMessage());
            return mav("redirect:/site/signup");
        }
        info.setMessage("");
        return mav("redirect:/site/login");
    }
}
