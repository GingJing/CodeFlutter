package com.github.gingjing.plugin.generator.code.entity;

import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.NameUtils;
import com.intellij.database.util.DasUtil;

/**
 * 生成代码的模式枚举
 *
 * @author: Jmm
 * @date: 2020年06月30日10时58分
 * @version: 1.0
 */
public enum CreateModeEnum {
    // DbTool
    DATABASE_TOOL {
        @Override
        public TableInfo getTableInfoAndConfig(TableInfoService tableInfoService, CacheDataUtils cacheDataUtils) {
            return tableInfoService.getTableInfoAndConfig(cacheDataUtils.getSelectDbTable());
        }

        @Override
        public String getConfigFileName(TableInfo tableInfo) {
            String schemaName = DasUtil.getSchema(tableInfo.getObj());
            return schemaName + "-" + tableInfo.getObj().getName() + ".json";
        }
    },
    // Create Sql
    CREATE_SQL {
        @Override
        public TableInfo getTableInfoAndConfig(TableInfoService tableInfoService, CacheDataUtils cacheDataUtils) {
            return tableInfoService.getTableInfoAndConfig(cacheDataUtils.getCurrCreateSql());
        }

        @Override
        public String getConfigFileName(TableInfo tableInfo) {
            if (!"".equals(DasUtil.getSchema(tableInfo.getObj()))) {
                String schemaName = DasUtil.getSchema(tableInfo.getObj());
                return schemaName + "-" + tableInfo.getObj().getName() + ".json";
            }
            return tableInfo.getSchemaName() + "-" + NameUtils.getInstance().hump2Underline(tableInfo.getName()) + ".json";
        }
    },
    // Java Model
    SELECT_MODEL {
        @Override
        public TableInfo getTableInfoAndConfig(TableInfoService tableInfoService, CacheDataUtils cacheDataUtils) {
            return tableInfoService.getTableInfoAndConfig(cacheDataUtils.getSelectClass());
        }

        @Override
        public String getConfigFileName(TableInfo tableInfo) {
            if (!"".equals(DasUtil.getSchema(tableInfo.getObj()))) {
                String schemaName = DasUtil.getSchema(tableInfo.getObj());
                return schemaName + "-" + tableInfo.getObj().getName() + ".json";
            }
            return tableInfo.getSchemaName() + "-" + NameUtils.getInstance().hump2Underline(tableInfo.getName()) + ".json";
        }
    };


    /**
     * 获取当前项目的当前模式的表信息
     *
     * @param tableInfoService  表服务
     * @param cacheDataUtils    缓存数据工具
     * @return
     */
    public abstract TableInfo getTableInfoAndConfig(TableInfoService tableInfoService, CacheDataUtils cacheDataUtils);

    /**
     * 获取配置文件名
     *
     * @param tableInfo 表信息
     * @return 配置文件名称
     */
    public abstract String getConfigFileName(TableInfo tableInfo);
}
