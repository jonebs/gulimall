package cn.ggb.gulimall.coupon.dao;

import cn.ggb.gulimall.coupon.entity.MemberPriceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-06 22:25:00
 */
@Mapper
public interface MemberPriceDao extends BaseMapper<MemberPriceEntity> {
	
}
