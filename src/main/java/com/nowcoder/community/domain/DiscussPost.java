package com.nowcoder.community.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/09/30/21:52
 * @Description: 帖子类
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//es文档注解 索引 文档  分片 备份
@Document(indexName = "discuss_post",type = "_doc",shards = 6,replicas = 3)
public class DiscussPost {
    @Field(type = FieldType.Integer)
    private Integer id;
    @Field(type = FieldType.Integer)
    private Integer userId;
    //主要检索字段 使用ik分词器 使用最大词拆分，搜索时智能搜索
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Integer)
    private Integer type;//0 普通 1 置顶
    @Field(type = FieldType.Integer)
    private Integer status;//0 正常 1 精华
    //日期格式化
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    @Field(type = FieldType.Double)
    private Double score;//排名分数
}