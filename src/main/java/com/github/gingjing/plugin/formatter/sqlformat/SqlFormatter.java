package com.github.gingjing.plugin.formatter.sqlformat;


import com.github.gingjing.plugin.formatter.constants.SqlFormatMessageEnum;
import com.github.gingjing.plugin.formatter.sqlformat.entity.SqlEntity;

import java.util.List;


/**
 * sql格式化者
 *
 * @author: Jmm
 * @date: 2020年05月20日18时23分
 * @version: 1.0
 */
public class SqlFormatter {
    public static final String PREPARING = "Preparing: ";
    public static final String PARAMETERS = "Parameters: ";
    public static final String REGX = "Preparing: |Parameters: ";
    public static final String COMMA = ",";
    public static final String WINDOWS_LINE_SEPARATOR = "\r\n";
    public static final String LINUX_LINE_SEPARATOR = "\n";

    private SqlEntity sqlEntity;

    private String sqlParamStr;

    private List<Object> sqlParamList;

    private SqlFormatMessageEnum massage;

    public SqlEntity getSqlEntity() {
        return sqlEntity;
    }

    public void setSqlEntity(SqlEntity sqlEntity) {
        this.sqlEntity = sqlEntity;
    }

    public String getSqlParamStr() {
        return sqlParamStr;
    }

    public void setSqlParamStr(String sqlParamStr) {
        this.sqlParamStr = sqlParamStr;
    }

    public List<Object> getSqlParamList() {
        return sqlParamList;
    }

    public void setSqlParamList(List<Object> sqlParamList) {
        this.sqlParamList = sqlParamList;
    }

    public SqlFormatMessageEnum getMassage() {
        return massage;
    }

    public void setMassage(SqlFormatMessageEnum massage) {
        this.massage = massage;
    }


    public static void main(String[] args) {
        String hasParam = "dsfsfs\nddfd +=>ddf " + PREPARING + "select * from" +
                System.lineSeparator() +
                "==>dfdsfsdf==> " + PARAMETERS + "1(Integer), 2(Integer), dfsfd(String)" +
                System.lineSeparator() + "dsfsfs";
        String noParam = "ddfd +=>ddf" + PREPARING + "select * from" +
                System.lineSeparator() +
                "dfdsfsdf Para";
        String reg = "Preparing: |Parameters: ";
//        String reg = System.lineSeparator();
        System.out.println("hasPram ");
        String[] split = hasParam.split(reg);
        System.out.println(split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println(i + ": " + split[i]);
        }
        System.out.println("noPram ");
        String[] split2 = noParam.split(reg);
        System.out.println(split2.length);
        int j = 0;
        for (String s : split2) {
            System.out.println((j++) +" " + s);
        }
        String str = "dd?df?df?bbbbb";
        System.out.println("dd?df?df?bbbbb".split("\\?").length);
        System.out.println("aaaa".split(System.lineSeparator()).length);
    }
}
