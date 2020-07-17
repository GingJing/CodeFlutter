package com.github.gingjing.plugin.generator.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.psi.DbTable;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 表信息
 *
 * @author gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class TableInfo {
    /**
     * 原始对象
     */
    @JsonIgnore
    private DbTable obj;

    /** schema名 */
    String schemaName;
    /**
     * 表名（首字母大写）
     */
    private String name;
    /**
     * 表名前缀
     */
    private String preName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 模板组名称
     */
    private String templateGroupName;
    /**
     * 所有列
     */
    private List<ColumnInfo> fullColumn;
    /**
     * 主键列
     */
    private List<ColumnInfo> pkColumn;
    /**
     * 其他列
     */
    private List<ColumnInfo> otherColumn;
    /**
     * 保存的包名称
     */
    private String savePackageName;
    /**
     * 保存路径
     */
    private String savePath;
    /**
     * 保存的model名称
     */
    private String saveModelName;

    public DbTable getObj() {
        return obj;
    }

    public void setObj(DbTable obj) {
        this.obj = obj;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTemplateGroupName() {
        return templateGroupName;
    }

    public void setTemplateGroupName(String templateGroupName) {
        this.templateGroupName = templateGroupName;
    }

    public List<ColumnInfo> getFullColumn() {
        return fullColumn;
    }

    public void setFullColumn(List<ColumnInfo> fullColumn) {
        this.fullColumn = fullColumn;
    }

    public List<ColumnInfo> getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(List<ColumnInfo> pkColumn) {
        this.pkColumn = pkColumn;
    }

    public List<ColumnInfo> getOtherColumn() {
        return otherColumn;
    }

    public void setOtherColumn(List<ColumnInfo> otherColumn) {
        this.otherColumn = otherColumn;
    }

    public String getSavePackageName() {
        return savePackageName;
    }

    public void setSavePackageName(String savePackageName) {
        this.savePackageName = savePackageName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveModelName() {
        return saveModelName;
    }

    public void setSaveModelName(String saveModelName) {
        this.saveModelName = saveModelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableInfo tableInfo = (TableInfo) o;

        if (!Objects.equals(obj, tableInfo.obj)) {
            return false;
        }
        if (!Objects.equals(name, tableInfo.name)) {
            return false;
        }
        if (!Objects.equals(preName, tableInfo.preName)) {
            return false;
        }
        if (!Objects.equals(comment, tableInfo.comment)) {
            return false;
        }
        if (!Objects.equals(templateGroupName, tableInfo.templateGroupName)) {
            return false;
        }
        if (!Objects.equals(fullColumn, tableInfo.fullColumn)) {
            return false;
        }
        if (!Objects.equals(pkColumn, tableInfo.pkColumn)) {
            return false;
        }
        if (!Objects.equals(otherColumn, tableInfo.otherColumn)) {
            return false;
        }
        if (!Objects.equals(savePackageName, tableInfo.savePackageName)) {
            return false;
        }
        if (!Objects.equals(savePath, tableInfo.savePath)) {
            return false;
        }
        return Objects.equals(saveModelName, tableInfo.saveModelName);
    }

    @Override
    public int hashCode() {
        int result = obj != null ? obj.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (preName != null ? preName.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (templateGroupName != null ? templateGroupName.hashCode() : 0);
        result = 31 * result + (fullColumn != null ? fullColumn.hashCode() : 0);
        result = 31 * result + (pkColumn != null ? pkColumn.hashCode() : 0);
        result = 31 * result + (otherColumn != null ? otherColumn.hashCode() : 0);
        result = 31 * result + (savePackageName != null ? savePackageName.hashCode() : 0);
        result = 31 * result + (savePath != null ? savePath.hashCode() : 0);
        result = 31 * result + (saveModelName != null ? saveModelName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TableInfo.class.getSimpleName() + "[", "]")
                .add("obj=" + obj)
                .add("name='" + name + "'")
                .add("preName='" + preName + "'")
                .add("comment='" + comment + "'")
                .add("templateGroupName='" + templateGroupName + "'")
                .add("fullColumn=" + fullColumn)
                .add("pkColumn=" + pkColumn)
                .add("otherColumn=" + otherColumn)
                .add("savePackageName='" + savePackageName + "'")
                .add("savePath='" + savePath + "'")
                .add("saveModelName='" + saveModelName + "'")
                .toString();
    }

}
