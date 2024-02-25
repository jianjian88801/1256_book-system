package com.xunmaw.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xunmaw.book.entity.User;
import com.xunmaw.book.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping({"/"})
    public String portal() {
        return "login";
    }

    @RequestMapping("/index")
    public String index(int id, Model model, HttpServletRequest request){
        User user = userService.getById(id);
        model.addAttribute("user",user);
//        request.getSession().setAttribute("LoginSuccess",user);
        return user.getFlag() == 0 ? "admin_index" : "reader_index";
    }

    @RequestMapping({"/login"})
    public String login(User user, Model model,HttpServletRequest request) {
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper();
        queryWrapper1.eq(user.getUsername() != null, User::getUsername, user.getUsername());
        User user1 = this.userService.getOne(queryWrapper1);
        if (user1 != null) {
            if (user1.getPassword().equals(user.getPassword()) && user1.getFlag() == user.getFlag()) {
                model.addAttribute("user", user1);
                request.getSession().setAttribute("LoginSuccess",user1);
                return user.getFlag() == 0 ? "admin_index" : "reader_index";
            } else {
                model.addAttribute("msg","密码或身份错误！");
                return "forward:/";
            }
        } else {
            model.addAttribute("msg","账号不存在！");
            return "forward:/";
        }
    }

    //跳转到锁屏
    @RequestMapping("/toLock")
    public String toLock(int id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        return "lock";
    }

    //解锁
    @RequestMapping("/lock")
    public String lock(int id,String password,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        if (user.getPassword().equals(password))
            return user.getFlag() == 0 ? "admin_index" : "reader_index";
        else{
            model.addAttribute("msg","密码输入错误，请重试!");
            return "lock";
        }
    }
}
