package com.xunmaw.book.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xunmaw.book.entity.User;
import com.xunmaw.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/user"})
public class UserController {
    @Autowired
    private UserService userService;

    //个人信息查询
    @RequestMapping({"/me"})
    public String me(int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "html/reader/reader_info";
    }

    //带参数跳转至个人信息修改页面
    @RequestMapping({"/toUpdate"})
    public String toUpdate(int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "html/reader/reader_info_edit";
    }

    //个人信息修改
    @PostMapping({"/update"})
    public String update(User u, Model model) {
        this.userService.updateById(u);
        User user = (User)this.userService.getById(u.getId());
        model.addAttribute("user", user);
        return "html/reader/reader_info";
    }

    //带id跳转至修改密码页面
    @RequestMapping("/toPwd")
    public String toPwd(int id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        if (user.getFlag()==0) return "html/admin/repassword";
        else return "html/reader/reader_repasswd";
    }

    //密码修改
    @PostMapping("/rePwd")
    public String rePwd(int id,String oldPasswd,String newPasswd,String reNewPasswd,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        if (user.getPassword().equals(oldPasswd)){
            if (newPasswd.equals(reNewPasswd)){
                user.setPassword(newPasswd);
                userService.updateById(user);
                model.addAttribute("msg","密码修改成功，请重新登录！");
                return "forward:/";
            }else{
                model.addAttribute("msg","提示:两次输入的新密码不同，请检查！");
                if (user.getFlag()==0) return "html/admin/repassword";
                else return "html/reader/reader_repasswd";
            }
        }else{
            model.addAttribute("msg","提示:旧密码输入错误，请重试！");
            if (user.getFlag()==0) return "html/admin/repassword";
            else return "html/reader/reader_repasswd";
        }
    }

    //跳转到添加用户界面
    @RequestMapping("/toAddUser")
    public String toAddUser(int id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        User user1 = new User();
        model.addAttribute("user1",user1);
        return "html/admin/admin_user_add";
    }

    //添加用户
    @RequestMapping("/addUser")
    public String addUser(int uid,User user,Model model){
        User user1 = userService.getById(uid);
        model.addAttribute("user",user1);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User one = userService.getOne(queryWrapper);
        if (one==null){
            userService.save(user);
            User user2 = new User();
            model.addAttribute("user1",user2);
            model.addAttribute("msg","提示:添加成功！");
        }else{
            model.addAttribute("user1",user);
            model.addAttribute("msg","提示:用户名重复,添加失败！");
        }
        return "html/admin/admin_user_add";
    }

    //查询所有用户
    @RequestMapping("/selectAll")
    public String selectAll(int uid,int current,Model model){
        User user = userService.getById(uid);
        model.addAttribute("user",user);

        Page<User> page = new Page<>(current,10);
        userService.page(page,null);

        model.addAttribute("page",page);
        model.addAttribute("x",0);

        return "html/admin/admin_user";
    }

    //条件查询用户
    @RequestMapping("/selectSome")
    public String selectSome(int id,int current,int flag,String name,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        model.addAttribute("flag",flag);
        model.addAttribute("name",name);

        QueryWrapper queryWrapper = new QueryWrapper<>();
        if (flag!=2) queryWrapper.eq("flag",flag);
        if (!name.isEmpty()) queryWrapper.eq("name",name);
        Page<User> page = new Page<>(current,5);
        userService.page(page,queryWrapper);

        model.addAttribute("page",page);
        model.addAttribute("x",1);

        return "html/admin/admin_user";
    }

    //跳转到用户修改页面
    @RequestMapping("/toUpdateUser")
    public String toUpdateUser(int uid, int id,int current,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        model.addAttribute("current",current);
        User user1 = userService.getById(uid);
        model.addAttribute("user1",user1);
        return "html/admin/admin_user_edit";
    }

    //修改用户
    @RequestMapping("/updateUser")
    public String updateUser(int uid,int current,User user,Model model){
        if (uid!=user.getId()){
            userService.updateById(user);
        }else{
            model.addAttribute("msg","提示:不能修改自己！");
        }

        model.addAttribute("uid",uid);
        model.addAttribute("current",current);

        return "forward:/user/selectAll";
    }

    //删除用户
    @RequestMapping("deleteUser")
    public String deleteUser(int uid,int id,int current,Model model){
        model.addAttribute("id",id);
        model.addAttribute("current",current);

        if (id!=uid){
            userService.removeById(uid);
            model.addAttribute("msg","提示:删除成功！");
        }else{
            model.addAttribute("msg","提示:不能删除自己！");
        }

        return "forward:/user/selectAll";
    }
}
