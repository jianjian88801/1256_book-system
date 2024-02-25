package com.xunmaw.book.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xunmaw.book.entity.BookInfo;
import com.xunmaw.book.entity.LendList;
import com.xunmaw.book.entity.User;
import com.xunmaw.book.service.BookInfoService;
import com.xunmaw.book.service.LendListService;
import com.xunmaw.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/lend")
public class LendController {

    @Autowired
    private LendListService lendListService;
    @Autowired
    private BookInfoService bookInfoService;
    @Autowired
    private UserService userService;

    //查询所有未还书
    @RequestMapping("/selectSome")
    public String selectSome(int id,int current, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        List<LendList> lendList = lendListService.selectByReader(id);

        Page<LendList> page = new Page<>(current,5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %5 == 0 ? count/5 :count/5+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*5:0;

        Date date1=new Date();
        List<LendList> pageList = new ArrayList<>();
        for (int i=0; i<5 && i<count - currId;i++){
            Date date2 = lendList.get(currId+i).getBackDate();
            if (date1.compareTo(date2)==1){
                if (lendList.get(currId+i).getFlag()==0)
                lendListService.updateFlag(lendList.get(currId+i).getSerNum());
            }
            if (date1.compareTo(date2)<1){
                if (lendList.get(currId+i).getFlag()==2)
                lendListService.updateFlag2(lendList.get(currId+i).getSerNum());
            }

            pageList.add(lendList.get(currId+i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        return "html/reader/reader_return_books";
    }

    //查询所有已还书
    @RequestMapping("/selectAll")
    public String selectAll(int id,int current, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        List<LendList> lendList = lendListService.selectByReader2(id);
        int size = 10;
        Page<LendList> page = new Page<>(current,size);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %size == 0 ? count/size :count/size+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*size:0;

        List<LendList> pageList = new ArrayList<>();
        for (int i=0; i<size && i<count - currId;i++){
            pageList.add(lendList.get(currId+i));
        }
        page.setSize(size);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        return "html/reader/reader_lend_list";
    }

    //删除借还信息
    @RequestMapping("/deleteOne")
    public String deleteOne(int serNum,int id,int current,Model model){
        lendListService.removeById(serNum);
        User user = userService.getById(id);
        model.addAttribute("id",user.getId());
        model.addAttribute("current",current);
        return "forward:/lend/selectAll";
    }

    //图书借阅
    @RequestMapping("/addOne")
    public String addOne(int id,int bookId,int current,int classId,int flag,String name,Model model){
        //获取当前系统时间
        Date date1=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        //修改为30天后
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date date2 = calendar.getTime();

        //添加一条借阅信息
        LendList lendList = new LendList();
        lendList.setBookId(bookId);
        lendList.setReaderId(id);
        lendList.setLendDate(date1);
        lendList.setBackDate(date2);
        lendList.setFlag(0);
        lendList.setLossflag(0);
        lendListService.save(lendList);

        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber()-1;
        book.setNumber(number);
        bookInfoService.updateById(book);


        if (flag==0){
            System.out.println(classId);
            System.out.println(name);
            model.addAttribute("id",id);
            model.addAttribute("current",current);
            return "forward:/book/selectAll";
        }else{
            model.addAttribute("id",id);
            model.addAttribute("current",current);
            model.addAttribute("classId",classId);
            model.addAttribute("name",name);
            return "forward:/book/selectSome";
        }
    }

    //图书挂失页面
    @RequestMapping("/toUpdateOne")
    public String toUpdateOne(int id,int current,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        List<LendList> lendList = lendListService.selectByReader(id);

        Page<LendList> page = new Page<>(current,5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %5 == 0 ? count/5 :count/5+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*5:0;

        List<LendList> pageList = new ArrayList<>();

        Date date1=new Date();
        for (int i=0; i<5 && i<count - currId;i++){
            Date date2 = lendList.get(currId+i).getBackDate();
            if (date1.compareTo(date2)==1){
                if (lendList.get(currId+i).getFlag()==0)
                lendListService.updateFlag(lendList.get(currId+i).getSerNum());
            }
            if (date1.compareTo(date2)<1){
                if (lendList.get(currId+i).getFlag()==2)
                lendListService.updateFlag2(lendList.get(currId+i).getSerNum());
            }

            pageList.add(lendList.get(currId+i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        return "html/reader/reader_guashi_books";
    }

    //图书挂失操作
    @RequestMapping("/updateOne")
    public String updateOne(int id,int current,int serNum,Model model){
        model.addAttribute("current",current);
        model.addAttribute("id",id);
        Date date=new Date();
        lendListService.update1(serNum,date);
        return "forward:/lend/toUpdateOne";
    }

    //图书解挂页面
    @RequestMapping("/toUpdateTwo")
    public String toUpdateTwo(int id,int current,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        List<LendList> lendList = lendListService.selectByReader3(id);

        Page<LendList> page = new Page<>(current,5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %5 == 0 ? count/5 :count/5+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*5:0;

        List<LendList> pageList = new ArrayList<>();
        for (int i=0; i<5 && i<count - currId;i++){
            pageList.add(lendList.get(currId+i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        return "html/reader/reader_jiegua_books";
    }

    //图书解挂操作
    @RequestMapping("/updateTwo")
    public String updateTwo(int id,int current,int serNum,Model model){
        model.addAttribute("current",current);
        model.addAttribute("id",id);

        lendListService.update2(serNum);
        return "forward:/lend/toUpdateTwo";
    }

    //图书赔偿操作
    @RequestMapping("/updateThree")
    public String updateThree(int id,int current,int serNum,int bookId,Model model){
        model.addAttribute("current",current);
        model.addAttribute("id",id);
        Date date=new Date();
        lendListService.update3(serNum,date);
        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber()+1;
        book.setNumber(number);
        bookInfoService.updateById(book);

        return "forward:/lend/toUpdateTwo";
    }

    //图书归还操作
    @RequestMapping("/updateFour")
    public String updateFour(int id,int current,int serNum,int bookId,Model model){
        model.addAttribute("current",current);
        model.addAttribute("id",id);
        Date date=new Date();
        lendListService.update4(serNum,date);
        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber()+1;
        book.setNumber(number);
        bookInfoService.updateById(book);

        return "forward:/lend/selectSome";
    }

    //图书续借操作
    @RequestMapping("/updateFive")
    public String updateFive(int id,int current,int serNum,Model model){
        model.addAttribute("current",current);
        model.addAttribute("id",id);
        //获取当前的归还时间
        Date date = lendListService.getDate(serNum);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //修改为30天后
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date date1 = calendar.getTime();

        lendListService.updateDate(serNum,date1);

        return "forward:/lend/selectSome";
    }

    //管理员的查询全部借还信息
    @RequestMapping("/select1")
    public String select1(int id,int current,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        List<LendList> lendList = lendListService.select1();

        int size = 10;
        Page<LendList> page = new Page<>(current,size);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %size == 0 ? count/size :count/size+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*size:0;

        List<LendList> pageList = new ArrayList<>();
        for (int i=0; i<size && i<count - currId;i++){
            pageList.add(lendList.get(currId+i));
        }
        page.setSize(size);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        model.addAttribute("z",0);
        return "html/admin/admin_lend_list";
    }

    //管理员的查询全部借还信息
    @RequestMapping("/select2")
    public String select2(int id,int current,int x,String y,Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        model.addAttribute("x",x);
        model.addAttribute("y",y);

        List<LendList> lendList = new ArrayList<>();

        if (x!=4&&y.isEmpty()){
            lendList = lendListService.select2(x);
        }
        else if (x==4&&!y.isEmpty()){
            lendList = lendListService.select3(y);
        }
        else if (x!=4&&!y.isEmpty()){
            lendList = lendListService.select4(x,y);
        }

        Page<LendList> page = new Page<>(current,5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages=count %5 == 0 ? count/5 :count/5+1;
        //让页码保持在合理范围
        if (current==0) current++;
        if (current>pages) current--;
        //计算当前页第一条数据的下标
        int currId = current>1 ? (current-1)*5:0;

        List<LendList> pageList = new ArrayList<>();
        for (int i=0; i<5 && i<count - currId;i++){
            pageList.add(lendList.get(currId+i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page",page);
        model.addAttribute("z",1);

        return "html/admin/admin_lend_list";
    }

    //催还操作
    @RequestMapping("/update1")
    public String update1(int id,int current,int serNum,Model model){
        model.addAttribute("id",id);
        model.addAttribute("current",current);

        lendListService.updateOne(serNum);

        return "forward:/lend/select1";
    }

    //删除操作
    @RequestMapping("/delete")
    public String delete(int id,int current,int serNum,Model model){
        model.addAttribute("id",id);
        model.addAttribute("current",current);

        lendListService.removeById(serNum);

        return "forward:/lend/select1";
    }
}
