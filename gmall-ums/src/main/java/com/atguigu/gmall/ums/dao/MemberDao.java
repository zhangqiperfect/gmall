package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author ZQ
 * @email 2994182080@qq.com
 * @date 2019-10-28 23:34:21
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
