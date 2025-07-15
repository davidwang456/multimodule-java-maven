package com.example.service.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.service.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问层
 */
@Mapper
@Repository
public interface UserRepository extends BaseMapper<User> {
    
    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查找用户
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(@Param("email") String email);
    
    /**
     * 根据状态查找用户列表
     */
    @Select("SELECT * FROM users WHERE status = #{status}")
    List<User> findByStatus(@Param("status") Integer status);
    
    /**
     * 根据用户名模糊查询
     */
    @Select("SELECT * FROM users WHERE username LIKE CONCAT('%', #{username}, '%')")
    List<User> findByUsernameContaining(@Param("username") String username);
    
    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
    
    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(@Param("email") String email);
} 