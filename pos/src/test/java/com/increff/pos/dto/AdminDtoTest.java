package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdminDtoTest extends AbstractUnitTest {
    @Autowired
    private AdminApiDto adminApiDto;
    @Autowired
    private UserService userService;
    /**
     * Add a Supervisor User test
     * @throws ApiException
     */
    @Test
    public void addSupervisorTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("xyz@increff.com","xyz");
        adminApiDto.add(userForm);
        List<UserData> list = adminApiDto.getAll();
        List<UserPojo> pojos = userService.getAll();
        assertEquals(pojos.size(),list.size());
        assertEquals(pojos.get(0).getEmail(),list.get(0).getEmail());
        assertEquals(list.get(0).getRole(),list.get(0).getRole());
    }

    /**
     * Add an operator User test
     * @throws ApiException
     */
    @Test
    public void addOperatorTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("operator1@increff.com","xyz");
        adminApiDto.add(userForm);
        List<UserData> list = adminApiDto.getAll();
        List<UserPojo> pojos = userService.getAll();
        assertEquals(pojos.size(),list.size());
        assertEquals(pojos.get(0).getEmail(),list.get(0).getEmail());
        assertEquals(list.get(0).getRole(),list.get(0).getRole());
    }
}
