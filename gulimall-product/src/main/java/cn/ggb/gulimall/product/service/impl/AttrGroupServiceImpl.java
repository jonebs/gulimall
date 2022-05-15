package cn.ggb.gulimall.product.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
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


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        }else {
            String key = (String)params.get("key");
            // 先根据分类id进行查询
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>()
                    .eq("catelog_id",catelogId);
            if (!StringUtils.isEmpty(key)){
                wrapper.and((obj) ->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            // 组装条件进行查询
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }

    }
}