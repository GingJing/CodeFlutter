package com.github.gingjing.plugin.common.utils;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.github.gingjing.plugin.common.exception.CodeGenerateException;
import com.github.gingjing.plugin.common.exception.SqlParserException;
import com.github.gingjing.plugin.common.visitor.ColumnTableAliasVisitor;
import com.github.gingjing.plugin.formatter.constants.SqlFormatMessageEnum;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;
import com.github.gingjing.plugin.formatter.sqlformat.entity.SqlEntity;
import com.github.gingjing.plugin.generator.code.entity.ColumnInfo;
import com.github.gingjing.plugin.generator.code.entity.TableInfo;
import com.github.gingjing.plugin.generator.code.entity.TypeMapper;
import com.github.gingjing.plugin.generator.code.tool.CurrGroupUtils;
import com.github.gingjing.plugin.generator.code.tool.NameUtils;
import com.intellij.util.ExceptionUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * sql工具类
 *
 * @author: gingjingdm
 * @date: 2020年 07月30日 22时03分
 * @version: 1.0
 */
public class PluginSqlUtil {

    public static final String TABLE_NAME = "tableName";
    public static final String TABLE_COMMENT = "tableComment";

    /**
     * 格式化sql
     *
     * @param sql    需要格式化的sql
     * @param dbType 数据库类型
     * @return 格式化后的sql字符串
     */
    public static String format(String sql, String dbType) {
        return format(sql, dbType, null);
    }

    /**
     * 格式化sql
     *
     * @param sql    需要格式化的sql
     * @param dbType 数据库类型
     * @param params 参数
     * @return 格式化后的sql字符串
     */
    public static String format(String sql, String dbType, List<Object> params) {
        if (PluginStringUtil.isBlank(sql)) {
            return null;
        }
        if (PluginStringUtil.isBlank(dbType)) {
            dbType = JdbcConstants.MYSQL;
        }
        if (CollectionUtils.isEmpty(params)) {
            return SQLUtils.format(sql, dbType);
        }
        return SQLUtils.format(sql, dbType, params);
    }

    /**
     * 直接处理（即传入的字符串不包含参数列表）待分析的sql语句，将结果封装为SqlFormatter对象
     *
     * @param str         待处理的字符串
     * @param placeHolder 占位符
     * @param dbType      数据库类型
     * @return com.github.gingjing.gocodereasier.bean.SqlFormatter
     * @date: 2020/5/25
     * @since v1.0.0
     */
    public static SqlFormatter directProcess(String str, String placeHolder, String dbType) {
        if (PluginStringUtil.isBlank(str)) {
            return null;
        }
        if (PluginStringUtil.isBlank(placeHolder)) {
            placeHolder = "?";
        }
        if (PluginStringUtil.isBlank(dbType)) {
            dbType = JdbcConstants.MYSQL;
        }
        return makeupSqlFormatterNoParamsStr(placeHolder, dbType, str);
    }

    /**
     * 通过传入的参数组装sqlFormatter对象，此sqlFormatter对象sql参数列表相关属性为空
     *
     * @param placeHolder 占位符
     * @param dbType      数据库类型
     * @param preSqlStr   预编译后的sql
     * @return com.github.gingjing.gocodereasier.bean.SqlFormatter
     * @date: 2020/5/25
     * @since v1.0.0
     */
    public static SqlFormatter makeupSqlFormatterNoParamsStr(String placeHolder, String dbType, String preSqlStr) {
        SqlEntity sqlEntity;
        SqlFormatter sqlFormatter;
        int count = PluginStringUtil.containsCount(preSqlStr, placeHolder);
        sqlEntity = new SqlEntity();
        sqlEntity.setPrepareSql(SQLUtils.format(preSqlStr, dbType));
        sqlEntity.setWithoutFormatSql(preSqlStr);
        sqlEntity.setPlaceHolderCount(String.valueOf(count));
        sqlEntity.setRealSql(sqlEntity.getPrepareSql());
        sqlFormatter = new SqlFormatter();
        sqlFormatter.setSqlEntity(sqlEntity);
        sqlFormatter.setSqlParamStr("");
        sqlFormatter.setSqlParamList(new ArrayList<>());
        if (count > 0) {
            sqlFormatter.setMassage(SqlFormatMessageEnum.NO_PARAM_LIST);
        } else {
            sqlFormatter.setMassage(SqlFormatMessageEnum.NO_PLACEHOLDER);
        }
        return sqlFormatter;
    }

