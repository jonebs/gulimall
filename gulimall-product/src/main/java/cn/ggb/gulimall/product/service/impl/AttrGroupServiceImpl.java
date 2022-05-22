package cn.ggb.gulimall.product.service.impl;

import cn.ggb.gulimall.product.entity.AttrEntity;
import cn.ggb.gulimall.product.service.AttrService;
import cn.ggb.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.Query;

import cn.ggb.gulimall.product.dao.AttrGroupDao;
import cn.ggb.gulimall.product.entity.AttrGroupEntity;
import cn.ggb.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        String key = (String)params.get("key");
        // 先根据分类id进行查询
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();

        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj) ->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if (catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id",catelogId);
            // 组装条件进行查询
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }

    }

    /**
     *  获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //查询分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrsVo);
            //通过分组id查询出 商品属性信息
            // 调用 getRelationAttr方法先根据 分组id去 中间关系表查询到商品属性id 然后根据商品属性id查询到商品信息
            List<AttrEntity> relationAttr = attrService.getRelationAttr(item.getAttrGroupId());
            attrsVo.setAttrs(relationAttr);

            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }
}