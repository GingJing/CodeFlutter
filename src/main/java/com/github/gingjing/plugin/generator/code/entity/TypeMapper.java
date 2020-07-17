package com.github.gingjing.plugin.generator.code.entity;

import java.util.StringJoiner;

/**
 * 类型隐射信息
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class TypeMapper {
    /**
     * 列类型
     */
    private String columnType;
    /**
     * java类型
     */
    private String javaType;

    public TypeMapper() {
    }

    public TypeMapper(String columnType, String javaType) {
        this.columnType = columnType;
        this.javaType = javaType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeMapper that = (TypeMapper) o;

        if (columnType != null ? !columnType.equals(that.columnType) : that.columnType != null) return false;
        return javaType != null ? javaType.equals(that.javaType) : that.javaType == null;
    }

    @Override
    public int hashCode() {
        int result = columnType != null ? columnType.hashCode() : 0;
        result = 31 * result + (javaType != null ? javaType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TypeMapper.class.getSimpleName() + "[", "]")
                .add("columnType='" + columnType + "'")
                .add("javaType='" + javaType + "'")
                .toString();
    }
}
