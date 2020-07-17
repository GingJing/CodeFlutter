package com.github.gingjing.plugin.generator.code.entity;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 类型映射分组
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class TypeMapperGroup implements AbstractGroup<TypeMapper> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<TypeMapper> elementList;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<TypeMapper> getElementList() {
        return elementList;
    }

    @Override
    public void setElementList(List<TypeMapper> elementList) {
        this.elementList = elementList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeMapperGroup that = (TypeMapperGroup) o;

        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(elementList, that.elementList);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (elementList != null ? elementList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TypeMapperGroup.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("elementList=" + elementList)
                .toString();
    }
}
