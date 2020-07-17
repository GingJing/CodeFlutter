package com.github.gingjing.plugin.formatter.sqlformat.handler;

import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.makeupSqlFormatter;
import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.makeupSqlFormatterNoParamsStr;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.*;


/**
 * 只含有parameter关键字的格式化处理者
 *
 * @author: Jmm
 * @date: 2020年05月29日09时13分
 * @version: 1.0
 */
public class ParametersKeywordSqlFormatHandler extends AbstractSqlFormatHandler {

    public ParametersKeywordSqlFormatHandler() {
    }

    public ParametersKeywordSqlFormatHandler(SqlFormatHandler handler) {
        super(handler);
    }

    public ParametersKeywordSqlFormatHandler(String processStr, String placeHolder, String dbType) {
        super(processStr, placeHolder, dbType);
    }

    @Override
    protected SqlFormatter processWithLineSeparator() {
        String[] split = processStr.split(PARAMETERS, -1);
        String preSqlStr = split[0].trim();
        if (PluginStringUtil.isBlank(preSqlStr)) {
            return null;
        }
        if (PluginStringUtil.containsKeywords(preSqlStr, WINDOWS_LINE_SEPARATOR, LINUX_LINE_SEPARATOR)) {
            preSqlStr = preSqlStr.split(WINDOWS_LINE_SEPARATOR + "|" + LINUX_LINE_SEPARATOR)[0].trim();
        }
        String paramStr = split[1].trim();
        return PluginStringUtil.isNotBlank(paramStr)
                ? makeupSqlFormatter(placeHolder, dbType, preSqlStr, paramStr)
                : makeupSqlFormatterNoParamsStr(placeHolder, dbType, preSqlStr);
    }

    @Override
    protected SqlFormatter processWithoutLineSeparator() {
        String[] split = processStr.split(PARAMETERS, -1);
        String preSqlStr = split[0].trim();
        if (PluginStringUtil.isBlank(preSqlStr)) {
            return null;
        }
        String paramStr = split[1].trim();
        return PluginStringUtil.isNotBlank(paramStr)
                ? makeupSqlFormatter(placeHolder, dbType, preSqlStr, paramStr)
                : makeupSqlFormatterNoParamsStr(placeHolder, dbType, preSqlStr);
    }

    @Override
    public boolean canProcess() {
        if (PluginStringUtil.isBlank(processStr)) {
            return false;
        }
        return processStr.contains(PARAMETERS) && !processStr.contains(PREPARING);
    }
}
