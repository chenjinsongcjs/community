package com.nowcoder.community.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/04/22:11
 * @Description: 敏感词过滤器
 * 1.定义前缀树
 * 2.将敏感词放入前缀树中
 * 3.编写敏感词过滤方法
 */
@Component
@Slf4j
public class SensitiveWordsFilter {
    //前缀树的结构
    private class Node{
        boolean isWord;
        TreeMap<Character,Node> next;
        public Node(){
            this.isWord = false;
            this.next = new TreeMap<>();
        }
    }
    private Node root;
    public SensitiveWordsFilter(){
        root = new Node();
    }
    //初始化敏感词前缀树,在构造结束之后进行属性赋值
    @PostConstruct
    private void init(){
        //读取敏感词文件，
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                ){

            String str = "";
            while((str = reader.readLine()) != null){
                addWord(str);
            }
        } catch (IOException e) {
            log.error("敏感词前缀树初始化失败:{}",e.getMessage());
        }
    }
    //向前缀树中添加元素
    private void addWord(String word){
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(cur.next.get(c) == null){
                cur.next.put(c,new Node());
            }
            cur = cur.next.get(c);
        }
        if(!cur.isWord)
            cur.isWord = true;
    }
    /**
    * @Description: 过滤敏感词
    * @Param: [text]
    * @return: [java.lang.String]
    * @Author: 陈进松
    * @Date: 2021/10/4
    */
    public String filter(String text){
        if(StringUtils.isEmpty(text))
            return "";
        //用于存储过滤后的文本
        StringBuilder sb = new StringBuilder();
        String replace = "***";
        //定义三个指针，分别指向前缀树和待过滤文本
        //指向树根
        Node cur = root;
        //指向文本，begin每次往下移动，position来回移动
        int begin = 0,position = 0;
        //循环处理敏感词
        while(position < text.length()){
            char c = text.charAt(position);
            //处理特殊符号，排除特殊字符的干扰
            if(isSymbol(c)){
                //开头的特殊字符跳过
                if(cur == root){
                    sb.append(c);
                    begin++;
                }
                //特殊字符不在开头，跳过，检查下一个字符
                position++;
                continue;
            }
            if(cur.next.get(c)!= null && cur.next.get(c).isWord){//跳过敏感词
                sb.append(replace);
                begin = ++position;
                cur = root;//重新开始
            }else if(cur.next.get(c) == null){
                //不是敏感词
                sb.append(c);
                position = ++begin;
                cur = root;
            }else {
                position++;
                cur = cur.next.get(c);
            }
        }
        //截取尾端非敏感词
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(char c){
        //不是字母或数字或者不在东亚字符范围之内的都是特殊符号
        return !Character.isLetterOrDigit(c) || c < 0x4E00 || c > 0x9FFF;
    }
}