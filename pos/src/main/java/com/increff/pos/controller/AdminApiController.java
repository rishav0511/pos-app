package com.increff.pos.controller;

import com.increff.pos.model.UserData;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class AdminApiController {

    @Autowired
    private UserService service;


    @ApiOperation(value = "Gets list of all users")
    @RequestMapping(path = "/api/admin/user", method = RequestMethod.GET)
    public List<UserData> getAllUser() {
        List<UserPojo> list = service.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(convert(p));
        }
        return list2;
    }

    private static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

}
