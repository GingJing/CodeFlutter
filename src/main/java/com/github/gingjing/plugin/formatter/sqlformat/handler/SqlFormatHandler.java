package com.github.gingjing.plugin.formatter.sqlformat.handler;

import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

/**
 * sql格式化处理者接口
 *
 * @author: Jmm
 * @date: 2020年05月29日09时32分
 * @version: 1.0
 */
public interface SqlFormatHandler extends FormatHandler<SqlFormatHandler> {

    @Override
    SqlFormatter process();

    @Override
    SqlFormatHandler next();

    @Override
    void setNext(SqlFormatHandler handler);

    void change(String sql, String placeHolder, String dbType);
}
