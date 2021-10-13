package com.nowcoder.community.aliyunOSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/22:33
 * @Description: 阿里云网络流上传图片
 */
public class NetStreamUpload {
    @Value("${aliyun-oss.endpoint}")
    private String endpoint;
    @Value("${aliyun-oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun-oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun-oss.net-upload-bucket}")
    private String bucket;

    public void upload(String url,String fileName) throws IOException {
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "yourEndpoint";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 填写网络流地址。
        InputStream inputStream = new URL(url).openStream();
    // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        LocalDate date = LocalDate.now();
        String path = date.getYear()+"/"+date.getMonth()+"/"+date.getDayOfMonth()+"/";
        ossClient.putObject(bucket,path+fileName,inputStream);
    // 关闭OSSClient。
        ossClient.shutdown();
    }
}