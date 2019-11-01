package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.*;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.service.SpuInfoService;
import com.atguigu.gmall.pms.vo.ProductAttrValueVo;
import com.atguigu.gmall.pms.vo.SaleVo;
import com.atguigu.gmall.pms.vo.SkuInfoVo;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDao spuInfoDao;
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private ProductAttrValueDao productAttrValueDao;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private SkuImagesDao skuImagesDao;
    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Autowired
    private GmallSmsClient gmallSmsClient;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfoByKey(Long catId, QueryCondition condition) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (catId != 0) {
            wrapper.eq("catalog_id", catId);
        }
        String key = condition.getKey();
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and(t -> t.eq("id", key).or().like("spu_name", key));
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(condition),
                wrapper
        );

        return new PageVo(page);
    }

    @Override
    public void bigsave(SpuInfoVo spuInfoVo) {
        //新增spu相关三张表
        //1.1新增spuInfo
        spuInfoVo.setCreateTime(new Date());
        spuInfoVo.setUodateTime(spuInfoVo.getCreateTime());
        this.save(spuInfoVo);
        Long spuId = spuInfoVo.getId();
        //1.2新增spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        List<String> spuImages = spuInfoVo.getSpuImages();
        spuInfoDescEntity.setDecript(StringUtils.join(spuImages, ","));
        this.spuInfoDescDao.insert(spuInfoDescEntity);
        //1.3新增product_att_value
        List<ProductAttrValueVo> baseAttrs = spuInfoVo.getBaseAttrs();
        baseAttrs.forEach(baseAttr -> {
            baseAttr.setSpuId(spuId);
            baseAttr.setQuickShow(0);
            baseAttr.setAttrSort(0);
            this.productAttrValueDao.insert(baseAttr);
        });


        //新增sku相关三张表  spuId

        List<SkuInfoVo> skuInfoVos = spuInfoVo.getSkus();
        if (CollectionUtils.isEmpty(skuInfoVos)) {
            return;
        }
        skuInfoVos.forEach(skuInfoVo -> {
            //  2.1新增skuInfo
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuInfoVo, skuInfoEntity);
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setCatalogId(spuInfoVo.getCatalogId());
            skuInfoEntity.setBrandId(spuInfoVo.getBrandId());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString());
            List<String> images = skuInfoVo.getImages();
            if (!CollectionUtils.isEmpty(images)) {
                //设置默认图片
                skuInfoEntity.setSkuDefaultImg(StringUtils.isNotEmpty(skuInfoEntity.getSkuDefaultImg()) ? skuInfoEntity.getSkuDefaultImg() : images.get(0));
            }
            this.skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();

            if (!CollectionUtils.isEmpty(images)) {
//        2.2新增skuImage
                images.forEach(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(StringUtils.equals(image, skuInfoEntity.getSkuDefaultImg()) ? 1 : 0);
                    skuImagesEntity.setImgSort(0);
                    skuImagesEntity.setImgUrl(image);
                    this.skuImagesDao.insert(skuImagesEntity);
                });

//        2.3新增sale_attr_value
                List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVo.getSaleAttrs();
                if (!CollectionUtils.isEmpty(saleAttrs)) {
                    saleAttrs.forEach(saleAttr -> {
                        saleAttr.setSkuId(skuId);
                        saleAttr.setAttrSort(0);
                        this.skuSaleAttrValueDao.insert(saleAttr);
                    });
                }


            }
            //新增营销相关
            SaleVo saleVo = new SaleVo();
            BeanUtils.copyProperties(skuInfoVo, saleVo);
            saleVo.setSkuId(skuId);
            this.gmallSmsClient.saveSale(saleVo);
        });

    }


}