package com.example.service;

import com.example.api.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * UserService接口单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService接口测试")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("测试保存用户")
    void testSaveUser() {
        // Given
        when(userMapper.insert(any(User.class))).thenReturn(1);
        
        // When
        boolean result = userService.save(testUserDTO);
        
        // Then
        assertTrue(result);
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("测试保存用户失败")
    void testSaveUserFailure() {
        // Given
        when(userMapper.insert(any(User.class))).thenReturn(0);
        
        // When
        boolean result = userService.save(testUserDTO);
        
        // Then
        assertFalse(result);
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("测试根据ID查找用户")
    void testFindById() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        
        // When
        Optional<UserDTO> result = userService.findById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserDTO.getUsername(), result.get().getUsername());
        assertEquals(testUserDTO.getEmail(), result.get().getEmail());
        verify(userMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("测试根据ID查找用户 - 用户不存在")
    void testFindByIdNotFound() {
        // Given
        when(userMapper.selectById(999L)).thenReturn(null);
        
        // When
        Optional<UserDTO> result = userService.findById(999L);
        
        // Then
        assertFalse(result.isPresent());
        verify(userMapper, times(1)).selectById(999L);
    }

    @Test
    @DisplayName("测试根据用户名查找用户")
    void testFindByUsername() {
        // Given
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "testuser");
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(testUser);
        
        // When
        Optional<UserDTO> result = userService.findByUsername("testuser");
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试根据用户名查找用户 - 用户不存在")
    void testFindByUsernameNotFound() {
        // Given
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        
        // When
        Optional<UserDTO> result = userService.findByUsername("nonexistent");
        
        // Then
        assertFalse(result.isPresent());
        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试查找所有用户")
    void testFindAll() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        
        List<User> users = Arrays.asList(testUser, user2);
        when(userMapper.selectList(any(QueryWrapper.class))).thenReturn(users);
        
        // When
        List<UserDTO> result = userService.findAll();
        
        // Then
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(userMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试分页查找用户")
    void testFindByPage() {
        // Given
        Page<User> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(testUser));
        page.setTotal(1);
        when(userMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(page);
        
        // When
        Page<UserDTO> result = userService.findByPage(1, 10);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
        verify(userMapper, times(1)).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试更新用户")
    void testUpdateUser() {
        // Given
        when(userMapper.updateById(any(User.class))).thenReturn(1);
        
        // When
        boolean result = userService.update(testUserDTO);
        
        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    @DisplayName("测试更新用户失败")
    void testUpdateUserFailure() {
        // Given
        when(userMapper.updateById(any(User.class))).thenReturn(0);
        
        // When
        boolean result = userService.update(testUserDTO);
        
        // Then
        assertFalse(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    @DisplayName("测试删除用户")
    void testDeleteUser() {
        // Given
        when(userMapper.deleteById(1L)).thenReturn(1);
        
        // When
        boolean result = userService.deleteById(1L);
        
        // Then
        assertTrue(result);
        verify(userMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("测试删除用户失败")
    void testDeleteUserFailure() {
        // Given
        when(userMapper.deleteById(999L)).thenReturn(0);
        
        // When
        boolean result = userService.deleteById(999L);
        
        // Then
        assertFalse(result);
        verify(userMapper, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("测试检查用户名是否存在")
    void testExistsByUsername() {
        // Given
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);
        
        // When
        boolean result = userService.existsByUsername("testuser");
        
        // Then
        assertTrue(result);
        verify(userMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试检查用户名是否存在 - 不存在")
    void testExistsByUsernameNotExists() {
        // Given
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        
        // When
        boolean result = userService.existsByUsername("nonexistent");
        
        // Then
        assertFalse(result);
        verify(userMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试检查邮箱是否存在")
    void testExistsByEmail() {
        // Given
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);
        
        // When
        boolean result = userService.existsByEmail("test@example.com");
        
        // Then
        assertTrue(result);
        verify(userMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试检查邮箱是否存在 - 不存在")
    void testExistsByEmailNotExists() {
        // Given
        when(userMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        
        // When
        boolean result = userService.existsByEmail("nonexistent@example.com");
        
        // Then
        assertFalse(result);
        verify(userMapper, times(1)).selectCount(any(QueryWrapper.class));
    }
} 