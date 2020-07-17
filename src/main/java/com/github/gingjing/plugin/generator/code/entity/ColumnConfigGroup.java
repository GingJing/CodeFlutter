package com.github.gingjing.plugin.generator.code.entity;

import java.util.List;
import java.util.StringJoiner;

/**
 * 列配置分组
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/18 09:33
 */
public class ColumnConfigGroup implements AbstractGroup<ColumnConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<ColumnConfig> elementList;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<ColumnConfig> getElementList() {
        return elementList;
    }

    @Override
    public void setElementList(List<ColumnConfig> elementList) {
        this.elementList = elementList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnConfigGroup that = (ColumnConfigGroup) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return elementList != null ? elementList.equals(that.elementList) : that.elementList == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (elementList != null ? elementList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ColumnConfigGroup.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("elementList=" + elementList)
                .toString();
    }
}
