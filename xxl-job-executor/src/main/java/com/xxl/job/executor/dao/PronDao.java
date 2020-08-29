package com.xxl.job.executor.dao;

import com.xxl.job.executor.model.Pron;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface PronDao {

    public int save(Pron info);
    public List<Pron> findAll(String tableName);

    public int update(@Param("id") int id,@Param("tableName") String tableName);

    public int updateFail(@Param("id") int id,@Param("tableName") String tableName);

    public int remove(@Param("id") int id,@Param("tableName") String tableName);


    public List<Pron> pageList(@Param("offset") int offset,
                                @Param("pagesize") int pagesize,@Param("tableName") String tableName);
    public List<Pron> pageListFail(@Param("offset") int offset,
                                @Param("pagesize") int pagesize,@Param("tableName") String tableName);

    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,@Param("tableName") String tableName);

}
