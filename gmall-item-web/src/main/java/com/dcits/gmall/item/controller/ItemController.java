package com.dcits.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.api.bean.PmsProductSaleAttr;
import com.api.bean.PmsSkuInfo;
import com.api.bean.PmsSkuSaleAttrValue;
import com.api.service.SkuService;
import com.api.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

public class ItemController {


    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;


    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId,ModelMap map){

        PmsSkuInfo pmsSkuInfo =  skuService.getSkuById(skuId);
        map.put("skuInfo",pmsSkuInfo);

        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrList =  spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);

        //查询当前sku的spu的其他spu的集合的hash表


        Map<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfoList = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo:pmsSkuInfoList){
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue skuSaleAttrValue:pmsSkuSaleAttrValueList){
                k+=skuSaleAttrValue.getSaleAttrValueId()+"|";
            }

            skuSaleAttrHash.put(k,v);

        }
        //将sku的Hash表放在页面
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);


        return "item";
    }





    @RequestMapping("index")
    public String index(ModelMap modelMap){

        List<String> list = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            list.add("循环数据"+i);
        }

        modelMap.put("list",list);
        modelMap.put("hello","hello thymeleaf !!");

        modelMap.put("check","0");


        return "index";
    }


}
