package com.nowcoder.community.aliyunOSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/22:54
 * @Description: 阿里云文件本地上传
 */
@Slf4j
@Component
public class LocalUpload {
    @Value("${aliyun-oss.endpoint}")
    private String endpoint;
    @Value("${aliyun-oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun-oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun-oss.net-upload-bucket}")
    private String bucket;
//    @Value("${community.path.uploadPath}")
//    private String uploadPath;
    //返回访问的url
    public String upload(String fileName,InputStream inputStream){
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "yourEndpoint";
//// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream("D:\\localpath\\examplefile.txt");
//        } catch (FileNotFoundException e) {
//            log.error("文件读取失败:{}",e.getMessage());
//        }
// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        LocalDate date = LocalDate.now();
        String path = date.getYear()+"/"+date.getMonth()+"/"+date.getDayOfMonth()+"/";
        ossClient.putObject(bucket, path+fileName, inputStream);

// 关闭OSSClient。
        ossClient.shutdown();
        return "http://"+bucket+"."+endpoint+"/"+path+fileName;
    }
}