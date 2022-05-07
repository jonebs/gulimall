package cn.ggb.gulimall.product.dao;

import cn.ggb.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-06 17:18:56
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
