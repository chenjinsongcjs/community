package com.nowcoder.community.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/6:47
 * @Description: 分享功能，使用wkhtmltoimage 生成截图进行分享
 */
public interface ShareService {
    //生成图片返回图片的访问地址
    String GenerateImages(String imageUrl);
}
