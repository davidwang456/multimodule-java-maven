package com.example.common.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BusinessException类单元测试
 */
@DisplayName("BusinessException类测试")
class BusinessExceptionTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        // Given
        String message = "业务异常";
        
        // When
        BusinessException exception = new BusinessException(message);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(500, exception.getCode());
    }

    @Test
    @DisplayName("测试带代码的构造函数")
    void testConstructorWithCode() {
        // Given
        Integer code = 400;
        String message = "参数错误";
        
        // When
        BusinessException exception = new BusinessException(code, message);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("测试带原因的构造函数")
    void testConstructorWithCause() {
        // Given
        String message = "业务异常";
        Throwable cause = new RuntimeException("原始异常");
        
        // When
        BusinessException exception = new BusinessException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(500, exception.getCode());
    }

    @Test
    @DisplayName("测试带代码和原因的构造函数")
    void testConstructorWithCodeAndCause() {
        // Given
        Integer code = 404;
        String message = "资源未找到";
        Throwable cause = new RuntimeException("原始异常");
        
        // When
        BusinessException exception = new BusinessException(code, message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(code, exception.getCode());
    }

    @Test
    @DisplayName("测试setter和getter方法")
    void testSetterAndGetter() {
        // Given
        BusinessException exception = new BusinessException("测试异常");
        
        // When
        exception.setCode(403);
        
        // Then
        assertEquals(403, exception.getCode());
    }

    @Test
    @DisplayName("测试异常继承关系")
    void testExceptionInheritance() {
        // Given
        String message = "业务异常";
        
        // When
        BusinessException exception = new BusinessException(message);
        
        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("测试异常堆栈信息")
    void testExceptionStackTrace() {
        // Given
        String message = "业务异常";
        
        // When
        BusinessException exception = new BusinessException(message);
        
        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
} 