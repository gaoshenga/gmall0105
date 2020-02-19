package com.dcits.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.bean.UmsMember;
import com.api.bean.UmsMemberReceiveAddress;
import com.api.service.UserService;

import com.dcits.mapper.UmsMemberReceiveAddressMapper;
import com.dcits.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Override
    public List<UmsMember> getAllUser() {
//        List<UmsMember> umsMemberList = userMapper.selectAllUser();

        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId) {

//        Example e = new Example(UmsMemberReceiveAddress.class);
//        e.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = umsMemberReceiveAddressMapper.selectByExample(e);
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);

        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);


        return umsMemberReceiveAddressList;
    }
}
