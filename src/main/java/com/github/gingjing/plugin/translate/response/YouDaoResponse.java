package com.github.gingjing.plugin.translate.response;

import java.util.List;

/**
 * 有道响应结果
 *
 * @author: gingjingdm
 * @date: 2020年 07月04日 19时31分
 * @version: 1.0
 */
public class YouDaoResponse {

    private String type;
    private int errorCode;
    private int elapsedTime;
    private List<List<TranslateResult>> translateResult;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setTranslateResult(List<List<TranslateResult>> translateResult) {
        this.translateResult = translateResult;
    }

    public List<List<TranslateResult>> getTranslateResult() {
        return translateResult;
    }

    public static String getIdentity() {
        return "YouDao%s";
    }


    public static class TranslateResult {

        private String src;
        private String tgt;

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }

        public void setTgt(String tgt) {
            this.tgt = tgt;
        }

        public String getTgt() {
            return tgt;
        }

    }
}
