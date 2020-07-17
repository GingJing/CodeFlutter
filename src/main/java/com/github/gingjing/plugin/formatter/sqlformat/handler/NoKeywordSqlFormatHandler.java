package com.github.gingjing.plugin.formatter.sqlformat.handler;


import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.*;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.*;

/**
 * 无关键字sql格式化处理者
 *
 * @author: Jmm
 * @date: 2020年05月28日15时54分
 * @version: 1.0
 */
public class NoKeywordSqlFormatHandler extends AbstractSqlFormatHandler {

    public NoKeywordSqlFormatHandler() {
    }

    public NoKeywordSqlFormatHandler(SqlFormatHandler handler) {
        super(handler);
    }

    public NoKeywordSqlFormatHandler(String processStr, String placeHolder, String dbType) {
        super(processStr, placeHolder, dbType);
    }

    @Override
    protected SqlFormatter processWithLineSeparator() {
        String[] split = processStr.split(WINDOWS_LINE_SEPARATOR + "|" + LINUX_LINE_SEPARATOR);
        int needExtraDetail = 2;
        if (split.length >= needExtraDetail) {
            String preSqlStr = split[0].trim();
            if (PluginStringUtil.isBlank(preSqlStr)) {
                return null;
            }
            String paramStr = split[1].trim();
            return makeupSqlFormatter(placeHolder, dbType, preSqlStr, paramStr);
        } else {
            return directProcess(processStr, placeHolder, dbType);
        }
    }

    @Override
    protected SqlFormatter processWithoutLineSeparator() {
        return makeupSqlFormatterNoParamsStr(placeHolder, dbType, processStr);
    }

    @Override
    public boolean canProcess() {
        if (PluginStringUtil.isBlank(processStr)) {
            return false;
        }
        return !PluginStringUtil.containsKeywords(processStr, PREPARING, PARAMETERS);
    }
}
