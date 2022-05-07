package cn.ggb.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.ggb.common.utils.PageUtils;
import cn.ggb.gulimall.member.entity.MemberCollectSpuEntity;

import java.util.Map;

/**
 * 会员收藏的商品
 *
 * @author GG-B
 * @email ggb@qq.com
 * @date 2022-05-07 12:21:59
 */
public interface MemberCollectSpuService extends IService<MemberCollectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

