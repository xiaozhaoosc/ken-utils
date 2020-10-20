package com.xxl.job.executor.dao;

import com.xxl.job.executor.model.JavBus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JavBusDao {
    public List<JavBus> findAll(String tableName);

    public int update(@Param("id") int id, @Param("tableName") String tableName);

    public int updateFail(@Param("id") int id,@Param("tableName") String tableName);

    public int remove(@Param("id") int id,@Param("tableName") String tableName);


    public List<JavBus> pageList(@Param("offset") int offset,
                                @Param("pagesize") int pagesize,@Param("tableName") String tableName);
    public List<JavBus> pageListFail(@Param("offset") int offset,
                                    @Param("pagesize") int pagesize,@Param("tableName") String tableName);

    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,@Param("tableName") String tableName);
}
