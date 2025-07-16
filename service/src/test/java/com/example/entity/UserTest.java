package com.example.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * User实体类单元测试
 */
@DisplayName("User实体类测试")
class UserTest {

    @Test
    @DisplayName("测试User实体创建")
    void testUserCreation() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setNickname("测试用户");
        user.setPhone("13800138000");
        user.setStatus(1);
        
        // When & Then
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("测试用户", user.getNickname());
        assertEquals("13800138000", user.getPhone());
        assertEquals(1, user.getStatus());
    }

    @Test
    @DisplayName("测试User实体构造函数")
    void testUserConstructor() {
        // Given & When
        User user = new User();
        
        // Then
        assertNotNull(user);
    }

    @Test
    @DisplayName("测试User实体toString方法")
    void testUserToString() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        // When
        String result = user.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
    }

    @Test
    @DisplayName("测试User实体equals方法")
    void testUserEquals() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        user1.setEmail("test@example.com");
        
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        
        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("differentuser");
        user3.setEmail("different@example.com");
        
        // When & Then
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertEquals(user1, user1); // 自反性
    }

    @Test
    @DisplayName("测试User实体hashCode方法")
    void testUserHashCode() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        user1.setEmail("test@example.com");
        
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setEmail("test@example.com");
        
        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("differentuser");
        user3.setEmail("different@example.com");
        
        // When & Then
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    @DisplayName("测试User实体字段验证")
    void testUserFieldValidation() {
        // Given
        User user = new User();
        
        // When & Then - 测试默认值
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getNickname());
        assertNull(user.getPhone());
        assertNull(user.getStatus());
    }

    @Test
    @DisplayName("测试User实体字段设置和获取")
    void testUserFieldSettersAndGetters() {
        // Given
        User user = new User();
        
        // When
        user.setId(100L);
        user.setUsername("newuser");
        user.setPassword("newpassword");
        user.setEmail("new@example.com");
        user.setNickname("新用户");
        user.setPhone("13900139000");
        user.setStatus(0);
        
        // Then
        assertEquals(100L, user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("newpassword", user.getPassword());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("新用户", user.getNickname());
        assertEquals("13900139000", user.getPhone());
        assertEquals(0, user.getStatus());
    }

    @Test
    @DisplayName("测试User实体边界值")
    void testUserBoundaryValues() {
        // Given
        User user = new User();
        
        // When - 测试边界值
        user.setId(Long.MAX_VALUE);
        user.setUsername("a"); // 最小长度
        user.setPassword("123456"); // 最小长度
        user.setEmail("a@b.c"); // 最小邮箱格式
        user.setStatus(Integer.MAX_VALUE);
        
        // Then
        assertEquals(Long.MAX_VALUE, user.getId());
        assertEquals("a", user.getUsername());
        assertEquals("123456", user.getPassword());
        assertEquals("a@b.c", user.getEmail());
        assertEquals(Integer.MAX_VALUE, user.getStatus());
    }

    @Test
    @DisplayName("测试User实体特殊字符")
    void testUserSpecialCharacters() {
        // Given
        User user = new User();
        
        // When
        user.setUsername("user@123");
        user.setNickname("用户@#$%");
        user.setEmail("user+tag@example.com");
        user.setPhone("+86-138-0013-8000");
        
        // Then
        assertEquals("user@123", user.getUsername());
        assertEquals("用户@#$%", user.getNickname());
        assertEquals("user+tag@example.com", user.getEmail());
        assertEquals("+86-138-0013-8000", user.getPhone());
    }
} 