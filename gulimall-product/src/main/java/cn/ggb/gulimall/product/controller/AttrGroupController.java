package cn.ggb.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.ggb.gulimall.product.entity.AttrEntity;
import cn.ggb.gulimall.product.service.AttrService;
import cn.ggb.gulimall.product.service.CategoryService;
import cn.ggb.gulimall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.ggb.gulimall.product.entity.AttrGroupEntity;
import cn.ggb.gulimall.product.service.AttrGroupService;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.R;



/**
 * 属性分组
 *
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-06 20:03:06
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    /**
     * 删除属性与分组的关联关系
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] relationVo){
        attrService.deleteRelation(relationVo);
        return R.ok();
    }

    /**
     * 获取属性分组的关联的所有属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entityList =attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entityList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);

        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }




    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
