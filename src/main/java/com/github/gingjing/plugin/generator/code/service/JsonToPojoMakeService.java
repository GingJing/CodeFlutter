package com.github.gingjing.plugin.generator.code.service;

/**
 * Json生成java实体类
 *
 * @date: 2020/7/3 18:50
 * @author: gingjingdm
 * @version: 1.0
 */
public interface JsonToPojoMakeService {

    /**
     * 通过json生成文件的方法
     *
     * @param canonicalPath          生成POJO的文件夹路径
     * @param jsonString             用于生成POJO的JSON代码
     * @param className              类名
     * @param isAnnotationRequired   是否生成Gson库注解
     * @param isNeedConstructor      是否生成构建函数
     * @param isGetterSetterRequired 是否生成GetterAndSetter方法
     */
    void generate(String canonicalPath, String jsonString, String className, boolean isAnnotationRequired, boolean isNeedConstructor, boolean isGetterSetterRequired);
}
