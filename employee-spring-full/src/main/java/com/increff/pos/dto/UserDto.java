package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserDto {
    @Autowired
    private UserService service;

    @Autowired
    private InfoData info;

    @Value("${signup.email}")
    private String email;

    public ModelAndView signup(UserForm form) throws ApiException {
        UserPojo user = convert(form);
        if(user.getEmail().equals(email)){ user.setRole("supervisor"); }
        else{ user.setRole("operator"); }
        Boolean isSuccess = service.add(user);
        if(isSuccess){
            info.setMessage("Signup Successful!");
            return new ModelAndView("redirect:/site/login");
        }
        else{
            info.setMessage("The email is already registered!");
        }
        return new ModelAndView("redirect:/site/signup");
    }

    public List<UserData> getAllUsers() {
        return convertUsers(service.selectAll());
    }

    public List<UserData> convertUsers(List<UserPojo> user){
        List<UserData> data = new ArrayList<>();
        for(UserPojo u : user){
            UserData d = new UserData();
            d.setEmail(u.getEmail());
            d.setRole(u.getRole());
            d.setId(u.getId());
            data.add(d);
        }
        return data;
    }

    public UserPojo login(LoginForm form) throws ApiException {
        UserPojo user = service.select(form.getEmail());
        boolean authenticated = (user != null && Objects.equals(user.getPassword(), form.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return null;
        }
        info.setMessage("Login Successful!");
        return user;
    }

    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        info.setMessage("Logout Successful!");
        return new ModelAndView("redirect:/site/logout");
    }

    private static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setPassword(f.getPassword());
        return p;
    }
}
