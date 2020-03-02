package com.dcits.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.api.bean.PmsBaseAttrInfo;
import com.api.bean.PmsBaseAttrValue;
import com.api.bean.PmsBaseSaleAttr;
import com.api.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {


    @Reference
    AttrService attrService;

    @RequestMapping("/attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos =  attrService.attrInfoList(catalog3Id);

        return  pmsBaseAttrInfos;
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return success;
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){

        List<PmsBaseAttrValue> pmsBaseAttrValueList =   attrService.getAttrValueList(attrId);
        return pmsBaseAttrValueList;
    }

    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs =   attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }




}
