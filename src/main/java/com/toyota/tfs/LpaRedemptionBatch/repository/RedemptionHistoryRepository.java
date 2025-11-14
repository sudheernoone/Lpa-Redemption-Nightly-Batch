package com.toyota.tfs.LpaRedemptionBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;

@Repository
public interface RedemptionHistoryRepository extends JpaRepository<RedemptionHistory,String> {
}
