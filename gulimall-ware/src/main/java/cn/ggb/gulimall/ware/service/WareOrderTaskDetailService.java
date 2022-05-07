package cn.ggb.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:57:26
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

