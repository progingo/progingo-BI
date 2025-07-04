package org.progingo.progingobi.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.progingo.progingobi.domain.entity.User;
import org.progingo.progingobi.service.UserService;
import org.progingo.progingobi.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/sign")
    public JsonResult sign(@RequestBody User user){
        return userService.sign(user);
    }

}
