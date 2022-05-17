package cn.ggb.gulimall.product.vo;

import cn.ggb.gulimall.product.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrRespVo extends AttrVo {

    /**
     * catelogName 手机数码，所属分类名字
     * groupName 主体 所属分组名字
     */
    private String catelogName;
    private String groupName;

    private Long[] catelogPath;
}
