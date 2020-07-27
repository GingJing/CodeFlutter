package com.github.gingjing.plugin.generator.code.tool;

import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.intellij.database.model.DasObject;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.intellij.database.util.DasUtil.getTables;

/**
 * IDEA项目相关工具
 *
 * @author gingjingdm
 * @version 1.0.0
 * @since 2020/05/14 18:35
 */
public class ProjectUtils {

    /**
     * 获取当前项目对象
     *
     * @return 当前项目对象
     */
    public static Project getCurrProject() {

        ProjectManager projectManager = ProjectManager.getInstance();
        Project[] openProjects = projectManager.getOpenProjects();
        if (openProjects.length == 0) {
            //正常情况下不会发生
            return projectManager.getDefaultProject();
        } else if (openProjects.length == 1) {
            // 只存在一个打开的项目则使用打开的项目
            return openProjects[0];
        }

        //如果有项目窗口处于激活状态
        try {
            WindowManager wm = WindowManager.getInstance();
            for (Project project : openProjects) {
                Window window = wm.suggestParentWindow(project);
                if (window != null && window.isActive()) {
                    return project;
                }
            }
        } catch (Exception ignored) {
        }

        //否则使用默认项目
        return projectManager.getDefaultProject();
    }

    public static List<DbTable> getDbTables(Project project) {
        if (project == null) {
            project = getCurrProject();
        }
        List<DbDataSource> dataSourceList = DbPsiFacade.getInstance(project).getDataSources();
        List<DbTable> list = new ArrayList<>();
        if (!CollectionUtil.isEmpty(dataSourceList)) {
            for (DbDataSource dbDataSource : dataSourceList) {
                for (DasTable dasTable : getTables(dbDataSource)) {
                    DbTable dbTable = null;
                    if (dasTable instanceof DbTable) {
                        // 针对2017.2版本做兼容
                        dbTable = (DbTable) dasTable;
                    } else {
                        Method method = ReflectionUtil.getMethod(DbPsiFacade.class, "findElement", DasObject.class);
                        if (method == null) {
                            Messages.showWarningDialog("findElement method not found", MsgValue.TITLE_INFO);
                            return null;
                        }
                        try {
                            // 针对2017.2以上版本做兼容
                            dbTable = (DbTable) method.invoke(DbPsiFacade.getInstance(project), dasTable);
                        } catch (IllegalAccessException| InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (dbTable != null) {
                        list.add(dbTable);
                    }
                }
            }
        }
        return list;
    }

    private static String getClassName(String str) {
        return NameUtils.getInstance().getClassName(str);
    }

    public static DbTable getDbTableByPsiClass(@NotNull Project project, @NotNull PsiClass psiClass) {
        return getDbTableByTableName(project, NameUtils.getInstance().hump2Underline(psiClass.getName()));
    }

    public static DbTable getDbTableByTableName(Project project, String name) {
        List<DbDataSource> dataSourceList = DbPsiFacade.getInstance(project).getDataSources();
        DasTable dasTable = null;
        if (!CollectionUtil.isEmpty(dataSourceList)) {
            for (DbDataSource dbDataSource : dataSourceList) {
                for (DasTable table : getTables(dbDataSource)) {
                    if (Objects.equals(table.toString(), name)) {
                        dasTable = table;
                        break;
                    }
                }
            }
        }
        if (dasTable == null) {
            return null;
        }
        DbTable dbTable = null;
        if (dasTable instanceof DbTable) {
            // 针对2017.2版本做兼容
            dbTable = (DbTable) dasTable;
        } else {
            Method method = ReflectionUtil.getMethod(DbPsiFacade.class, "findElement", DasObject.class);
            if (method == null) {
                Messages.showWarningDialog("findElement method not found", MsgValue.TITLE_INFO);
                return null;
            }
            try {
                // 针对2017.2以上版本做兼容
                dbTable = (DbTable) method.invoke(DbPsiFacade.getInstance(project), dasTable);
            } catch (IllegalAccessException| InvocationTargetException e1) {
                ExceptionUtil.rethrow(e1);
            }
        }
        return dbTable;
    }
}


