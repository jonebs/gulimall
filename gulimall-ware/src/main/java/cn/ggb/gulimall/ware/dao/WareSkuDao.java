package cn.ggb.gulimall.ware.dao;

import cn.ggb.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * εεεΊε­
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:57:26
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId")Long wareId, @Param("skuNum")Integer skuNum);
}
