package cn.ggb.gulimall.order.dao;

import cn.ggb.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:34:27
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
