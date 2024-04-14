package com.shihHsin.controller;

import com.shihHsin.common.R;
import com.shihHsin.pojo.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shihHsin.service.ITestService;

import java.util.List;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    public ITestService testService;

    //    @RequestMapping(name="/error")
//    public String handleError() {
//        // Handle error logic here
//        return "error"; // Return the name of your error page template
//    }
    @RequestMapping("/hello")
    public String Hello() {
        return "Hello world!";
    }

    @RequestMapping("/test")
    public String test() {
        log.debug("debugAAsadas");
        List<User> userList = testService.getUsers();
        log.debug("userList" + userList.toString());
        return userList.toString();
    }

//    @RequestMapping("/list")
//    public R get() {
//        log.debug("debugAAsadas");
//
//        return R.success();
//    }

//    @GetMapping("/list")
//    public R getList(){
//        List<ShoppingCart> list = shoppingCartService.getList();
//        return R.success(list);
//    }

}
