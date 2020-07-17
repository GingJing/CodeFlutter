package com.github.gingjing.plugin.formatter.sqlformat.handler;


import com.github.gingjing.plugin.formatter.constants.PatternConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 数据库处理者封装类
 *
 * @author: Jmm
 * @date: 2020年05月09日13时50分
 * @version: 1.0
 */
public class DatabaseHandler {

    public static DatabaseHandler theHandler;

    private Map<Pattern, String> typeMapping = new HashMap<>();

    private DatabaseHandler() {
        loadTypes();
    }

    public static DatabaseHandler getInstance() {
        if (theHandler == null) {
            synchronized (DatabaseHandler.class) {
                if (theHandler == null) {
                    theHandler = new DatabaseHandler();
                }
            }
        }
        return theHandler;
    }

    /** 加载类型映射 */
    private void loadTypes() {
        typeMapping.put(PatternConstants.VARCHAR, "String");
        typeMapping.put(PatternConstants.CHAR, "String");
        typeMapping.put(PatternConstants.STRING, "String");
        typeMapping.put(PatternConstants.TEXT, "String");
        typeMapping.put(PatternConstants.DECIMAL, "BigDecimal");
        typeMapping.put(PatternConstants.DECIMAL_TWO, "BigDecimal");
        typeMapping.put(PatternConstants.TINYINT, "int");
        typeMapping.put(PatternConstants.INTEGER, "int");
        typeMapping.put(PatternConstants.INT, "int");
        typeMapping.put(PatternConstants.INT4, "int");
        typeMapping.put(PatternConstants.INT8, "long");
        typeMapping.put(PatternConstants.BIGINT, "long");
        typeMapping.put(PatternConstants.DATE, "Date");
        typeMapping.put(PatternConstants.DATETIME, "Date");
        typeMapping.put(PatternConstants.TIMESTAMP, "Date");
        typeMapping.put(PatternConstants.BOOLEAN, "boolean");
    }


}
