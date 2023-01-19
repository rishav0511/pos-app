package com.increff.pos.dto;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdminDtoTest extends AbstractUnitTest {
    @Autowired
    private AdminApiDto adminApiDto;

    @Test
    public void addSupervisorTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("xyz@increff.com","xyz");
        adminApiDto.add(userForm);
        List<UserData> list = adminApiDto.getAll();
        assertEquals(1,list.size());
        assertEquals("xyz@increff.com",list.get(0).getEmail());
        assertEquals("supervisor",list.get(0).getRole());
    }

    @Test
    public void addOperatorTest() throws ApiException {
        UserForm userForm = TestUtils.getUserForm("operator1@increff.com","xyz");
        adminApiDto.add(userForm);
        List<UserData> list = adminApiDto.getAll();
        assertEquals(1,list.size());
        assertEquals("operator1@increff.com",list.get(0).getEmail());
        assertEquals("operator",list.get(0).getRole());
    }
}
