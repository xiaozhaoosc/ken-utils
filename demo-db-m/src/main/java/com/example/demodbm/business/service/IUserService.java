package com.example.demodbm.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demodbm.business.entity.User;

/**
 * Description:
 *
 * @author kenzhao
 * @date 2018/10/31 18:02
 */

public interface IUserService extends IService<User> {

  /**
   * 添加用户信息
   * @param fileName
   */
  void addUserInfo(String fileName);
  /**
   * 用户信息
   * @param phone
   */
  User getUserInfo(String phone);
  /**
   * 用户信息
   * @param phone
   */
  User getUserInfo2(String phone);

  /**
   * 同步信息
   * @param date
   * @return
   */
  Long syncUser(String date);
}