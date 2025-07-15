package com.example.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.dto.UserDTO;
import com.example.api.vo.UserVO;
import com.example.common.exception.BusinessException;
import com.example.service.entity.User;
import com.example.service.repository.UserRepository;
import com.example.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserRepository, User> implements UserService {
    
    @Override
    @Transactional
    public UserVO createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (this.baseMapper.countByUsername(userDTO.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (this.baseMapper.countByEmail(userDTO.getEmail()) > 0) {
            throw new BusinessException("邮箱已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        save(user);
        return convertToVO(user);
    }
    
    @Override
    public UserVO getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }
    
    @Override
    public UserVO getUserByUsername(String username) {
        User user = this.baseMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }
    
    @Override
    public List<UserVO> getAllUsers() {
        List<User> users = list();
        return users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public UserVO updateUser(Long id, UserDTO userDTO) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(userDTO.getUsername()) && 
            this.baseMapper.countByUsername(userDTO.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否被其他用户使用
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            this.baseMapper.countByEmail(userDTO.getEmail()) > 0) {
            throw new BusinessException("邮箱已存在");
        }
        
        BeanUtils.copyProperties(userDTO, user, "id", "createTime");
        updateById(user);
        return convertToVO(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!removeById(id)) {
            throw new BusinessException("用户不存在");
        }
    }
    
    @Override
    public List<UserVO> searchUsersByUsername(String username) {
        List<User> users = this.baseMapper.findByUsernameContaining(username);
        return users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return this.baseMapper.countByUsername(username) > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return this.baseMapper.countByEmail(email) > 0;
    }
    
    /**
     * 将实体转换为VO
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
} 