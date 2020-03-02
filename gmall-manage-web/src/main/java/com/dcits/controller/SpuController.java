package com.dcits.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.api.bean.PmsProductImage;
import com.api.bean.PmsProductInfo;
import com.api.bean.PmsProductSaleAttr;
import com.api.service.SpuService;
import com.dcits.util.PmsUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {


    @Reference
    SpuService spuService;


    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){


        List<PmsProductInfo>  pmsProductInfoList =  spuService.spuList(catalog3Id);

        return pmsProductInfoList;
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);


        return "success";
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrList =  spuService.spuService(spuId);
        return pmsProductSaleAttrList;
    }


    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> pmsProductImageList =  spuService.spuImageList(spuId);
        return pmsProductImageList;
    }


}
