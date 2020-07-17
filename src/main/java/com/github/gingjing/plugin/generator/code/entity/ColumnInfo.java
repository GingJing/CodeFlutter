package com.github.gingjing.plugin.generator.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellij.database.model.DasColumn;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 列信息
 *
 * @author gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class ColumnInfo {
    /**
     * 原始对象
     */
    @JsonIgnore
    private DasColumn obj;
    /**
     * 名称
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 全类型
     */
    private String type;
    /**
     * 短类型
     */
    private String shortType;
    /**
     * 标记是否为自定义附加列
     */
    private boolean custom;
    /** 是否主键 */
    private boolean isPk;
    /**
     * 扩展数据
     */
    private Map<String, Object> ext;

    public DasColumn getObj() {
        return obj;
    }

    public void setObj(DasColumn obj) {
        this.obj = obj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShortType() {
        return shortType;
    }

    public void setShortType(String shortType) {
        this.shortType = shortType;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isPk() {
        return isPk;
    }

    public void setPk(boolean pk) {
        isPk = pk;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ColumnInfo that = (ColumnInfo) o;

        if (custom != that.custom) {
            return false;
        }
        if (!Objects.equals(obj, that.obj)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(comment, that.comment)) {
            return false;
        }
        if (!Objects.equals(type, that.type)) {
            return false;
        }
        if (!Objects.equals(shortType, that.shortType)) {
            return false;
        }
        return Objects.equals(ext, that.ext);
    }

    @Override
    public int hashCode() {
        int result = obj != null ? obj.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (shortType != null ? shortType.hashCode() : 0);
        result = 31 * result + (custom ? 1 : 0);
        result = 31 * result + (ext != null ? ext.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ColumnInfo.class.getSimpleName() + "[", "]")
                .add("obj=" + obj)
                .add("name='" + name + "'")
                .add("comment='" + comment + "'")
                .add("type='" + type + "'")
                .add("shortType='" + shortType + "'")
                .add("custom=" + custom)
                .add("ext=" + ext)
                .toString();
    }
}
