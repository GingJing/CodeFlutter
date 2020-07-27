package com.github.gingjing.plugin.generator.code.entity;

import com.github.gingjing.plugin.generator.code.ui.base.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 全局配置实体类
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/27 13:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfig implements Item {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;

}
