package com.example.demodbm.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demodbm.business.entity.TUserNew;
import com.example.demodbm.business.entity.UserWei;
import com.example.demodbm.business.mapper.TUserNewMapper;
import com.example.demodbm.business.mapper.TUserWeiMapper;
import com.example.demodbm.business.service.MPTUserNewService;
import com.example.demodbm.business.service.MPTUserWeiService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kenzhao
 * @since 2018-12-22
 */
@Service
public class TUserWeiServiceImpl extends ServiceImpl<TUserWeiMapper, UserWei> implements
    MPTUserWeiService {

}
