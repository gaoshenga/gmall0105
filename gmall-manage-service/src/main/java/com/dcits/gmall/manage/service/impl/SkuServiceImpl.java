package com.dcits.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.bean.PmsSkuAttrValue;
import com.api.bean.PmsSkuImage;
import com.api.bean.PmsSkuInfo;
import com.api.bean.PmsSkuSaleAttrValue;
import com.api.service.SkuService;
import com.dcits.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.dcits.gmall.manage.mapper.PmsSkuImageMapper;
import com.dcits.gmall.manage.mapper.PmsSkuInfoMapper;
import com.dcits.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class SkuServiceImpl implements SkuService {


    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;


    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;


    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;


    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        //插入
        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();
        //插入平台属性关联

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        //插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue: skuSaleAttrValueList){
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        //插入图片属性

        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage:skuImageList){
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }


    }

    @Override
    public PmsSkuInfo getSkuById(String skuId) {

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);


        PmsSkuImage pmsSkuImage  = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);



        return skuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfoList =  pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfoList;
    }
}
