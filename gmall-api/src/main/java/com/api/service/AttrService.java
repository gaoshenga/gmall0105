package com.api.service;

import com.api.bean.PmsBaseAttrInfo;
import com.api.bean.PmsBaseAttrValue;
import com.api.bean.PmsBaseSaleAttr;

import java.util.List;

public interface AttrService {

    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
