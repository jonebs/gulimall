package cn.ggb.gulimall.member.dao;

import cn.ggb.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:21:59
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
