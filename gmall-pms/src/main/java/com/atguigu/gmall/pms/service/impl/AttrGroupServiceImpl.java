package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.AttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querygroups(long catid, QueryCondition condition) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (catid != 0) {
            wrapper.eq("catelog_id", catid);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(condition),
                wrapper
        );

        return new PageVo(page);
    }

    @Override
    public AttrGroupVo queryByGid(long gid) {
        AttrGroupVo attrGroupVo = new AttrGroupVo();
        //1、先查询分组
        AttrGroupEntity groupEntity = this.getById(gid);
        BeanUtils.copyProperties(groupEntity, attrGroupVo);

        //2、根据分组id查询关联关系
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id", gid);
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = this.attrAttrgroupRelationDao.selectList(wrapper);
        if (CollectionUtils.isEmpty(attrAttrgroupRelationEntities)) {
            return attrGroupVo;
        }
        attrGroupVo.setRelations(attrAttrgroupRelationEntities);
        //3、根据关联关系attrid查询属性
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(relation -> relation.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> attrEntities = this.attrDao.selectBatchIds(attrIds);
        attrGroupVo.setAttrEntities(attrEntities);
        return attrGroupVo;
    }

    @Override
    public List<AttrGroupVo> queryGroupAndAttrByCid(Long catId) {
        //根据分类catid查询分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        //根据分组查询所有属性
        List<AttrGroupVo> attrGroupVos = attrGroupEntities.stream().map(attrGroupEntity -> this.queryByGid(attrGroupEntity.getAttrGroupId())
        ).collect(Collectors.toList());

        return attrGroupVos;
    }


}