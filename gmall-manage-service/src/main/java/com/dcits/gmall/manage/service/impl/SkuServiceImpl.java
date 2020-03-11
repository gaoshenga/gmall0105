package com.dcits.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.api.bean.PmsSkuAttrValue;
import com.api.bean.PmsSkuImage;
import com.api.bean.PmsSkuInfo;
import com.api.bean.PmsSkuSaleAttrValue;
import com.api.service.SkuService;
import com.dcits.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.dcits.gmall.manage.mapper.PmsSkuImageMapper;
import com.dcits.gmall.manage.mapper.PmsSkuInfoMapper;
import com.dcits.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.dcits.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;


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

    @Autowired
    RedisUtil redisUtil;


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
    public PmsSkuInfo getSkuById(String skuId,String ip) {

        System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"进入商品详情请求。");
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        //链接缓存
        Jedis jedis = redisUtil.getJedis();
        //查询缓存
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);
        //直接将json的字符串转换成java类
        if(StringUtils.isNotBlank(skuJson)){
            System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"从缓存中获取商品。");
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else {
            System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"发现缓存中没有，申请缓存的分布式锁。"+"sku:" + skuId + ":lock");

            //如果缓存中没有再去查询mysql
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10*1000);//设置分布式锁
            if (StringUtils.isNotBlank(OK)&&OK.equals("OK")){
                //设置成功在十秒的过期时间内访问数据库
                System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"成功拿到分布式锁，有权在十秒钟访问数据库。"+"sku:" + skuId + ":lock");
                pmsSkuInfo  = getSkuByIdFromDb(skuId);
                //加上睡眠时间
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(pmsSkuInfo!=null){
                    //mysql查询的结果存入缓存
                    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
                }else{
                    //数据库中不存在
                    //为了防止缓存穿透
                    jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(pmsSkuInfo));
                }
                //在访问mysql后，将分布式锁归还
                System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"使用完毕，将锁归还。"+"sku:" + skuId + ":lock");
                String lockToken = "sku:" + skuId + ":lock";
                if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
                    jedis.del("sku:" + skuId + ":lock");//用token删除确认自己的sku的锁
                }
            }else{
                //设置失败 自旋（该线程在睡眠几秒时间后，重新尝试访问本方法）
                System.out.println("ip："+ip+"的同学"+Thread.currentThread().getName()+"没有拿到分布式锁，开始自旋。"+"sku:" + skuId + ":lock");
                return  getSkuById(skuId,ip);
            }
        }
        jedis.close();
        return pmsSkuInfo;
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId) {

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

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfoList;
    }
}
