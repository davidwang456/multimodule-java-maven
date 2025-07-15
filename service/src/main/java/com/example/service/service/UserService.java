package com.example.service.service;

import com.example.api.dto.UserDTO;
import com.example.api.vo.UserVO;
import com.example.service.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 创建用户
     */
    UserVO createUser(UserDTO userDTO);
    
    /**
     * 根据ID获取用户
     */
    UserVO getUserById(Long id);
    
    /**
     * 根据用户名获取用户
     */
    UserVO getUserByUsername(String username);
    
    /**
     * 获取所有用户
     */
    List<UserVO> getAllUsers();
    
    /**
     * 更新用户
     */
    UserVO updateUser(Long id, UserDTO userDTO);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 根据用户名模糊查询用户
     */
    List<UserVO> searchUsersByUsername(String username);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
} 