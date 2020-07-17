package com.github.gingjing.plugin.converter.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字段容器类
 *
 * @author: GingJingDM
 * @date: 2020年 07月09日 23时04分
 * @version: 1.0
 */
public class FieldContainer<K, V> extends LinkedHashMap<K, V> {

    public FieldContainer() {
    }

    public static <K, V> FieldContainer by(K key, V value) {
        return new FieldContainer().set(key, value);
    }

    public static <K, V> FieldContainer create() {
        return new FieldContainer();
    }

    public FieldContainer set(K key, V value) {
        super.put(key, value);
        return this;
    }

    public FieldContainer set(Map map) {
        super.putAll(map);
        return this;
    }

    public FieldContainer set(FieldContainer fieldMap) {
        super.putAll(fieldMap);
        return this;
    }

    public FieldContainer delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    public String getStr(Object key) {
        return (String) get(key);
    }

    public Integer getInt(Object key) {
        return (Integer) get(key);
    }

    public Long getLong(Object key) {
        return (Long) get(key);
    }

    public Boolean getBoolean(Object key) {
        return (Boolean) get(key);
    }

    public Float getFloat(Object key) {
        return (Float) get(key);
    }


    /**
     * key 存在，并且 value 不为 null
     */
    public boolean notNull(Object key) {
        return get(key) != null;
    }

    /**
     * key 不存在，或者 key 存在但 value 为null
     */
    public boolean isNull(Object key) {
        return get(key) == null;
    }

    /**
     * key 存在，并且 value 为 true，则返回 true
     */
    public boolean isTrue(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == true));
    }

    /**
     * key 存在，并且 value 为 false，则返回 true
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == false));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String toPrettyJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public boolean equals(Object KV) {
        return KV instanceof FieldContainer && super.equals(KV);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
