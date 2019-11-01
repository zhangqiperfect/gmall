package com.atguigu.gmall.sms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.sms.dao.SkuBoundsDao;
import com.atguigu.gmall.sms.dao.SkuFullReductionDao;
import com.atguigu.gmall.sms.dao.SkuLadderDao;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.atguigu.gmall.sms.vo.SaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {
    @Autowired
    private SkuFullReductionDao skuFullReductionDao;
    @Autowired
    private SkuLadderDao skuLadderDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void saveSale(SaleVo saleVo) {
    //新增积分
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        skuBoundsEntity.setBuyBounds(saleVo.getBuyBounds());
        skuBoundsEntity.setGrowBounds(saleVo.getGrowBounds());
        skuBoundsEntity.setSkuId(saleVo.getSkuId());
        List<Integer> works = saleVo.getWork();
        if (!CollectionUtils.isEmpty(works)&&works.size()==4){
            skuBoundsEntity.setWork(works.get(3)*1+works.get(2)*2+works.get(1)*4+works.get(0)*8);
        }
        this.save(skuBoundsEntity);
        //新增打折信息
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(saleVo.getSkuId());
        skuLadderEntity.setFullCount(saleVo.getFullCount());
        skuLadderEntity.setAddOther(saleVo.getLadderAddOther());
        skuLadderEntity.setDiscount(saleVo.getDiscount());
        this.skuLadderDao.insert(skuLadderEntity);
        //新增满减
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        skuFullReductionEntity.setAddOther(saleVo.getFullAddOther());
        skuFullReductionEntity.setSkuId(saleVo.getSkuId());
        skuFullReductionEntity.setFullPrice(saleVo.getFullPrice());
        skuFullReductionEntity.setReducePrice(saleVo.getReducePrice());
        this.skuFullReductionDao.insert(skuFullReductionEntity);
    }

}