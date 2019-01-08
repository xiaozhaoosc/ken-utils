package com.example.demodbm.business.service.impl;

import com.example.demodbm.business.entity.TUserRecord;
import com.example.demodbm.business.mapper.TUserRecordMapper;
import com.example.demodbm.business.service.MPTUserRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TUserRecordServiceImpl extends ServiceImpl<TUserRecordMapper, TUserRecord> implements MPTUserRecordService {

}
