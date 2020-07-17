package com.github.gingjing.plugin.formatter.sqlformat.handler;


import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.makeupSqlFormatterNoParamsStr;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.PARAMETERS;
import static com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter.PREPARING;

/**
 * 只含有prepare关键字的格式化处理者
 *
 * @author: Jmm
 * @date: 2020年05月29日09时13分
 * @version: 1.0
 */
public class PrepareKeywordSqlFormatHandler extends AbstractSqlFormatHandler {

    public PrepareKeywordSqlFormatHandler() {
    }

    public PrepareKeywordSqlFormatHandler(SqlFormatHandler handler) {
        super(handler);
    }

    public PrepareKeywordSqlFormatHandler(String processStr, String placeHolder, String dbType) {
        super(processStr, placeHolder, dbType);
    }

    @Override
    protected SqlFormatter processWithLineSeparator() {
        String[] split = processStr.split(PREPARING, -1);
        if (split.length <= 1) {
            return null;
        }
        String preSqlStr = split[1].trim();
        if (PluginStringUtil.isBlank(preSqlStr)) {
            return null;
        }
        return makeupSqlFormatterNoParamsStr(placeHolder, dbType, preSqlStr);

    }

    @Override
    protected SqlFormatter processWithoutLineSeparator() {
        return processWithLineSeparator();
    }

    @Override
    public boolean canProcess() {
        if (PluginStringUtil.isBlank(processStr)) {
            return false;
        }
        return processStr.contains(PREPARING) && !processStr.contains(PARAMETERS);
    }
}
