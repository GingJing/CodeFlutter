package com.github.gingjing.plugin.generator.code.entity;

import java.util.StringJoiner;

/**
 * 回调实体类
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class Callback {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 保存路径
     */
    private String savePath;
    /**
     * 是否重新格式化代码
     */
    private boolean reformat = true;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public boolean isReformat() {
        return reformat;
    }

    public void setReformat(boolean reformat) {
        this.reformat = reformat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Callback callback = (Callback) o;

        if (reformat != callback.reformat) return false;
        if (fileName != null ? !fileName.equals(callback.fileName) : callback.fileName != null) return false;
        return savePath != null ? savePath.equals(callback.savePath) : callback.savePath == null;
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + (savePath != null ? savePath.hashCode() : 0);
        result = 31 * result + (reformat ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Callback.class.getSimpleName() + "[", "]")
                .add("fileName='" + fileName + "'")
                .add("savePath='" + savePath + "'")
                .add("reformat=" + reformat)
                .toString();
    }
}
