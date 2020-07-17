package com.github.gingjing.plugin.common.exception;

/**
 * 模板解析异常
 *
 * @author: Jmm
 * @date: 2020年 05月05日 20时47分
 * @version: 1.0
 */
public class TemplateProcessException extends CodeFlutterException {

    private static final long serialVersionUID = 7621084814814108838L;

    public TemplateProcessException(String massage) {
        super(massage);
    }

    public TemplateProcessException(String massage, Throwable cause) {

        super(massage, cause);
    }

    public TemplateProcessException(Throwable cause) {
        super(cause);
    }
}
