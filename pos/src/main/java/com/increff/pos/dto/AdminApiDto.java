package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminApiDto {
    @Autowired
    private UserService userService;

    public void add(UserForm userForm) throws ApiException {
        ValidationUtils.validateForm(userForm);
        UserPojo user = ConvertUtil.convertFormToPojo(userForm);
        userService.add(user);
    }

    public List<UserData> getAll() {
        return userService
                .getAll()
                .stream()
                .map(ConvertUtil::convertPojoToData)
                .collect(Collectors.toList());
    }
}
