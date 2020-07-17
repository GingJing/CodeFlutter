package com.github.gingjing.plugin.common.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.github.gingjing.plugin.common.utils.PluginSqlUtil;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;

import java.util.HashMap;
import java.util.Map;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.TABLE_COMMENT;
import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.TABLE_NAME;

/**
 * 列、表访问者
 *
 * @author: Jmm
 * @date: 2020年 05月05日 18时49分
 * @version: 1.0
 */
public class ColumnTableAliasVisitor extends SchemaStatVisitor {



    private String dbType;

    public ColumnTableAliasVisitor(String dbType) {
        super(dbType);
        this.dbType = dbType;
    }

    public Map<String, String> map = new HashMap<>();


    @Override
    public boolean visit(SQLColumnDefinition x) {
        String tableName = null;
        SQLObject parent = x.getParent();

        if (parent instanceof SQLCreateTableStatement) {
            tableName = ((SQLCreateTableStatement) parent).getName().toString();
            SQLExpr tableComment = ((SQLCreateTableStatement) parent).getComment();
            if (tableComment.hasAfterComment()) {
                String comment = tableComment.getAfterCommentsDirect().get(0);
                map.put(TABLE_COMMENT, comment);
            } else if (tableComment.hasBeforeComment()) {
                String comment = tableComment.getBeforeCommentsDirect().get(0);
                map.put(TABLE_COMMENT, comment);
            }
        }
        if (tableName == null) {
            return true;
        } else {
            map.put(TABLE_NAME, tableName);
            String columnName = x.getName().toString();
            String normalize = PluginSqlUtil.normalize(columnName);
            String comment = x.getComment() == null ? normalize : x.getComment().toString();
            map.put(normalize, PluginStringUtil.isBlank(comment) ? normalize : PluginSqlUtil.normalize(comment));
            TableStat.Column column = this.addColumn(tableName, columnName);
            if (x.getDataType() != null) {
                column.setDataType(x.getDataType().getName());
            }

            for (Object item : x.getConstraints()) {
                if (item instanceof SQLPrimaryKey) {
                    column.setPrimaryKey(true);
                } else if (item instanceof SQLUnique) {
                    column.setUnique(true);
                }
            }

            return false;
        }
    }

    @Override
    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
