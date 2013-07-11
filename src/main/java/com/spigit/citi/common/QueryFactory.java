package com.spigit.citi.common;

import com.spigit.citi.dao.TransactionQueryHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QueryFactory {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    @Autowired
    private TransactionQueryHistoryDao transactionQueryHistoryDao;

    public HashMap<QueryType,String> getQueries() {
        HashMap<QueryType,String> queriesMap = new HashMap<QueryType, String>(7);
        HashMap<String,String> queryPropertiesMap = fillPropertiesMap();
        String sql = null;
        for(QueryType queryType : QueryType.values())   {
            if(queryType!=QueryType.RECONCILIATION)    {
                sql = getQuery(queryType, queryPropertiesMap);
                queriesMap.put(queryType, sql);
            }
        }
        return queriesMap;
    }

    private String getQuery(QueryType queryType, HashMap<String,String> queryPropertiesMap) {
        String sql = null;
        switch (queryType)  {
            case IDEA:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.idea.from_time"));
                break;
            case COMMENT:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.comment.from_time"));
                break;
            case REPLY:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.reply.from_time"));
                break;
            case TELL_A_FRIEND:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.tellafriend.from_time"));
                break;
            case REVIEW:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.review.from_time"));
                break;
            case EMAIL_HISTORY:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.emailhistory.from_time"));
                break;
            case TESTIMONIAL:
                sql = loadQuery(queryPropertiesMap.get("query.location"), queryType.name());
                sql = replaceFromTimeToTime(sql, queryPropertiesMap.get("query.testimonial.from_time"));
                break;
        }
        return sql;
    }

    private HashMap<String,String> fillPropertiesMap() {
        HashMap<String,String> queryPropertiesMap = null;
        int numberOfRecords = transactionQueryHistoryDao.getNumberOfRows();
        queryPropertiesMap = readQueryPropertiesFile();
        if(numberOfRecords>0)    {
            queryPropertiesMap = readQueryFromTable(queryPropertiesMap);
        }
        return queryPropertiesMap;
    }

    private String replaceFromTimeToTime(String sql, String fromTime)    {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);
        String sql1 = sql.replace("@from_time", fromTime);
        String sql2 = sql1.replace("@to_time", formatter.format(currentTime));
        return sql2;
    }

    private HashMap<String,String> readQueryFromTable(HashMap<String,String> propertiesMap)    {
        List<HashMap<String,String>> propertiesMapList = transactionQueryHistoryDao.getTableProperties();
        for(HashMap<String,String> map : propertiesMapList) {
            QueryType queryType =   QueryType.valueOf(map.get("queryType"));
            String fromTime = map.get("timestamp").substring(0,10);

            switch (queryType)  {
                case IDEA:
                    propertiesMap.put("query.idea.from_time", fromTime);
                    break;
                case COMMENT:
                    propertiesMap.put("query.comment.from_time", fromTime);
                    break;
                case REPLY:
                    propertiesMap.put("query.reply.from_time", fromTime);
                    break;
                case TELL_A_FRIEND:
                    propertiesMap.put("query.tellafriend.from_time", fromTime);
                    break;
                case REVIEW:
                    propertiesMap.put("query.review.from_time", fromTime);
                    break;
                case EMAIL_HISTORY:
                    propertiesMap.put("query.emailhistory.from_time", fromTime);
                    break;
                case TESTIMONIAL:
                    propertiesMap.put("query.testimonial.from_time", fromTime);
                    break;
            }

        }
        return propertiesMap;
    }

    private HashMap<String,String> readQueryPropertiesFile()    {
        Properties properties = new Properties();
        HashMap<String, String> propertiesMap = new HashMap<String, String>(0);
        InputStream inputStream = null;
        try {
            inputStream = QueryFactory.class.getClassLoader().getResourceAsStream("query.properties");
            properties.load(inputStream);
            propertiesMap.put("query.location", properties.getProperty("query.location"));
            propertiesMap.put("query.idea.from_time", properties.getProperty("query.idea.from_time"));
            propertiesMap.put("query.comment.from_time", properties.getProperty("query.comment.from_time"));
            propertiesMap.put("query.reply.from_time", properties.getProperty("query.reply.from_time"));
            propertiesMap.put("query.tellafriend.from_time", properties.getProperty("query.tellafriend.from_time"));
            propertiesMap.put("query.review.from_time", properties.getProperty("query.review.from_time"));
            propertiesMap.put("query.emailhistory.from_time", properties.getProperty("query.emailhistory.from_time"));
            propertiesMap.put("query.testimonial.from_time", properties.getProperty("query.testimonial.from_time"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream!=null)
                    inputStream.close();
            } catch (IOException ex)    {
                ex.printStackTrace();
            }
        }
        return propertiesMap;
    }

    private String loadQuery(String filePath, String sqlFileName)    {
        StringBuilder sqlBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            String currentLine;
            bufferedReader = new BufferedReader(new FileReader(filePath + File.separator+sqlFileName+".sql"));
            while((currentLine=bufferedReader.readLine())!=null)    {
                sqlBuilder.append(currentLine);
                sqlBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader!=null)
                    bufferedReader.close();
            } catch (IOException ex)    {
                ex.printStackTrace();
            }
        }
        return sqlBuilder.toString();
    }

}
