package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ZQ
 * @email 2994182080@qq.com
 * @date 2019-10-28 23:23:31
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
