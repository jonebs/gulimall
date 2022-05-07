package cn.ggb.gulimall.order.dao;

import cn.ggb.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:34:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
