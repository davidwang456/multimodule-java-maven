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
        try {
            UserVO userVO = userService.createUser(userDTO);
            return Result.success("用户创建成功", userVO);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        try {
            UserVO userVO = userService.getUserById(id);
            if (userVO == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable String username) {
        try {
            UserVO userVO = userService.getUserByUsername(username);
            if (userVO == null) {
                return Result.error("用户不存在");
            }
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<UserVO>> getAllUsers() {
        try {
            List<UserVO> users = userService.getAllUsers();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<UserVO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            UserVO userVO = userService.updateUser(id, userDTO);
            return Result.success("用户更新成功", userVO);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return Result.success("用户删除成功", null);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 根据用户名搜索用户
     */
    @GetMapping("/search")
    public Result<List<UserVO>> searchUsers(@RequestParam String username) {
        try {
            List<UserVO> users = userService.searchUsersByUsername(username);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            return Result.success(exists);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
} 