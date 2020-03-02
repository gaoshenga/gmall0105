package com.dcits.gmall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.api.bean.*;
import com.api.service.AttrService;
import com.dcits.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.dcits.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.dcits.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        for(PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos){

            List<PmsBaseAttrValue>  pmsBaseAttrValueList = new ArrayList<>();
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseSaleAttr> pmsBaseSaleAttrs =  new ArrayList<>();
            pmsBaseAttrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValueList);

        }




        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        String id  = pmsBaseAttrInfo.getId();

        if(StringUtils.isBlank(id)){

            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            List<PmsBaseAttrValue> attrInfoList = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue pmsBaseAttrValue : attrInfoList){
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);

            }
        }else {

            Example e = new Example(PmsBaseAttrInfo.class);
            e.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,e);
            //属性值
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();

            //按照属性id删除所有属性值
            PmsBaseAttrValue pmsBaseAttrValueDe = new PmsBaseAttrValue();
            pmsBaseAttrValueDe.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDe);

            //删除后将新的属性值插入
            for (PmsBaseAttrValue pmsBaseAttrValue :attrValueList){
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> baseAttrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return baseAttrValueList;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.selectAll();
        return pmsBaseSaleAttrs;
    }


}
