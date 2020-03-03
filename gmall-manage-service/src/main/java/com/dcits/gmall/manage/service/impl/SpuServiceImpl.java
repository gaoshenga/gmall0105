package com.dcits.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.bean.*;
import com.api.service.SpuService;
import com.dcits.gmall.manage.mapper.PmsProductImageMapper;
import com.dcits.gmall.manage.mapper.PmsProductInfoMapper;
import com.dcits.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.dcits.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SpuServiceImpl  implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> productInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return productInfos;
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        //生成商品的住键
        String productId = pmsProductInfo.getId();

        //保存商品图片信息
        List<PmsProductImage> pmsProductImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage:pmsProductImageList){
            pmsProductImage.setProductId(productId);
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        //保存商品属性值
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValueList){
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);

            }

        }
    }

    @Override
    public List<PmsProductSaleAttr> spuService(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> PmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : PmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());// 销售属性id用的是系统的字典表中id，不是销售属性表的主键
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }

        return PmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {

        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {


//        PmsProductSaleAttr productSaleAttr = new PmsProductSaleAttr();
//        productSaleAttr.setProductId(productId);
//        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(productSaleAttr);
//        for(PmsProductSaleAttr pmsProductSaleAttr: pmsProductSaleAttrList){
//            String saleAttrId = pmsProductSaleAttr.getSaleAttrId();
//
//            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
//            pmsProductSaleAttrValue.setProductId(productId);
//            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//            pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
//        }


        List<PmsProductSaleAttr> pmsProductSaleAttrList =  pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrList;
    }
}
