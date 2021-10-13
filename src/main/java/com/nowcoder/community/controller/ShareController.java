package com.nowcoder.community.controller;

import com.nowcoder.community.service.ShareService;
import com.nowcoder.community.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/7:15
 * @Description:
 */
@Slf4j
@Controller
public class ShareController {
    @Autowired
    private ShareService service;
    @Value("${community.wkhtmltoimage.storepath}")
    private String storePath;
    /**
    * @Description: 分享功能生成长截图，这个过程可以交给kafka消息队列进行异步处理，避免阻塞
    * @Param: [imageUrl]
    * @return: [java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/14
    */
    @GetMapping("/share")
    @ResponseBody
    public String share(String imageUrl){
        if (imageUrl == null){
            log.error("截屏地址不能为空");
            throw new RuntimeException("截屏地址不能为空");
        }

        String path = service.GenerateImages(imageUrl);
        Map<String,Object> map = new HashMap<>();
        map.put("imagePath",path);
        return JSONUtils.getJSONString(0,"截图成功",map);
    }
    //从本地获取图片
    @GetMapping("/share/image/{fileName}")
    public void getImage(@PathVariable("fileName") String fileName,
                           HttpServletResponse response){
        if (fileName == null){
            log.error("图片名不能为空");
            throw new RuntimeException("图片名不能为空");
        }
        try (
                InputStream inputStream =  new FileInputStream(storePath+ fileName+".png");
                OutputStream outputStream = response.getOutputStream();
        ){
            response.setContentType("image/png");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read()) != -1){
                outputStream.write(buffer,0,len);
            }
        } catch (Exception e) {
            log.error("图片读取失败:{}",e.getMessage());
            e.printStackTrace();
        }
    }
}