package com.xxl.job.executor.repository;

import com.xxl.job.executor.model.JavBus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JavBusRepository extends JpaRepository<JavBus,Integer> {
}
