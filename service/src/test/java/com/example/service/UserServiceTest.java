package com.example.service;

import com.example.api.dto.UserDTO;
import com.example.api.vo.UserVO;
import com.example.common.exception.BusinessException;
import com.example.service.entity.User;
import com.example.service.repository.UserRepository;
import com.example.service.service.UserService;
import com.example.service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;

/**
 * UserService接口单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService接口测试")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private UserVO testUserVO;

    @BeforeEach
    void setUp() throws Exception {
        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setNickname("测试用户");
        testUser.setPhone("13800138000");
        testUser.setStatus(1);

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setPassword("password123");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setNickname("测试用户");
        testUserDTO.setPhone("13800138000");
        testUserDTO.setStatus(1);

        testUserVO = new UserVO();
        testUserVO.setId(1L);
        testUserVO.setUsername("testuser");
        testUserVO.setEmail("test@example.com");
        testUserVO.setNickname("测试用户");
        testUserVO.setPhone("13800138000");
        testUserVO.setStatus(1);

        // 反射注入 baseMapper，解决 ServiceImpl NPE 问题
        Field baseMapperField = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class.getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(userService, userRepository);
    }

    @Test
    @DisplayName("测试检查用户名是否存在")
    void testExistsByUsername() {
        // Given
        when(userRepository.countByUsername("testuser")).thenReturn(1);
        
        // When
        boolean result = userService.existsByUsername("testuser");
        
        // Then
        assertTrue(result);
        verify(userRepository, times(1)).countByUsername("testuser");
    }

    @Test
    @DisplayName("测试检查用户名是否存在 - 不存在")
    void testExistsByUsernameNotExists() {
        // Given
        when(userRepository.countByUsername("nonexistent")).thenReturn(0);
        
        // When
        boolean result = userService.existsByUsername("nonexistent");
        
        // Then
        assertFalse(result);
        verify(userRepository, times(1)).countByUsername("nonexistent");
    }

    @Test
    @DisplayName("测试检查邮箱是否存在")
    void testExistsByEmail() {
        // Given
        when(userRepository.countByEmail("test@example.com")).thenReturn(1);
        
        // When
        boolean result = userService.existsByEmail("test@example.com");
        
        // Then
        assertTrue(result);
        verify(userRepository, times(1)).countByEmail("test@example.com");
    }

    @Test
    @DisplayName("测试检查邮箱是否存在 - 不存在")
    void testExistsByEmailNotExists() {
        // Given
        when(userRepository.countByEmail("nonexistent@example.com")).thenReturn(0);
        
        // When
        boolean result = userService.existsByEmail("nonexistent@example.com");
        
        // Then
        assertFalse(result);
        verify(userRepository, times(1)).countByEmail("nonexistent@example.com");
    }
} 