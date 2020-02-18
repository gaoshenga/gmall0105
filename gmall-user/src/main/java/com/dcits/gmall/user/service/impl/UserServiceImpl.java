package com.dcits.gmall.user.service.impl;

import com.dcits.gmall.user.bean.UmsMember;
import com.dcits.gmall.user.bean.UmsMemberReceiveAddress;
import com.dcits.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.dcits.gmall.user.mapper.UserMapper;
import com.dcits.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.crypto.ExemptionMechanism;
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
