package com.nowcoder.community.controller;

import com.nowcoder.community.constant.PageConstant;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/12/6:23
 * @Description:
 */
@Controller
public class SearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @GetMapping("/search")
    public String search(String key,
                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                         Model model){
        MyPage myPage = elasticSearchService.searchByPage(key, pageNum, PageConstant.MESSAGE_PAGE_SIZE);
        model.addAttribute("page",myPage);
        model.addAttribute("key",key);
        model.addAttribute("requestPath","/search?key="+key);
        return "site/search";
    }
}