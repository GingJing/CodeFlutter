package com.github.gingjing.plugin.formatter.sqlformat.handler;

import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.LINUX_LINE_SEPARATOR;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.WINDOWS_LINE_SEPARATOR;


/**
 * sql格式化处理者抽象类
 *
 * @author: Jmm
 * @date: 2020年05月28日15时36分
 * @version: 1.0
 */
public abstract class AbstractSqlFormatHandler implements SqlFormatHandler {

    protected String processStr;

    protected String placeHolder;

    protected String dbType;

    protected SqlFormatHandler next;

    public AbstractSqlFormatHandler() {
    }

    public AbstractSqlFormatHandler(SqlFormatHandler handler) {
        this.next = handler;
    }

    public AbstractSqlFormatHandler(String processStr, String placeHolder, String dbType) {
        this.processStr = processStr;
        this.placeHolder = placeHolder;
        this.dbType = dbType;
    }

    @Override
    public SqlFormatter process() {
        if (!canProcess()) {
            return next().process();
        }
        return hasLineSeparator() ? processWithLineSeparator() : processWithoutLineSeparator();
    }

    /**
     * 处理有换行符的情况
     *
     * @return com.github.gingjing.gocodereasier.bean.SqlFormatter
     * @since v1.0.0
     * @date: 2020/5/29
     */
    protected abstract SqlFormatter processWithLineSeparator();

    /**
     * 处理无换行符的情况
     *
     * @return com.github.gingjing.gocodereasier.bean.SqlFormatter
     * @since v1.0.0
     * @date: 2020/5/29
     */
    protected abstract SqlFormatter processWithoutLineSeparator();

    protected boolean hasLineSeparator() {
        return PluginStringUtil.containsKeywords(processStr, WINDOWS_LINE_SEPARATOR, LINUX_LINE_SEPARATOR);
    }

    @Override
    public SqlFormatHandler next() {
        return next;
    }

    public String getProcessStr() {
        return processStr;
    }

    public AbstractSqlFormatHandler setProcessStr(String processStr) {
        this.processStr = processStr;
        return this;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public AbstractSqlFormatHandler setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public String getDbType() {
        return dbType;
    }

    public AbstractSqlFormatHandler setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    @Override
    public void setNext(SqlFormatHandler next) {
        this.next = next;
    }

    @Override
    public void change(String sql, String placeHolder, String dbType) {
        setProcessStr(sql).setPlaceHolder(placeHolder).setDbType(dbType);
    }
}
