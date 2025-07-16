package com.example.api.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDTO类单元测试
 */
@DisplayName("UserDTO类测试")
class UserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("测试有效用户数据")
    void testValidUserData() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        userDTO.setNickname("测试用户");
        userDTO.setPhone("13800138000");
        userDTO.setStatus(1);
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("password123", userDTO.getPassword());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("测试用户", userDTO.getNickname());
        assertEquals("13800138000", userDTO.getPhone());
        assertEquals(1, userDTO.getStatus());
    }

    @Test
    @DisplayName("测试用户名验证 - 空值")
    void testUsernameValidationEmpty() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    @DisplayName("测试用户名验证 - 长度过短")
    void testUsernameValidationTooShort() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("ab");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    @DisplayName("测试用户名验证 - 长度过长")
    void testUsernameValidationTooLong() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("verylongusernameexceedinglimit");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    @DisplayName("测试密码验证 - 空值")
    void testPasswordValidationEmpty() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("测试密码验证 - 长度过短")
    void testPasswordValidationTooShort() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("12345");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("测试邮箱验证 - 空值")
    void testEmailValidationEmpty() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("测试邮箱验证 - 格式错误")
    void testEmailValidationInvalidFormat() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("invalid-email");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("测试邮箱验证 - 有效格式")
    void testEmailValidationValidFormat() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("测试可选字段")
    void testOptionalFields() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setEmail("test@example.com");
        // nickname, phone, status 是可选的
        
        // When
        var violations = validator.validate(userDTO);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        
        // When
        String result = userDTO.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("test@example.com"));
    }

    @Test
    @DisplayName("测试equals和hashCode方法")
    void testEqualsAndHashCode() {
        // Given
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setUsername("testuser");
        userDTO1.setEmail("test@example.com");
        
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(1L);
        userDTO2.setUsername("testuser");
        userDTO2.setEmail("test@example.com");
        
        UserDTO userDTO3 = new UserDTO();
        userDTO3.setId(2L);
        userDTO3.setUsername("differentuser");
        userDTO3.setEmail("different@example.com");
        
        // When & Then
        assertEquals(userDTO1, userDTO2);
        assertNotEquals(userDTO1, userDTO3);
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
        assertNotEquals(userDTO1.hashCode(), userDTO3.hashCode());
    }
} 