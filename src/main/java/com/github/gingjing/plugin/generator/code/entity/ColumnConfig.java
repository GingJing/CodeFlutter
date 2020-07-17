package com.github.gingjing.plugin.generator.code.entity;

import java.util.StringJoiner;

/**
 * 列配置信息
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class ColumnConfig {
    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private ColumnConfigType type;
    /**
     * 可选值，逗号分割
     */
    private String selectValue;

    public ColumnConfig() {
    }

    public ColumnConfig(String title, ColumnConfigType type) {
        this.title = title;
        this.type = type;
    }

    public ColumnConfig(String title, ColumnConfigType type, String selectValue) {
        this.title = title;
        this.type = type;
        this.selectValue = selectValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ColumnConfigType getType() {
        return type;
    }

    public void setType(ColumnConfigType type) {
        this.type = type;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnConfig that = (ColumnConfig) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (type != that.type) return false;
        return selectValue != null ? selectValue.equals(that.selectValue) : that.selectValue == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (selectValue != null ? selectValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ColumnConfig.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("type=" + type)
                .add("selectValue='" + selectValue + "'")
                .toString();
    }
}
