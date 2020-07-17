package com.github.gingjing.plugin.translate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 百度响应结果
 *
 * @author: GingJingDM
 * @date: 2020年 07月04日 19时32分
 * @version: 1.0
 */
public class BaiDuResponse {

    private String from;
    private String to;
    @JsonProperty("trans_result")
    private List<TransResult> transResult;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTransResult(List<TransResult> transResult) {
        this.transResult = transResult;
    }

    public List<TransResult> getTransResult() {
        return transResult;
    }

    public static String getIdentity() {
        return "BaiDu%s";
    }

    public static class TransResult {

        private String src;
        private String dst;

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSrc() {
            return src;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }

        public String getDst() {
            return dst;
        }

    }
}
