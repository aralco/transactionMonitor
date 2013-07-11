package com.spigit.citi.dao;

import com.spigit.citi.common.QueryType;
import com.spigit.citi.model.TransactionQueue;

import java.util.List;

public interface TransactionQueueDao {

    public List<TransactionQueue> getTransactionQueues();

    public void saveTransactionQueues(List<TransactionQueue> transactionQueueList, QueryType queryType);

}
