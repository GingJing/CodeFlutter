package com.github.gingjing.plugin.formatter.sqlformat.handler;


import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.makeupSqlFormatter;
import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.makeupSqlFormatterNoParamsStr;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.*;

/**
 * 含所有关键字sql格式化处理者
 *
 * @author: Jmm
 * @date: 2020年05月28日15时54分
 * @version: 1.0
 */
public class AllKeywordsSqlFormatHandler extends AbstractSqlFormatHandler {

    public AllKeywordsSqlFormatHandler() {
    }

    public AllKeywordsSqlFormatHandler(SqlFormatHandler handler) {
        super(handler);
    }

    public AllKeywordsSqlFormatHandler(String processStr, String placeHolder, String dbType) {
        super(processStr, placeHolder, dbType);
    }

    @Override
    protected SqlFormatter processWithLineSeparator() {
        String[] split = processStr.split(REGX, -1);
        String preSqlStr = split[1].split(WINDOWS_LINE_SEPARATOR + "|" + LINUX_LINE_SEPARATOR)[0].trim();
        if (PluginStringUtil.isBlank(preSqlStr)) {
            return null;
        }
        String paramStr = split[2].trim();
        return PluginStringUtil.isBlank(paramStr)
                ? makeupSqlFormatterNoParamsStr(placeHolder, dbType, preSqlStr)
                : makeupSqlFormatter(placeHolder, dbType, preSqlStr, paramStr);
    }

    @Override
    protected SqlFormatter processWithoutLineSeparator() {
        String[] split = processStr.split(REGX, -1);
        String preSqlStr = split[1].trim();
        if (PluginStringUtil.isBlank(preSqlStr)) {
            return null;
        }
        String paramStr = split[2].trim();
        return PluginStringUtil.isBlank(paramStr)
                ? makeupSqlFormatterNoParamsStr(placeHolder, dbType, preSqlStr)
                : makeupSqlFormatter(placeHolder, dbType, preSqlStr, paramStr);
    }

    @Override
    public boolean canProcess() {
        if (PluginStringUtil.isBlank(processStr)) {
            return false;
        }
        return PluginStringUtil.containsAllKeywords(processStr, PREPARING, PARAMETERS);
    }

}
