package com.example.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Result类单元测试
 */
@DisplayName("Result类测试")
class ResultTest {

    @Test
    @DisplayName("测试成功结果创建")
    void testSuccessResult() {
        // Given
        String data = "test data";
        
        // When
        Result<String> result = Result.success(data);
        
        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    @DisplayName("测试成功结果创建（无数据）")
    void testSuccessResultWithoutData() {
        // When
        Result<Void> result = Result.success();
        
        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试成功结果创建（自定义消息）")
    void testSuccessResultWithCustomMessage() {
        // Given
        String customMessage = "自定义成功消息";
        Integer data = 123;
        
        // When
        Result<Integer> result = Result.success(customMessage, data);
        
        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(customMessage, result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    @DisplayName("测试错误结果创建")
    void testErrorResult() {
        // When
        Result<Void> result = Result.error();
        
        // Then
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("操作失败", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试错误结果创建（自定义消息）")
    void testErrorResultWithCustomMessage() {
        // Given
        String errorMessage = "自定义错误消息";
        
        // When
        Result<Void> result = Result.error(errorMessage);
        
        // Then
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals(errorMessage, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试错误结果创建（自定义代码和消息）")
    void testErrorResultWithCustomCodeAndMessage() {
        // Given
        Integer errorCode = 404;
        String errorMessage = "资源未找到";
        
        // When
        Result<Void> result = Result.error(errorCode, errorMessage);
        
        // Then
        assertNotNull(result);
        assertEquals(errorCode, result.getCode());
        assertEquals(errorMessage, result.getMessage());
        assertNull(result.getData());
    }

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        // Given
        Integer code = 200;
        String message = "测试消息";
        String data = "测试数据";
        
        // When
        Result<String> result = new Result<>(code, message, data);
        
        // Then
        assertNotNull(result);
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
    }

    @Test
    @DisplayName("测试setter和getter方法")
    void testSetterAndGetter() {
        // Given
        Result<String> result = new Result<>();
        
        // When
        result.setCode(300);
        result.setMessage("重定向");
        result.setData("重定向数据");
        
        // Then
        assertEquals(300, result.getCode());
        assertEquals("重定向", result.getMessage());
        assertEquals("重定向数据", result.getData());
    }
} 