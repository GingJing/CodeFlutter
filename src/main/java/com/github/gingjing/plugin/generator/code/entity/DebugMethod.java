package com.github.gingjing.plugin.generator.code.entity;

import java.util.StringJoiner;

/**
 * 调试方法实体类
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/09/03 11:10
 */
public class DebugMethod {
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法描述
     */
    private String desc;
    /**
     * 执行方法得到的值
     */
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DebugMethod that = (DebugMethod) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DebugMethod.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("desc='" + desc + "'")
                .add("value=" + value)
                .toString();
    }
}
