package com.api.service;

import com.api.bean.PmsBaseCatalog1;
import com.api.bean.PmsBaseCatalog2;
import com.api.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> getcatalog1();


    List<PmsBaseCatalog2> getcatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getcatalog3(String catalog2Id);
}
