package com.github.gingjing.plugin.generator.code.entity;

import com.github.gingjing.plugin.generator.code.ui.base.Item;
import lombok.Data;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 模板信息类
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
@Data
public class Template implements Item {
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板代码
     */
    private String code;

    public Template() {
    }

    public Template(String name, String code) {
        this.name = name;
        this.code = code;
    }

}
