package com.example.web.controller;

import com.example.api.dto.UserDTO;
import com.example.api.vo.UserVO;
import com.example.common.Result;
import com.example.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 创建用户
     */
    @PostMapping
    public Result<UserVO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserVO userVO = userService.createUser(userDTO);
        return Result.success("用户创建成功", userVO);
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }
    
    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable String username) {
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return Result.success(users);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<UserVO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserVO userVO = userService.updateUser(id, userDTO);
        return Result.success("用户更新成功", userVO);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功", null);
    }
    
    /**
     * 根据用户名搜索用户
     */
    @GetMapping("/search")
    public Result<List<UserVO>> searchUsers(@RequestParam String username) {
        List<UserVO> users = userService.searchUsersByUsername(username);
        return Result.success(users);
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }
} 