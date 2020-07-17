package com.github.gingjing.plugin.common.exception;

/**
 * 代码生成异常
 *
 * @author: Jmm
 * @date: 2020年 05月05日 20时47分
 * @version: 1.0
 */
public class CodeGenerateException extends RuntimeException {

    private static final long serialVersionUID = 7621084814814108836L;

    public CodeGenerateException(String massage) {

        super(massage);
    }

    public CodeGenerateException(String massage, Throwable cause) {

        super(massage, cause);
    }

    public CodeGenerateException(Throwable cause) {
        super(cause);
    }
}
