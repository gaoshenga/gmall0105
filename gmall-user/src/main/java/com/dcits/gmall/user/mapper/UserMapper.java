package com.dcits.gmall.user.mapper;

import com.dcits.gmall.user.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UmsMember> {


    List<UmsMember> selectAllUser();
}