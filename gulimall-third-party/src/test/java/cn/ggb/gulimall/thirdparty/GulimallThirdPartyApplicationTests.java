package cn.ggb.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testUpload() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 //https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI5tFECFcWdXkvQPa3f4tz";
        String accessKeySecret = "gPMhFaoRiv56yTEieDscpqIXaoEOJb";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\User\\Desktop\\100511711.jpg");
        ossClient.putObject("mall00", "01.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功");
    }

}
