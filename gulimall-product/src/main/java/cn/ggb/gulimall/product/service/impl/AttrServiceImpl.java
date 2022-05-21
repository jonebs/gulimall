package cn.ggb.gulimall.product.service.impl;

import cn.ggb.common.constant.ProductConstant;
import cn.ggb.gulimall.product.dao.AttrAttrgroupRelationDao;
import cn.ggb.gulimall.product.dao.AttrGroupDao;
import cn.ggb.gulimall.product.dao.CategoryDao;
import cn.ggb.gulimall.product.entity.AttrAttrgroupRelationEntity;
import cn.ggb.gulimall.product.entity.AttrGroupEntity;
import cn.ggb.gulimall.product.entity.CategoryEntity;
import cn.ggb.gulimall.product.service.CategoryService;
import cn.ggb.gulimall.product.vo.AttrGroupRelationVo;
import cn.ggb.gulimall.product.vo.AttrRespVo;
import cn.ggb.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.Query;

import cn.ggb.gulimall.product.dao.AttrDao;
import cn.ggb.gulimall.product.entity.AttrEntity;
import cn.ggb.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        //保存基本数据
        this.save(attrEntity);
        //保存关联关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type","base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId != 0){
            wrapper.eq("catelog_Id",catelogId);
        }

        String key = (String)params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj) ->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });

        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),wrapper);

        PageUtils pageUtils = new PageUtils(page);
        // 拿到全部的分页记录
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVo = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity,attrRespVo);

            if ("base".equalsIgnoreCase(type)){
                // 1、根据attr_id 查询到 attr和 attrGroup的关系表
                AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectById(attrEntity.getAttrId());
                if (relationEntity != null && relationEntity.getAttrGroupId() != null){
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            // 根据attrEntity的分类id 查询到分类对象并设置分类姓名
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null){
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVo);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVo);



        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1、设置分组信息
            AttrAttrgroupRelationEntity attrgroupRelation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if(attrgroupRelation!=null){
                respVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
                if(attrGroupEntity!=null){
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }


        //2、设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if(categoryEntity!=null){
            respVo.setCatelogName(categoryEntity.getName());
        }


        return respVo;
    }
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //修改分组关联
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());
            Long count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count > 0){
                attrAttrgroupRelationDao.update(relationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
            }else {
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }

    }

    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        // 从关系表中 取到 属性id
        List<Long> attrIds = entities.stream().map((attr) ->{
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        // 按照属性id进行查询
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 删除属性与分组的关联关系
     * @return
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] relationVo) {
        // 转成 list 进行stream流处理
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(relationVo).stream().map((item) -> {
            // 将 item对应属性拷贝到 relationEntity对象中
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        // id批量删除
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
     * 查询分组未关联属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // 1、当前分组只能关联自己所属分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1 当前分类下的 **其他分组** 根据分类id进行查询
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 拿到分组id
        List<Long> collect = group.stream().map((groupEntity) -> {
            return groupEntity.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2 这些分组关联的属性  根据分组id查询出关联表
        List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        // 拿到所有的属性id
        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //查询需要查询的属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        //在pms_attr_attrgroup_relation表中的attrId不需要查询
        // attrIds 属性id数组不为空
        if (attrIds != null && attrIds.size() > 0) {
            // 在attrids 数组中得id不用进行查询
            wrapper.notIn("attr_id", attrIds);
        }

        //模糊查询
        String key = (String)params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj) ->{
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

}