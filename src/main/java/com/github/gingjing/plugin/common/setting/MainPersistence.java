package com.github.gingjing.plugin.common.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * 持久化
 *
 * @author: GingJingDM
 * @date: 2020年 07月06日 21时32分
 * @version: 1.0
 */
@State(name = "CodeFlutterSetting", storages = @Storage("code-flutter.xml"))
public class MainPersistence implements PersistentStateComponent<MainPersistence> {

    private String note;

    /**
     * 获取单例实例对象
     *
     * @return 实例对象
     */
    public static MainPersistence getInstance() {
        return ServiceManager.getService(MainPersistence.class);
    }

    @Nullable
    @Override
    public MainPersistence getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MainPersistence mainPersistence) {
        XmlSerializerUtil.copyBean(mainPersistence, this);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MainPersistence that = (MainPersistence) o;

        return Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return note != null ? note.hashCode() : 0;
    }
}
