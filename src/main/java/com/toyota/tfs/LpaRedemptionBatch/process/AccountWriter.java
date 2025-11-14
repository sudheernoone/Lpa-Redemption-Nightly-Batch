package com.toyota.tfs.LpaRedemptionBatch.process;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountWriter implements ItemWriter<RedemptionHistory> {

	@Override
	public void write(Chunk<? extends RedemptionHistory> chunk) throws Exception {
		log.info("Item writer - Chunk Processing - Start");
		for (RedemptionHistory redemptionHistory: chunk){
			log.info("Account Number:"+redemptionHistory.getAccountNumber()+" has been processed");
		}
		log.info("Item writer - Chunk Processing - End");
	}
}
