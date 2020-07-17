package com.github.gingjing.plugin.common.exception;

/**
 * 异常
 *
 * @author: gingjingdm
 * @date: 2020年 07月01日 01时39分
 * @version: 1.0
 */
public class CodeFlutterException extends RuntimeException {

    private static final long serialVersionUID = 7621084814814108835L;

    public CodeFlutterException(String massage) {
        super(massage);
    }

    public CodeFlutterException(String massage, Throwable cause) {
        super(massage, cause);
    }

    public CodeFlutterException(Throwable cause) {
        super(cause);
    }
}
