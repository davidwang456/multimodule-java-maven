package com.example.web;

import com.example.api.dto.UserDTO;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

/**
 * UserController集成测试
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("UserController集成测试")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("测试创建用户集成")
    void testCreateUserIntegration() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("integrationuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("integration@example.com");
        
        when(userService.save(any(UserDTO.class))).thenReturn(true);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    @DisplayName("测试获取用户集成")
    void testGetUserIntegration() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("integrationuser");
        userDTO.setEmail("integration@example.com");
        
        when(userService.findById(1L)).thenReturn(Optional.of(userDTO));
        
        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("integrationuser"));
    }

    @Test
    @DisplayName("测试获取所有用户集成")
    void testGetAllUsersIntegration() throws Exception {
        // Given
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("user1");
        
        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");
        
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("测试更新用户集成")
    void testUpdateUserIntegration() throws Exception {
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
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试删除用户集成")
    void testDeleteUserIntegration() throws Exception {
        // Given
        when(userService.deleteById(1L)).thenReturn(true);
        
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("测试检查用户名集成")
    void testCheckUsernameIntegration() throws Exception {
        // Given
        when(userService.existsByUsername("testuser")).thenReturn(true);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-username")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @DisplayName("测试检查邮箱集成")
    void testCheckEmailIntegration() throws Exception {
        // Given
        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        
        // When & Then
        mockMvc.perform(get("/api/users/check-email")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false));
    }
} 