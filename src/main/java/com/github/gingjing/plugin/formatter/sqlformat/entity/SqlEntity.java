package com.github.gingjing.plugin.formatter.sqlformat.entity;

/**
 * SQL Entity封装
 *
 * @author: Jmm
 * @date: 2020年05月20日18时02分
 * @version: 1.0
 */
public class SqlEntity {

    private String placeHolderCount;

    private String withoutFormatSql;

    private String prepareSql;

    private String realSql;

    public String getPlaceHolderCount() {
        return placeHolderCount;
    }

    public void setPlaceHolderCount(String placeHolderCount) {
        this.placeHolderCount = placeHolderCount;
    }

    public String getPrepareSql() {
        return prepareSql;
    }

    public void setPrepareSql(String prepareSql) {
        this.prepareSql = prepareSql;
    }

    public String getRealSql() {
        return realSql;
    }

    public void setRealSql(String realSql) {
        this.realSql = realSql;
    }

    public String getWithoutFormatSql() {
        return withoutFormatSql;
    }

    public void setWithoutFormatSql(String withoutFormatSql) {
        this.withoutFormatSql = withoutFormatSql;
    }
}