    /**
     * 通过参数组装sqlFormatter对象
     *
     * @param placeHolder 占位符
     * @param dbType      数据库类型
     * @param preSqlStr   预编译后的sql
     * @param paramStr    参数字符串
     * @return com.github.gingjing.gocodereasier.bean.SqlFormatter
     * @date: 2020/5/25
     * @since v1.0.0
     */
    public static SqlFormatter makeupSqlFormatter(String placeHolder, String dbType, String preSqlStr, String paramStr) {
        SqlEntity sqlEntity;
        SqlFormatter sqlFormatter;
        if (PluginStringUtil.isNotBlank(paramStr)) {
            paramStr = paramStr.split(System.lineSeparator())[0].trim();
        }
        int count = PluginStringUtil.containsCount(preSqlStr, placeHolder);
        List<Object> parser = parser(paramStr);
        if (count != parser.size()) {
            ExceptionUtil.rethrow(new SqlParserException("The param count of the sql in the str you pass unequals the param statement given in the str"));
        }
        sqlEntity = new SqlEntity();
        sqlEntity.setPlaceHolderCount(String.valueOf(count));
        sqlEntity.setWithoutFormatSql(preSqlStr);
        sqlEntity.setPrepareSql(SQLUtils.format(preSqlStr, dbType));
        sqlEntity.setRealSql(SQLUtils.format(preSqlStr, dbType, parser));
        sqlFormatter = new SqlFormatter();
        sqlFormatter.setSqlParamList(parser);
        sqlFormatter.setSqlEntity(sqlEntity);
        sqlFormatter.setSqlParamStr(paramStr);
        if (count > 0) {
            sqlFormatter.setMassage(SqlFormatMessageEnum.ALL);
        } else {
            sqlFormatter.setMassage(SqlFormatMessageEnum.NO_PLACEHOLDER);
        }
        return sqlFormatter;
    }

    /**
     * 将传入的sql参数解析为List
     *
     * @param paramStr sql参数字符串
     * @return java.util.List<java.lang.Object>
     * @date: 2020/5/25
     * @since v1.0.0
     */
    public static List<Object> parser(String paramStr) {
        String[] paramStrArr = PluginStringUtil.isNotBlank(paramStr) ? paramStr.split(",") : new String[0];
        if (paramStrArr.length == 0) {
            return new ArrayList<>();
        }
        List<Object> paramList = new ArrayList<>();
        for (String param : paramStrArr) {
            String trim = param.trim();
            if ("null".equals(trim)) {
                paramList.add("null");
            }
            paramList.add(trim.contains("(") ? trim.substring(0, trim.lastIndexOf("(")) : trim);
        }
        return paramList;
    }


    public static String normalize(String name) {

        if (name == null) {
            return null;
        } else {
            if (name.length() > 2) {
                char c0 = name.charAt(0);
                char x0 = name.charAt(name.length() - 1);
                boolean isQuotes = (c0 == '"' && x0 == '"');
                boolean isBackSingleQuote = (c0 == '`' && x0 == '`');
                boolean isEscapeSingleQuote = (c0 == '\'' && x0 == '\'');
                if (isQuotes || isBackSingleQuote || isEscapeSingleQuote) {
                    String normalizeName = name.substring(1, name.length() - 1);
                    if (c0 == '`') {
                        normalizeName = normalizeName.replaceAll("`\\.`", ".");
                    }
                    return normalizeName;
                }
            }
            return name;
        }
    }

    public static TableInfo initTableAndColumn(String sql, String dbType) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        ColumnTableAliasVisitor visitor = new ColumnTableAliasVisitor(dbType);
        if (stmtList == null) {
            throw new CodeGenerateException("代码生成失败，sql语句解析失败！");
        }
        if (stmtList.size() > 1) {
            throw new CodeGenerateException("代码生成失败，只支持单条建表语句！");
        }
        SQLStatement sqlStatement = stmtList.get(0);
        sqlStatement.accept(visitor);
        Collection<TableStat.Column> columns = visitor.getColumns();
        if (PluginStringUtil.isBlank(visitor.map.get(TABLE_NAME))) {
            throw new CodeGenerateException("代码生成失败，表名不能为空");
        }
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(visitor.map.get(TABLE_NAME));

        tableInfo.setComment(visitor.map.get(TABLE_COMMENT));
        List<ColumnInfo> fullColumns = tableInfo.getFullColumn();
        List<ColumnInfo> pkColumns = tableInfo.getPkColumn();
        List<ColumnInfo> otherColumns = tableInfo.getOtherColumn();
        for (TableStat.Column column : columns) {
            ColumnInfo columnInfo = new ColumnInfo();
            String columnName = column.getName();
            columnInfo.setName(PluginStringUtil.toJavaClassName(normalize(columnName)));
            columnInfo.setType(getColumnType(column.getDataType()));
            columnInfo.setComment(visitor.map.get(normalize(normalize(columnName))));
            columnInfo.setCustom(false);
            columnInfo.setShortType(NameUtils.getInstance().getClsNameByFullName(column.getDataType()));
            columnInfo.setObj(null);
            columnInfo.setExt(new LinkedHashMap<>());
            if (column.isPrimaryKey()) {
                columnInfo.setPk(true);
                pkColumns.add(columnInfo);
            } else {
                columnInfo.setPk(false);
                otherColumns.add(columnInfo);
            }
            fullColumns.add(columnInfo);
        }
        tableInfo.setFullColumn(fullColumns);
        tableInfo.setPkColumn(pkColumns);
        tableInfo.setOtherColumn(otherColumns);
        return tableInfo;
    }

    public static String getColumnType(String typeName) {
        for (TypeMapper typeMapper : CurrGroupUtils.getCurrTypeMapperGroup().getElementList()) {
            // 不区分大小写进行类型转换
            if (Pattern.compile(typeMapper.getColumnType(), Pattern.CASE_INSENSITIVE).matcher(typeName).matches()) {
                return typeMapper.getJavaType();
            }
        }
        // 没找到直接返回Object
        return "java.lang.Object";
    }
}
