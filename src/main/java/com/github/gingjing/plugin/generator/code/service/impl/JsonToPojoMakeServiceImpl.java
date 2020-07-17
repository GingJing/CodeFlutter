package com.github.gingjing.plugin.generator.code.service.impl;


import com.github.gingjing.plugin.generator.code.config.CodeFlutterGenCodeConfigComponent;
import com.github.gingjing.plugin.generator.code.service.JsonToPojoMakeService;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * json生成pojo类
 *
 * @author: gingjingdm
 * @date: 2020年 06月27日 19时59分
 * @version: 1.0
 */
public class JsonToPojoMakeServiceImpl implements JsonToPojoMakeService {

    /**
     * 生成pojo类的文件夹路径
     */
    public static String PATH;

    public JsonToPojoMakeServiceImpl() {
    }

    /**
     * 获取实例对象
     * @param project 项目对象
     * @return 实例对象
     */
    public static JsonToPojoMakeService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, JsonToPojoMakeService.class);
    }

    /**
     * 生成方法的静态形式，不依赖project
     *
     * @see JsonToPojoMakeServiceImpl#generate(String, String, String, boolean, boolean, boolean)
     */
    public static void staticGenerate(String canonicalPath, String jsonString, String className, boolean isAnnotationRequired, boolean isNeedConstructor, boolean isGetterSetterRequired) {
        JsonToPojoMakeServiceImpl.PATH = canonicalPath;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            doGen(className, isAnnotationRequired, isNeedConstructor, isGetterSetterRequired, jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    doGen(className, isAnnotationRequired, isNeedConstructor, isGetterSetterRequired, jsonObject);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println(e1.getMessage());
            }
        }
    }


    @Override
    public void generate(String canonicalPath, String jsonString, String className, boolean isAnnotationRequired, boolean isNeedConstructor, boolean isGetterSetterRequired) {
        staticGenerate(canonicalPath, jsonString, className, isAnnotationRequired, isNeedConstructor, isGetterSetterRequired);
    }

    /**
     * 生成pojo文件
     *
     * @param className              类名
     * @param isAnnotationRequired   是否生成Gson库注解
     * @param isNeedConstructor      是否生成构建函数
     * @param isGetterSetterRequired 是否生成GetterAndSetter方法
     * @param jsonObject             json对象
     */
    private static void doGen(String className, boolean isAnnotationRequired, boolean isNeedConstructor, boolean isGetterSetterRequired, JSONObject jsonObject) {
        StringBuilder variableBuilder = new StringBuilder("");
        StringBuilder importsBuilder = new StringBuilder("");
        StringBuilder getterSetterBuilder = new StringBuilder("");
        StringBuilder constructorBuilder = new StringBuilder("");
        StringBuilder constructorAssignmentBuilder = new StringBuilder("");

        WriteCommandAction.runWriteCommandAction(
                ProjectUtils.getCurrProject(),
                () -> extractVariablesFromJsonObject(
                        jsonObject, className, variableBuilder, importsBuilder,
                        getterSetterBuilder, constructorBuilder, constructorAssignmentBuilder,
                        isAnnotationRequired, isNeedConstructor, isGetterSetterRequired
                )
        );
    }

    /**
     * 生成文件
     *
     * @param name 文件名
     * @param path 文件夹路径
     * @return 生成的文件
     */
    private static File createFile(String name, String path) {
        File file = new File(path + "/" + name + ".java");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 从json对象中提取变量并写入文件
     *
     * @param jsonObject                   Json对象
     * @param name                         文件名
     * @param variableBuilder              属性字符串拼接
     * @param importsBuilder               导包字符串拼接
     * @param getterSetterBuilder          getter、setter方法字符串拼接
     * @param constructorBuilder           构造方法字符串拼接
     * @param constructorAssignmentBuilder 构造方法字符串拼接
     * @param isAnnotationRequired         是否生成Gson注解
     * @param isNeedConstructor            是否生成构造方法
     * @param isGetterSetterRequired       是否生成setter、getter方法
     */
    private static void extractVariablesFromJsonObject(JSONObject jsonObject, String name, StringBuilder variableBuilder, StringBuilder importsBuilder, StringBuilder getterSetterBuilder, StringBuilder constructorBuilder, StringBuilder constructorAssignmentBuilder, boolean isAnnotationRequired, boolean isNeedConstructor, boolean isGetterSetterRequired) {
        if (jsonObject == null) {
            return;
        }
        File file = createFile(name, PATH);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isAnnotationRequired) {
            importsBuilder.append("import com.google.gson.annotations.Expose;\n" + "import com.google.gson.annotations.SerializedName;").append("\n");
        }
        Iterator<String> keyIterator = jsonObject.keys();
        if (isNeedConstructor) {
            constructorBuilder.append("  public ").append(name).append("(){").append("\n").append("  }\n");
            constructorBuilder.append("  public ").append(name).append("(");
        }
        while (keyIterator.hasNext()) {
            String field = keyIterator.next();
            if (isAnnotationRequired) {
                variableBuilder.append("  @SerializedName(").append("\"").append(field).append("\"").append(")").append("\n");
                variableBuilder.append("  @Expose").append("\n");
            }
            try {
                StringBuilder fieldBuilder = new StringBuilder(field);
                Integer intValue = jsonObject.getInt(field);
                variableBuilder.append("  private Integer ").append(field).append(";");
                variableBuilder.append("\n");
                if (isNeedConstructor) {
                    constructorBuilder.append("Integer ").append(field).append(",");
                    constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                }
                String c = fieldBuilder.substring(0, 1);
                fieldBuilder.deleteCharAt(0);
                fieldBuilder.insert(0, c.toUpperCase());
                createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "Integer");
            } catch (JSONException e) {
                try {
                    jsonObject.getLong(field);
                    variableBuilder.append("  private Long ").append(field).append(";");
                    variableBuilder.append("\n");
                    if (isNeedConstructor) {
                        constructorBuilder.append("Long ").append(field).append(",");
                        constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                    }
                    StringBuilder fieldBuilder = new StringBuilder(field);
                    String c = fieldBuilder.substring(0, 1);
                    fieldBuilder.deleteCharAt(0);
                    fieldBuilder.insert(0, c.toUpperCase());
                    createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "Long");
                } catch (JSONException e1) {
                    try {
                        jsonObject.getDouble(field);
                        variableBuilder.append("  private Double ").append(field).append(";");
                        variableBuilder.append("\n");
                        if (isNeedConstructor) {
                            constructorBuilder.append("Double ").append(field).append(",");
                            constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                        }
                        StringBuilder fieldBuilder = new StringBuilder(field);
                        String c = fieldBuilder.substring(0, 1);
                        fieldBuilder.deleteCharAt(0);
                        fieldBuilder.insert(0, c.toUpperCase());
                        createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "Double");
                    } catch (JSONException e2) {
                        try {
                            jsonObject.getBoolean(field);
                            variableBuilder.append("  private Boolean ").append(field).append(";");
                            variableBuilder.append("\n");
                            if (isNeedConstructor) {
                                constructorBuilder.append("Boolean ").append(field).append(",");
                                constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                            }
                            StringBuilder fieldBuilder = new StringBuilder(field);
                            String c = fieldBuilder.substring(0, 1);
                            fieldBuilder.deleteCharAt(0);
                            fieldBuilder.insert(0, c.toUpperCase());
                            createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "Boolean");
                        } catch (JSONException e3) {
                            try {
                                jsonObject.getString(field);
                                variableBuilder.append("  private String ").append(field).append(";");
                                variableBuilder.append("\n");
                                if (isNeedConstructor) {
                                    constructorBuilder.append("String ").append(field).append(",");
                                    constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                                }
                                StringBuilder fieldBuilder = new StringBuilder(field);
                                String c = fieldBuilder.substring(0, 1);
                                fieldBuilder.deleteCharAt(0);
                                fieldBuilder.insert(0, c.toUpperCase());
                                createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "String");
                            } catch (JSONException e4) {
                                try {
                                    JSONObject newObject = jsonObject.getJSONObject(field);
                                    StringBuilder fieldBuilder = new StringBuilder(field);
                                    String c = fieldBuilder.substring(0, 1);
                                    fieldBuilder.deleteCharAt(0);
                                    fieldBuilder.insert(0, c.toUpperCase());
                                    variableBuilder.append("  private ").append(fieldBuilder).append(" ").append(field).append(";");
                                    variableBuilder.append("\n");
                                    if (isNeedConstructor) {
                                        constructorBuilder.append(fieldBuilder).append(" ").append(field).append(",");
                                        constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                                    }
                                    StringBuilder temp = new StringBuilder("");
                                    StringBuilder imports = new StringBuilder("");
                                    StringBuilder gsTemp = new StringBuilder("");
                                    StringBuilder constructorTemp = new StringBuilder("");
                                    StringBuilder constructorAssignTemp = new StringBuilder("");
                                    createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, fieldBuilder.toString());
                                    extractVariablesFromJsonObject(newObject, (fieldBuilder.toString()), temp, imports, gsTemp, constructorTemp, constructorAssignTemp, isAnnotationRequired, isNeedConstructor, isGetterSetterRequired);
                                } catch (JSONException e5) {
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray(field);
                                        if (!importsBuilder.toString().contains("List")) {
                                            importsBuilder.append("import java.util.List;").append("\n");
                                        }
                                        StringBuilder fieldBuilder = new StringBuilder(field);
                                        String c = fieldBuilder.substring(0, 1);
                                        fieldBuilder.deleteCharAt(0);
                                        fieldBuilder.insert(0, c.toUpperCase());
                                        variableBuilder.append("  private List<").append(fieldBuilder).append("> ").append(field).append(";");
                                        variableBuilder.append("\n");
                                        if (isNeedConstructor) {
                                            constructorBuilder.append("List<").append(fieldBuilder).append("> ").append(field).append(",");
                                            constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                                        }
                                        JSONObject newObject = null;
                                        if (jsonArray.length() > 0) {
                                            newObject = jsonArray.getJSONObject(0);
                                        }
                                        StringBuilder temp = new StringBuilder("");
                                        StringBuilder imports = new StringBuilder("");
                                        StringBuilder gsTemp = new StringBuilder("");
                                        StringBuilder constructorTemp = new StringBuilder("");
                                        StringBuilder constructorAssignTemp = new StringBuilder("");
                                        createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "List<" + (fieldBuilder.toString()) + ">");
                                        if (newObject != null) {
                                            extractVariablesFromJsonObject(newObject, (fieldBuilder.toString()), temp, imports, gsTemp, constructorTemp, constructorAssignTemp, isAnnotationRequired, isNeedConstructor, isGetterSetterRequired);
                                        }
                                    } catch (JSONException e6) {
                                        try {
                                            jsonObject.get(field);
                                            variableBuilder.append("  private Object ").append(field).append(";");
                                            variableBuilder.append("\n");
                                            if (isNeedConstructor) {
                                                constructorBuilder.append("Object ").append(field).append(",");
                                                constructorAssignmentBuilder.append("   this.").append(field).append("=").append(field).append(";").append("\n");
                                            }
                                            StringBuilder fieldBuilder = new StringBuilder(field);
                                            String c = fieldBuilder.substring(0, 1);
                                            fieldBuilder.deleteCharAt(0);
                                            fieldBuilder.insert(0, c.toUpperCase());
                                            createGetterSetterMathods(getterSetterBuilder, fieldBuilder, field, "Object");
                                        } catch (JSONException e7) {
                                            e7.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try {

            if (bufferedWriter != null) {
                createFileStructure(name, bufferedWriter, importsBuilder);
                bufferedWriter.write(variableBuilder.toString());
                if (isNeedConstructor) {
                    constructorBuilder.setLength(constructorBuilder.length() - 1);
                    constructorBuilder.append("){").append("\n");
                    bufferedWriter.write(constructorBuilder.toString());
                    bufferedWriter.write(constructorAssignmentBuilder.toString());
                    bufferedWriter.write("  }\n");
                }
                if (isGetterSetterRequired) {
                    bufferedWriter.write(getterSetterBuilder.toString());
                }
                bufferedWriter.write("}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成getter、setter方法
     *
     * @param getterSetterBuilder     getter、setter方法StringBuilder
     * @param fieldBuilder            字段StringBuilder
     * @param field                   字段
     * @param dataType                日期类型
     */
    private static void createGetterSetterMathods(StringBuilder getterSetterBuilder, StringBuilder fieldBuilder, String field, String dataType) {
        getterSetterBuilder.append("  public void set").append(fieldBuilder).append("(").append(dataType).append(" ").append(field)
                .append("){").append("\n").append("   this.").append(field).append("=").append(field).append(";").append("\n").append("  }").append("\n");
        getterSetterBuilder.append("  public ").append(dataType).append(" get").append(fieldBuilder).append("(").append("){")
                .append("\n").append("   return ").append(field).append(";").append("\n").append("  }").append("\n");
    }

    /**
     * 创建文件结构
     * @param name             文件名
     * @param bufferedWriter   输出字符流
     * @param importsBuilder   导包StringBuilder
     */
    public static void createFileStructure(String name, BufferedWriter bufferedWriter, StringBuilder importsBuilder) {
        try {
            String[] splitPaths = PATH.split("/");
            StringBuilder tempBuilder = new StringBuilder("");
            int tempIndex = 0;
            boolean isNeeded = false;
            for (int index = 0; index < splitPaths.length; index++) {
                if (splitPaths[index].contains("java")) {
                    isNeeded = true;
                    tempIndex = index + 1;
                    break;
                }
            }
            if (!isNeeded) {
                tempBuilder.append(splitPaths[splitPaths.length - 1]);
            } else {
                for (int index = tempIndex; index < splitPaths.length; index++) {
                    if (index != splitPaths.length - 1) {
                        tempBuilder.append(splitPaths[index]).append(".");
                    } else {
                        tempBuilder.append(splitPaths[index]);
                    }
                }
            }
            LocalDateTime now = LocalDateTime.now();
            String time = now.getYear() + "年 " +
                    now.getMonthValue() + "月" +
                    now.getDayOfMonth() + "日 " +
                    now.getHour() + "时" +
                    now.getMinute() + "分";
            String author = CodeFlutterGenCodeConfigComponent.getInstance().getAuthor();

            bufferedWriter.write("package " + tempBuilder + ";");
            bufferedWriter.write("\n");
            bufferedWriter.write(importsBuilder.toString());
            bufferedWriter.write("/**\n" +
                    " * " + name +" Pojo\n" +
                    " * @date " + time +"\n" +
                    " * @author " + author +"\n" +
                    " */\n");
            bufferedWriter.write("public class " + name);
            bufferedWriter.write("{");
            bufferedWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
