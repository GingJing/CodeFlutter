package com.github.gingjing.plugin.common.exception;

/**
 * SQL建表语句解析异常
 *
 * @author: Jmm
 * @date: 2020年 05月05日 20时47分
 * @version: 1.0
 */
public class SqlParserException extends CodeFlutterException {

    private static final long serialVersionUID = 7621084814814108837L;

    public SqlParserException(String massage) {
        super(massage);
    }

    public SqlParserException(String massage, Throwable cause) {

        super(massage, cause);
    }

    public SqlParserException(Throwable cause) {
        super(cause);
    }
}
