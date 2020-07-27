package com.github.gingjing.plugin.generator.code.entity;

import lombok.Data;

import java.util.List;
import java.util.StringJoiner;

/**
 * 模板分组类
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/18 09:33
 */
@Data
public class TemplateGroup implements AbstractGroup<Template> {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 元素对象
     */
    private List<Template> elementList;

}
