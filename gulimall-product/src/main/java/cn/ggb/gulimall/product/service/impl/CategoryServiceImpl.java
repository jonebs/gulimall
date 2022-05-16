package cn.ggb.gulimall.product.service.impl;

import cn.ggb.gulimall.product.entity.CategoryBrandRelationEntity;
import cn.ggb.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.Query;

import cn.ggb.gulimall.product.dao.CategoryDao;
import cn.ggb.gulimall.product.entity.CategoryEntity;
import cn.ggb.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //获取所有的数据
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //获取所有的一级分类
        List<CategoryEntity> levelList = entities.stream().filter((categoryEntity) -> {
            //parentCid ==0 为父目录默认0
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            // 设置二三级分类 递归
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2) -> {
            //  排序 Sort字段排序
            return  (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return levelList;
    }

    /**
     * 逻辑删除
     * @param asList
     */
    @Override
    public void removeMenuById(List<Long> asList) {

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有相关数据
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());

    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;

    }

    /**
     *  递归查询子分类
     * @param root 当前category对象
     * @param all  全部分类数据
     * @return
     */
    public List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> collet = all.stream().filter((categoryEntity) ->{
            // 遍历所有的category对象的父类id = 等于root的分类id 说明是他的子类
            return categoryEntity.getParentCid() == root.getCatId();
        }).map((menu) -> {
            menu.setChildren(getChildrens(menu,all));
            return menu;
        }).sorted((menu1,menu2) ->{
            // 2、菜单排序
            return  (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return collet;
    }

}