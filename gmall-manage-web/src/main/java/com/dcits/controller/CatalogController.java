package com.dcits.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.api.bean.PmsBaseCatalog1;
import com.api.bean.PmsBaseCatalog2;
import com.api.bean.PmsBaseCatalog3;
import com.api.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class CatalogController {


    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1s = catalogService.getcatalog1();
        return  catalog1s;
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        List<PmsBaseCatalog2> catalog2s = catalogService.getcatalog2(catalog1Id);
        return  catalog2s;
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        List<PmsBaseCatalog3> catalog3s = catalogService.getcatalog3(catalog2Id);
        return  catalog3s;
    }

}
