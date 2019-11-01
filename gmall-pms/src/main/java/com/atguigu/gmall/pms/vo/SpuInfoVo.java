package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZQ
 * @create 2019-10-31 11:47
 */
@Data
public class SpuInfoVo extends SpuInfoEntity {
    private List<String> spuImages;
    private List<ProductAttrValueVo> baseAttrs;
    private List<SkuInfoVo> skus;
}
