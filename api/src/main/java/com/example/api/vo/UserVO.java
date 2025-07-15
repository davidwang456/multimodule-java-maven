package com.example.api.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String nickname;
    
    private String phone;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 