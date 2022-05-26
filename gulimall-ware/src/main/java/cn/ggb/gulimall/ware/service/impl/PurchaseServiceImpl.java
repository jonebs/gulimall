package cn.ggb.gulimall.ware.service.impl;

import cn.ggb.common.constant.WareConstant;
import cn.ggb.gulimall.ware.entity.PurchaseDetailEntity;
import cn.ggb.gulimall.ware.service.PurchaseDetailService;
import cn.ggb.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.common.utils.Query;

import cn.ggb.gulimall.ware.dao.PurchaseDao;
import cn.ggb.gulimall.ware.entity.PurchaseEntity;
import cn.ggb.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * @param mergeVo
     */
    @Transactional
    @Override
    public void mrgePurchase(MergeVo mergeVo) {
        // 拿到采购单id
        Long purchaseId = mergeVo.getPurchaseId();
        // 采购单 id为空 新建
        if (purchaseId == null ) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            // 状态设置为新建
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            // 拿到最新的采购单id
            purchaseId = purchaseEntity.getId();
        }
        //TODO 确认采购是 0 或 1 才可以合并

        // 拿到合并项 **采购需求的id**
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            // 采购需求
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();

            // 通过采购单id 查询到 采购信息对象
            PurchaseEntity byId = this.getById(finalPurchaseId);
            // 状态如果是正在采购
            if ( !(byId.getStatus() == WareConstant.PurchaseDetailStatusEnum.BUYING.getCode())) {
                // 设置为已分配
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            }

            detailEntity.setId(i);
            // 设置采购单id
            detailEntity.setPurchaseId(finalPurchaseId);

            return detailEntity;
        }).collect(Collectors.toList());

        // id批量更新
        purchaseDetailService.updateBatchById(collect);

        // 再次合并的话 更新修改时间
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

}