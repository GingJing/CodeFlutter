package com.github.gingjing.plugin.generator.code.entity;

import java.util.List;
import java.util.StringJoiner;

/**
 * 全局配置分组
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/27 13:10
 */
public class GlobalConfigGroup implements AbstractGroup<GlobalConfig> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象集合
     */
    private List<GlobalConfig> elementList;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<GlobalConfig> getElementList() {
        return elementList;
    }

    @Override
    public void setElementList(List<GlobalConfig> elementList) {
        this.elementList = elementList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlobalConfigGroup that = (GlobalConfigGroup) o;

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
        return new StringJoiner(", ", GlobalConfigGroup.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("elementList=" + elementList)
                .toString();
    }
}
