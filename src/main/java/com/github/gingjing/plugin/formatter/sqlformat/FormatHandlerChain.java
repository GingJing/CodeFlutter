package com.github.gingjing.plugin.formatter.sqlformat;

import com.github.gingjing.plugin.formatter.sqlformat.handler.FormatHandler;

/**
 * 格式化处理者链
 *
 * @author: Jmm
 * @date: 2020年05月28日15时40分
 * @version: 1.0
 */
public interface FormatHandlerChain<T extends FormatHandler<T>, E extends FormatHandlerChain<T, E>> {

    /**
     * 添加格式化处理者
     *
     * @param handler  json 格式化处理者
     * @return com.github.gingjing.gocodereasier.handler.FormatHandlerChain
     * @since v1.0.0
     * @date: 2020/6/2
     */
    FormatHandlerChain<T, E> addFormatHandler(T handler);

    /**
     * 删除格式化处理者
     *
     * @param handler json 格式化处理者
     * @return com.github.gingjing.gocodereasier.handler.FormatHandlerChain
     * @since v1.0.0
     * @date: 2020/6/2
     */
    FormatHandlerChain<T, E> removeFormatHandler(T handler);


    /**
     * 处理
     *
     * @return java.lang.Object
     * @since v1.0.0
     * @date: 2020/5/28
     */
    Object doProcess();
}
