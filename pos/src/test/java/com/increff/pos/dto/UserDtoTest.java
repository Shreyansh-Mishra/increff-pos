package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest {

    @Autowired
    private UserDto userDto;

    @Autowired
    InfoData infoData;
    @Test
    public void testAdd() throws ApiException {
        UserForm user = new UserForm();
        user.setEmail("shreyansh@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        UserData data = userDto.getAllUsers().get(0);
        assertEquals("shreyansh@increff.com", data.getEmail());
    }

    @Test
    public void testLogin() throws ApiException {
        UserForm user = new UserForm();
        user.setEmail("shreyansh@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        LoginForm login = new LoginForm();
        login.setEmail("shreyansh@increff.com");
        login.setPassword("shreyansh");
        userDto.login(login);
        assertEquals("Login Successful!", infoData.getMessage());
    }

    @Test
    public void testLoginFail() throws ApiException {
        UserForm user = new UserForm();
        user.setEmail("shreyansh@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        LoginForm login = new LoginForm();
        login.setEmail("shreyansh@increff.com");
        login.setPassword("shreyansh1");
        userDto.login(login);
        assertEquals("Invalid username or password", infoData.getMessage());
    }

    @Test
    public void testDuplicateSignup() throws ApiException {
        UserForm user = new UserForm();
        user.setEmail("shreyansh@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        userDto.signup(user);
        assertEquals("The email is already registered!", infoData.getMessage());
    }

    @Test
    public void testSupervisorSignup() throws ApiException {
        UserForm user = new UserForm();
        user.setEmail("shreyansh.mishra@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        UserData data = userDto.getAllUsers().get(0);
        assertEquals("supervisor", data.getRole());
        user.setEmail("shreyansh.mishra2@increff.com");
        user.setPassword("shreyansh");
        userDto.signup(user);
        data = userDto.getAllUsers().get(1);
        assertEquals("operator", data.getRole());
    }

}
