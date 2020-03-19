package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {


    @Reference
    UserService userService;


    @RequestMapping("index")
    public String  index(){
        return "hello user";
    }

    @RequestMapping("getAllUser")
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMember  = userService.getAllUser();
        return umsMember;
    }


//    @RequestMapping("getUmsMemberReceiveAddressByMemberId")
//    public List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId){
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList  = userService.getUmsMemberReceiveAddressByMemberId(memberId);
//        return umsMemberReceiveAddressList;
//    }

}
