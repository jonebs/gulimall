package cn.ggb.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.Query;

import cn.ggb.gulimall.ware.dao.PurchaseDetailDao;
import cn.ggb.gulimall.ware.entity.PurchaseDetailEntity;
import cn.ggb.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();

        // 取出key
        String key = (String) params.get("key");
        // key 主要查询条件
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) ->{
                w.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.and((w) ->{
                w.eq("status",status);
            });
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.and((w) ->{
                w.eq("ware_id",wareId);
            });
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}