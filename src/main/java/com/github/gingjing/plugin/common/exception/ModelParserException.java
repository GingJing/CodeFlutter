package com.github.gingjing.plugin.common.exception;

/**
 * POJO解析异常
 *
 * @author: Jmm
 * @date: 2020年 05月05日 20时48分
 * @version: 1.0
 */
public class ModelParserException extends CodeFlutterException {

    private static final long serialVersionUID = 7621084814814108836L;

    public ModelParserException(String massage) {
        super(massage);
    }

    public ModelParserException(String massage, Throwable cause) {
        super(massage, cause);
    }

    public ModelParserException(Throwable cause) {
        super(cause);
    }
}
