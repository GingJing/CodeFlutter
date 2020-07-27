package com.github.gingjing.plugin.generator.code.tool;

import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

/**
 * 缓存数据工具类
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
@Getter
@Setter
public class CacheDataUtils {
    private volatile static CacheDataUtils cacheDataUtils;

    /**
     * 单例模式
     */
    public static CacheDataUtils getInstance() {
        if (cacheDataUtils == null) {
            synchronized (CacheDataUtils.class) {
                if (cacheDataUtils == null) {
                    cacheDataUtils = new CacheDataUtils();
                }
            }
        }
        return cacheDataUtils;
    }

    private CacheDataUtils() {
    }

    /** 是否是new class */
    private Map<String, Boolean> newClassMap = new HashMap<>();

    /** 是否在调式 */
    private boolean debugging = false;

    /** 生成代码的模式 */
    private CreateModeEnum createMode;

    /** 当前用于构建的create sql语句 */
    private String currCreateSql;

    /** 当前create sql语句的database类型 */
    private String currDbType;

    /** 数据库schema名称 */
    private String schema;

    /** 选中的java类 */
    private PsiClass selectClass;

    /** 当前选中的表 */
    private DbTable selectDbTable;

    /** 所有选中的表 */
    private List<DbTable> dbTableList;

    public Boolean isNewClass(Project project) {
        return newClassMap.get(project.getBasePath()) == null ? Boolean.FALSE : newClassMap.get(project.getBasePath());
    }

    public void setNewClass(Project project, Boolean isNewClass) {
        newClassMap.put(project.getBasePath(), isNewClass);
    }

}
