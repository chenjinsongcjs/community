package com.nowcoder.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/07/22:18
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDto {
    private int userId;
    private long followTime;
}