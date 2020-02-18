package com.dcits.gmall.user.service;

import com.dcits.gmall.user.bean.UmsMember;
import com.dcits.gmall.user.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId);
}
