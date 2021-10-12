package com.nowcoder.community.controller;

import com.nowcoder.community.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/2:02
 * @Description:
 */
@Controller
@RequestMapping("/data")
public class StatisticalController {
    @Autowired
    private StatisticalService statisticalService;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    @PostMapping("/uv")
    public String getUV(String start, String end, Model model) throws ParseException {
        long intervalUV = statisticalService.intervalUV(format.parse(start),format.parse(end));
        model.addAttribute("uv",intervalUV);
        model.addAttribute("uvStart",start);
        model.addAttribute("uvEnd",end);
        return "site/admin/data";
    }
    @PostMapping("/dau")
    public String getDAU(String start,String end,Model model) throws ParseException {
        long intervalDAU = statisticalService.intervalDAU(format.parse(start), format.parse(end));
        model.addAttribute("dau",intervalDAU);
        model.addAttribute("dauStart",start);
        model.addAttribute("dauEnd",end);
        return "site/admin/data";
    }

}