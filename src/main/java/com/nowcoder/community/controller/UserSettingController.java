package com.nowcoder.community.controller;

import cn.hutool.core.util.IdUtil;
import com.nowcoder.community.aliyunOSS.LocalUpload;
import com.nowcoder.community.annotation.LoginCheck;
import com.nowcoder.community.domain.User;
import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.service.UserSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/2:18
 * @Description: 用户账号信息修改控制器
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserSettingController {

    @Value("${community.path.uploadPath}")
    private String uploadPath;
    @Autowired
    private UserSettingService userSettingService;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    //使用阿里云上传
    @Autowired
    private LocalUpload localUpload ;

    @GetMapping("/setting")
    @LoginCheck
    public String toSetting(){
        return "/site/setting";
    }


    /**
    * @Description: 文件上传一定为post请求
    * @Param: []
    * @return: []
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    @PostMapping("/upload")
    @LoginCheck //登录检查，没有登录不能进行文件上传
    public String upload(MultipartFile headerImage, Model model){
        //将上传的文件存放在本地
        if(headerImage.isEmpty()){
            model.addAttribute("error","您还没有选择需要上传的头像！！");
            return "/site/setting";
        }
        //对文件名进行修改，避免文件名的重复导致上传失败
        String filename = headerImage.getOriginalFilename();
        //获取文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = IdUtil.simpleUUID()+suffix;

        try {
            String accessPath = localUpload.upload(filename, headerImage.getInputStream());
            User user = LoginInterceptor.users.get();
            userSettingService.updateUserHeaderImage(user.getId(), accessPath);
            log.info("文件上传成功");
        } catch (IOException e) {
            log.error("文件上传失败：{}",e.getMessage());
        }

//        //定义文件上传位置
//        try {
//            headerImage.transferTo(new File(uploadPath+filename));
//            //修改用户的头像地址，必须为web地址
//            User user = LoginInterceptor.users.get();
//            if (user == null){
//                throw new RuntimeException("用户还未登录");
//            }
//            String imageUrl = domain + contextPath + "/user/header/"+filename;
//            int i = userSettingService.updateUserHeaderImage(user.getId(), imageUrl);
//            if(i == 0){
//                throw new RuntimeException("用户头像地址修改失败");
//            }
//            log.info("头像上传成功");
//            return "redirect:/index";//修改成功返回首页刷新
//        } catch (IOException e) {
//            log.info("文件上传失败,服务器异常:{}",e.getMessage());
//            throw new MultipartException("文件上传失败");
//        }
        return "redirect:/index";
    }
    /**
    * @Description: 获取用户头像,读取用户的头像地址，将来可以用OSS对象存储
    * @Param: []
    * @return: []
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    @Deprecated
    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename,
                          HttpServletResponse response){
        //从本地读取图片写到模板上
        if(StringUtils.isEmpty(filename)){
            throw new RuntimeException("文件名不对");
        }
        //获取文件的后缀名用于拼接响应数据类型
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+suffix);
        //从本地读取文件
        try(    //会自动在finally中关闭
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(uploadPath + filename));
                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

        ) {
            int len = 0;
            while((len = inputStream.read()) != -1){
                outputStream.write(len);
            }
            log.info("头像读取成功");
        } catch (Exception e) {
            log.info("头像读取失败：{}",e.getMessage());
            //头像读取失败影响不大
        }
    }

    @PostMapping("/modifyPassword")
    @LoginCheck
    public String modifyPassword(String oldPassword, String newPassword,
                                 Model model,
                                 RedirectAttributes attributes){
        //修改密码
        /**
         * 1.校验原始密码
         * 2.校验通过，修改原始密码
         * 3.修改失败返回修改页面提示错误信息
         * 4.修改成功，跳转登录页面
         */
        User user = LoginInterceptor.users.get();
        if(user == null){
            //用户未登录，跳转登录页面
            attributes.addAttribute("userError","用户未登录");
            return "redirect:/toLogin";
        }
        Map<String, String> map = userSettingService.modifyPassword(user.getId(), oldPassword.trim(), newPassword.trim());
        if (map == null){
            //密码修改成功跳转登录页面,可以提示修改成功
//            attributes.addAttribute("success","密码修改成功");
            return "redirect:/toLogin";
        }
        model.addAttribute("passwordMsg",map.get("passwordMsg"));
        model.addAttribute("newPasswordMsg",map.get("newPasswordMsg"));
        return "/site/setting";
    }

}