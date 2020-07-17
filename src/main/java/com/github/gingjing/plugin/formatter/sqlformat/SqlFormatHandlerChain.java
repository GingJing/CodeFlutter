package com.github.gingjing.plugin.formatter.sqlformat;

import com.github.gingjing.plugin.formatter.sqlformat.handler.SqlFormatHandler;

/**
 * sql格式化处理者链
 *
 * @author: Jmm
 * @date: 2020年05月28日15时47分
 * @version: 1.0
 */
public interface SqlFormatHandlerChain extends FormatHandlerChain<SqlFormatHandler, SqlFormatHandlerChain> {

    /**
     * 传入待格式化的sql语句字符串返回格式化处理者链
     *
     * @param sqlStatement 待处理的sql语句字符串
     * @param placeHolder  占位符
     * @param dbType       数据库类型
     * @return com.github.gingjing.gocodereasier.handler.FormatHandlerChain
     * @since v1.0.0
     * @date: 2020/6/5
     */
    FormatHandlerChain<SqlFormatHandler, SqlFormatHandlerChain> change(String sqlStatement, String placeHolder, String dbType);

    /**
     * 处理
     *
     * @return {@link SqlFormatter}
     * @see FormatHandlerChain#doProcess()
     */
    @Override
    SqlFormatter doProcess();
}
