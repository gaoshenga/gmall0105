package com.dcits.gmall.user.controller;


import com.dcits.gmall.user.bean.UmsMember;
import com.dcits.gmall.user.bean.UmsMemberReceiveAddress;
import com.dcits.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
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


    @RequestMapping("getUmsMemberReceiveAddressByMemberId")
    public List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList  = userService.getUmsMemberReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddressList;
    }

}