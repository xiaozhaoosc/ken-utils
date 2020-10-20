package com.xxl.job.executor.repository;

import com.xxl.job.executor.model.JavBusDowns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavBusDownRepository extends JpaRepository<JavBusDowns,Integer> {
}
