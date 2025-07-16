package com.example.web.controller;

import com.example.api.dto.UserDTO;
import com.example.common.Result;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

/**
 * UserController单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController测试")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("测试创建用户 - 成功")
    void testCreateUserSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        when(userService.save(any(UserDTO.class))).thenReturn(true);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
        
        verify(userService, times(1)).save(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试创建用户 - 失败")
    void testCreateUserFailure() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        when(userService.save(any(UserDTO.class))).thenReturn(false);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).save(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 成功")
    void testGetUserByIdSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        
        when(userService.findById(1L)).thenReturn(Optional.of(userDTO));
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
        
        verify(userService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 用户不存在")
    void testGetUserByIdNotFound() throws Exception {
        // Given
        when(userService.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("用户不存在"));
        
        verify(userService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("测试获取所有用户")
    void testGetAllUsers() throws Exception {
        // Given
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        
        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[1].username").value("user2"));
        
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("测试更新用户 - 成功")
    void testUpdateUserSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updated@example.com");
        
        when(userService.update(any(UserDTO.class))).thenReturn(true);
        
        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
        
        verify(userService, times(1)).update(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试更新用户 - 失败")
    void testUpdateUserFailure() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updated@example.com");
        
        when(userService.update(any(UserDTO.class))).thenReturn(false);
        
        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).update(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试删除用户 - 成功")
    void testDeleteUserSuccess() throws Exception {
        // Given
        when(userService.deleteById(1L)).thenReturn(true);
        
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
        
        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("测试删除用户 - 失败")
    void testDeleteUserFailure() throws Exception {
        // Given
        when(userService.deleteById(999L)).thenReturn(false);
        
        // When & Then
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("测试检查用户名是否存在 - 存在")
    void testCheckUsernameExists() throws Exception {
        // Given
        when(userService.existsByUsername("testuser")).thenReturn(true);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-username")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
        
        verify(userService, times(1)).existsByUsername("testuser");
    }

    @Test
    @DisplayName("测试检查用户名是否存在 - 不存在")
    void testCheckUsernameNotExists() throws Exception {
        // Given
        when(userService.existsByUsername("nonexistent")).thenReturn(false);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-username")
                .param("username", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false));
        
        verify(userService, times(1)).existsByUsername("nonexistent");
    }

    @Test
    @DisplayName("测试检查邮箱是否存在 - 存在")
    void testCheckEmailExists() throws Exception {
        // Given
        when(userService.existsByEmail("test@example.com")).thenReturn(true);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-email")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
        
        verify(userService, times(1)).existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("测试检查邮箱是否存在 - 不存在")
    void testCheckEmailNotExists() throws Exception {
        // Given
        when(userService.existsByEmail("nonexistent@example.com")).thenReturn(false);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-email")
                .param("email", "nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false));
        
        verify(userService, times(1)).existsByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("测试无效的JSON请求")
    void testInvalidJsonRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("测试缺少必需字段的请求")
    void testMissingRequiredFields() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        // 不设置必需字段
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
    }
} 