package com.nowcoder.community.service.impl;

import cn.hutool.core.util.IdUtil;
import com.nowcoder.community.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/6:50
 * @Description:
 */
@Slf4j
@Service
public class ShareServiceImpl implements ShareService {
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.wkhtmltoimage.cmdpath}")
    private String cmdPath;
    @Value("${community.wkhtmltoimage.storepath}")
    private String storePath;

    @Override
    public String GenerateImages(String imageUrl) {
        String fileName = IdUtil.simpleUUID();
        //生成图片保存并返回访问地址
        String cmd = cmdPath +" --quality 75 "+ imageUrl + " "+storePath+fileName+".png";
        try {
            Runtime.getRuntime().exec(cmd);
            log.info("图片下载成功:{}",cmd);
        } catch (IOException e) {
            log.error("图片下载失败:{}",e.getMessage());
        }
        return domain+contextPath+"/share/image/"+fileName;
    }
}