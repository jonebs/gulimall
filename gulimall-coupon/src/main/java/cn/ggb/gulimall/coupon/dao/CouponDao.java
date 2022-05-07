package cn.ggb.gulimall.coupon.dao;

import cn.ggb.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-06 22:25:00
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
