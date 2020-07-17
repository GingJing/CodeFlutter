package com.github.gingjing.plugin.generator.code.tool;

import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
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

    public Map<String, Boolean> getNewClassMap() {
        return newClassMap;
    }

    public void setNewClassMap(Map<String, Boolean> newClassMap) {
        this.newClassMap = newClassMap;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    public CreateModeEnum getCreateMode() {
        return createMode;
    }

    public void setCreateMode(CreateModeEnum createMode) {
        this.createMode = createMode;
    }

    public String getCurrCreateSql() {
        return currCreateSql;
    }

    public void setCurrCreateSql(String currCreateSql) {
        this.currCreateSql = currCreateSql;
    }

    public String getCurrDbType() {
        return currDbType;
    }

    public void setCurrDbType(String currDbType) {
        this.currDbType = currDbType;
    }

    public PsiClass getSelectClass() {
        return selectClass;
    }

    public void setSelectClass(PsiClass selectClass) {
        this.selectClass = selectClass;
    }

    public DbTable getSelectDbTable() {
        return selectDbTable;
    }

    public void setSelectDbTable(DbTable selectDbTable) {
        this.selectDbTable = selectDbTable;
    }

    public List<DbTable> getDbTableList() {
        return dbTableList;
    }

    public void setDbTableList(List<DbTable> dbTableList) {
        this.dbTableList = dbTableList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CacheDataUtils that = (CacheDataUtils) o;

        if (createMode != that.createMode) {
            return false;
        }
        if (!Objects.equals(currCreateSql, that.currCreateSql)) {
            return false;
        }
        if (!Objects.equals(selectClass, that.selectClass)) {
            return false;
        }
        if (!Objects.equals(selectDbTable, that.selectDbTable)) {
            return false;
        }

        return Objects.equals(dbTableList, that.dbTableList);
    }

    @Override
    public int hashCode() {
        int result = createMode != null ? createMode.hashCode() : 0;
        result = 31 * result + (currCreateSql != null ? currCreateSql.hashCode() : 0);
        result = 31 * result + (selectClass != null ? selectClass.hashCode() : 0);
        result = 31 * result + (selectDbTable != null ? selectDbTable.hashCode() : 0);
        result = 31 * result + (dbTableList != null ? dbTableList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CacheDataUtils.class.getSimpleName() + "[", "]")
                .add("mode=" + createMode)
                .add("currCreateSql='" + currCreateSql + "'")
                .add("selectClass=" + selectClass)
                .add("selectDbTable=" + selectDbTable)
                .add("dbTableList=" + dbTableList)
                .toString();
    }


}
