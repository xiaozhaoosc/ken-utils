package com.xxl.job.executor.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
public class BaseClass {
    @Id
    @GeneratedValue
    private int id;

//    private Long bigenTime;
//    private Long endTime;
//    @Column(columnDefinition="创建时间", nullable = false)
    private Date createTime;
//    @Column(columnDefinition="最后修改时间", nullable = false)
    private Date updateTime;
}
