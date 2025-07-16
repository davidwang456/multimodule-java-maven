package com.example.web.controller;

import com.example.api.dto.UserDTO;
import com.example.common.Result;
import com.example.service.service.UserService;
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
import com.example.api.vo.UserVO;

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
    private UserVO userVO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        userVO = new UserVO();
        userVO.setId(1L);
        userVO.setUsername("testuser");
        userVO.setEmail("test@example.com");
        userVO.setNickname("测试用户");
        userVO.setPhone("13800138000");
        userVO.setStatus(1);
    }

    @Test
    @DisplayName("测试创建用户 - 成功")
    void testCreateUserSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        when(userService.createUser(any(UserDTO.class))).thenReturn(userVO);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("用户创建成功"));
        
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试创建用户 - 失败")
    void testCreateUserFailure() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        when(userService.createUser(any(UserDTO.class))).thenThrow(new RuntimeException("创建用户失败"));
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 成功")
    void testGetUserByIdSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        
        when(userService.getUserById(1L)).thenReturn(userVO);
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
        
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("测试根据ID获取用户 - 用户不存在")
    void testGetUserByIdNotFound() throws Exception {
        // Given
        when(userService.getUserById(999L)).thenReturn(null);
        
        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("用户不存在"));
        
        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    @DisplayName("测试获取所有用户")
    void testGetAllUsers() throws Exception {
        // Given
        UserVO userVO1 = new UserVO();
        userVO1.setId(1L);
        userVO1.setUsername("user1");
        userVO1.setEmail("user1@example.com");
        
        UserVO userVO2 = new UserVO();
        userVO2.setId(2L);
        userVO2.setUsername("user2");
        userVO2.setEmail("user2@example.com");
        
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userVO1, userVO2));
        
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[1].username").value("user2"));
        
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("测试更新用户 - 成功")
    void testUpdateUserSuccess() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("updateduser");
        userDTO.setPassword("password123");
        userDTO.setEmail("updated@example.com");
        
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userVO);
        
        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("用户更新成功"));
        
        verify(userService, times(1)).updateUser(anyLong(), any(UserDTO.class));
    }

    @Test
    @DisplayName("测试更新用户 - 失败")
    void testUpdateUserFailure() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("updateduser");
        userDTO.setPassword("password123");
        userDTO.setEmail("updated@example.com");
        
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenThrow(new RuntimeException("更新用户失败"));
        
        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).updateUser(anyLong(), any(UserDTO.class));
    }

    @Test
    @DisplayName("测试删除用户 - 成功")
    void testDeleteUserSuccess() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);
        
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("用户删除成功"));
        
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("测试删除用户 - 失败")
    void testDeleteUserFailure() throws Exception {
        // Given
        doThrow(new RuntimeException("删除用户失败")).when(userService).deleteUser(999L);
        
        // When & Then
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("操作失败"));
        
        verify(userService, times(1)).deleteUser(999L);
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
                .andExpect(status().isBadRequest());
    }
} 