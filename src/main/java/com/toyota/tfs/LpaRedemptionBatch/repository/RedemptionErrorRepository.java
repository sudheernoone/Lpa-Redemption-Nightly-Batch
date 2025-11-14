package com.toyota.tfs.LpaRedemptionBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionError;

@Repository
public interface RedemptionErrorRepository extends JpaRepository<RedemptionError,Long> {
}
