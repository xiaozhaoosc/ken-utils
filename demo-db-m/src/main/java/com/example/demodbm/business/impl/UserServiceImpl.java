package com.example.demodbm.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demodbm.business.dto.User;
import com.example.demodbm.business.dao.UserDao;
import com.example.demodbm.business.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2018/10/31 18:02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements IUserService {

}