package com.github.gingjing.plugin.formatter.sqlformat;


import com.github.gingjing.plugin.formatter.sqlformat.handler.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 默认sql格式化处理者链
 *
 * @author: Jmm
 * @date: 2020年05月29日14时50分
 * @version: 1.0
 */
public class DefaultSqlFormatHandlerChain extends AbstractFormatHandlerChain<SqlFormatHandler, SqlFormatHandlerChain> implements SqlFormatHandlerChain {

    public DefaultSqlFormatHandlerChain(String processStr, String placeHolder, String dbType) {
        defaultInit(processStr, placeHolder, dbType);
    }

    private void defaultInit(String processStr, String placeHolder, String dbType) {
        AllKeywordsSqlFormatHandler allKeyword = new AllKeywordsSqlFormatHandler(processStr, placeHolder, dbType);
        NoKeywordSqlFormatHandler noKeyword = new NoKeywordSqlFormatHandler(processStr, placeHolder, dbType);
        ParametersKeywordSqlFormatHandler parametersKeyword = new ParametersKeywordSqlFormatHandler(processStr, placeHolder, dbType);
        PrepareKeywordSqlFormatHandler prepareKeyword = new PrepareKeywordSqlFormatHandler(processStr, placeHolder, dbType);
        handlers.add(allKeyword);
        handlers.add(noKeyword);
        handlers.add(parametersKeyword);
        handlers.add(prepareKeyword);
        registerHandlers();
    }

    @Override
    public DefaultSqlFormatHandlerChain change(String sql, String placeHolder, String dbType) {
        if (StringUtils.isEmpty(sql) || StringUtils.isEmpty(placeHolder) || StringUtils.isEmpty(dbType)) {
            return this;
        }
        if (CollectionUtils.isNotEmpty(handlers)) {
            for (SqlFormatHandler handler : handlers) {
                handler.change(sql, placeHolder, dbType);
            }
        }
        return this;
    }

    @Override
    public SqlFormatter doProcess() {
        if (finalFormatHandler == null) {
            return null;
        }
        return finalFormatHandler.process();
    }
}
