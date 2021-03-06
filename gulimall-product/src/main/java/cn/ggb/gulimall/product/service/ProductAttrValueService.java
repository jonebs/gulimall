package cn.ggb.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-06 17:18:56
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveProductAttr(List<ProductAttrValueEntity> collect);

    List<ProductAttrValueEntity> baseAttrListforspu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntityList);
}

