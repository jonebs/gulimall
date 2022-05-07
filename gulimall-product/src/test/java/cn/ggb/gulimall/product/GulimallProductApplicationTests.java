package cn.ggb.gulimall.product;

import cn.ggb.gulimall.product.entity.BrandEntity;
import cn.ggb.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {

        BrandEntity brandEntity = new BrandEntity();

        brandEntity.setName("apple");

        brandService.save(brandEntity);
        System.out.println("保存成功。。。");
    }

}
