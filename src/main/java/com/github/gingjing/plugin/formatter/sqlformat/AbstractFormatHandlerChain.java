package com.github.gingjing.plugin.formatter.sqlformat;


import com.github.gingjing.plugin.common.exception.CodeFlutterException;
import com.github.gingjing.plugin.formatter.sqlformat.handler.FormatHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 格式化处理者链抽象类
 *
 * @author: Jmm
 * @date: 2020年06月04日18时10分
 * @version: 1.0
 */
public abstract class AbstractFormatHandlerChain<T extends FormatHandler<T>, E extends FormatHandlerChain<T, E>>
        implements FormatHandlerChain<T, E> {

    protected List<T> handlers = new LinkedList<>();

    protected T finalFormatHandler;

    @Override
    public FormatHandlerChain<T, E> addFormatHandler(T handler) {
        if (Objects.isNull(handler)) {
            return this;
        }
        handlers.add(handler);
        registerHandlers();
        return this;
    }

    @Override
    public FormatHandlerChain<T, E> removeFormatHandler(T handler) {
        if (Objects.isNull(handler)) {
            return this;
        }
        handlers.remove(handler);
        registerHandlers();
        return this;
    }

    public AbstractFormatHandlerChain<T, E> setHandler(int order, T handler) {
        if (order < 0 || order > handlers.size()) {
            throw new CodeFlutterException(String.format(
                    "order:[%s] is outOfBounds of this size:[%s] of the handlers in the chain  or the order is illegal!",
                    order, handlers.size()));
        }
        handlers.set(order, handler);
        registerHandlers();
        return this;
    }


    public void registerHandlers() {
        T tempHandler = null;
        for (int i = 0; i < handlers.size(); i++) {
            T handler = handlers.get(i);
            if (Objects.isNull(handler)) {
                continue;
            }
            if (i == 0) {
                tempHandler = handler;
                continue;
            }
            T next = tempHandler.next();
            if (Objects.isNull(next)) {
                tempHandler.setNext(handler);
            } else {
                next.setNext(handler);
            }
        }
        finalFormatHandler = tempHandler;
    }
}
