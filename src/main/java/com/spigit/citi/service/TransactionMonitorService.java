package com.spigit.citi.service;

import com.spigit.citi.common.QueryFactory;
import com.spigit.citi.common.QueryType;
import com.spigit.citi.dao.TransactionQueryHistoryDao;
import com.spigit.citi.dao.TransactionQueueDao;
import com.spigit.citi.model.TransactionQueryHistory;
import com.spigit.citi.model.TransactionQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionMonitorService extends Service {

    private static final Logger logger = LoggerFactory.getLogger(TransactionMonitorService.class);
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionQueueDao transactionQueueDAO;
    @Autowired
    private TransactionQueryHistoryDao transactionQueryHistoryDao;
    @Autowired
    private QueryFactory queryFactory;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void execute() {

        logger.info("*************************TransactionMonitor*************************");
        List<TransactionQueue> transactionQueues = null;
        HashMap<QueryType,String> queriesMap = queryFactory.getQueries();
        for(Map.Entry<QueryType, String> entry : queriesMap.entrySet())   {
            logger.info("retrieving data for QueryType {}",entry.getKey());
            transactionQueues = getTransactionsFromCitiDBs(entry.getValue());
            if(transactionQueues!=null) {
                addRecordToTransactionQueryHistory(entry.getKey());
                addTransactionsToTransactionQueue(transactionQueues, entry.getKey());
            }
        }
    }

    private void addRecordToTransactionQueryHistory(QueryType queryType)   {
        logger.info("adding record to TransactionQueryHistory table");
        TransactionQueryHistory transactionQueryHistory = new TransactionQueryHistory();
        transactionQueryHistory.setQueryType(queryType.name());
        transactionQueryHistory.setTimestamp(Calendar.getInstance().getTime());
        transactionQueryHistoryDao.saveTransactionQueryHistory(transactionQueryHistory);
    }

    private List<TransactionQueue> getTransactionsFromCitiDBs(String sql)    {
        List<TransactionQueue> transactionQueues = this.jdbcTemplate.query(sql, new TransactionQueueMapper());
        return transactionQueues;
    }

    private void addTransactionsToTransactionQueue(List<TransactionQueue> transactionQueues, QueryType queryType) {
        logger.info("adding transactions to TransactionQueue table");
        transactionQueueDAO.saveTransactionQueues(transactionQueues, queryType);
    }

    private static final class TransactionQueueMapper implements RowMapper<TransactionQueue> {
        public TransactionQueue mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            TransactionQueue transactionQueue = new TransactionQueue();
            transactionQueue.setMsgFrom(resultSet.getString("orgin_email"));
            transactionQueue.setMsgTo(resultSet.getString("rcvr_email"));
            transactionQueue.setSubject(resultSet.getString("subj"));
            transactionQueue.setDate(resultSet.getDate("timestamp"));
            transactionQueue.setBody(resultSet.getBlob("body"));
            transactionQueue.setTitle(resultSet.getString("title"));
            return transactionQueue;
        }
    }
}
