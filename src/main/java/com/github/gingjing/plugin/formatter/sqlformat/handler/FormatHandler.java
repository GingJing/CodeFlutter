package com.github.gingjing.plugin.formatter.sqlformat.handler;


/**
 * 格式化处理者接口
 *
 * @author: Jmm
 * @date: 2020年05月28日15时42分
 * @version: 1.0
 */
public interface FormatHandler<T> {

    /**
     * 是否可以处理
     *
     * @return boolean
     * @since v1.0.0
     * @date: 2020/5/29
     */
    boolean canProcess();

    /**
     * 格式化业务逻辑处理
     *
     * @return java.lang.Object
     * @since v1.0.0
     * @date: 2020/5/29
     */
    Object process();

    /**
     * 下一个处理者
     *
     * @return com.github.gingjing.gocodereasier.handler.FormatHandler
     * @since v1.0.0
     * @date: 2020/5/29
     */
    T next();

    /**
     * 设置处理者
     *
     * @param handler 处理者
     * @since v1.0.0
     * @date: 2020/6/
     */
    void setNext(T handler);
}
