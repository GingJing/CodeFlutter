package com.github.gingjing.plugin.translate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 金山响应结果
 *
 * @author: <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @modify  gingjingdm
 * @date: 2020年 07月04日 19时32分
 * @version: 1.0
 */
public class JinShanResponse {

    @JsonProperty("word_name")
    private String wordName;
    @JsonProperty("is_CRI")
    private String isCRI;
    private Exchange exchange;
    private List<Symbols> symbols;

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getIsCRI() {
        return isCRI;
    }

    public void setIsCRI(String isCRI) {
        this.isCRI = isCRI;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public List<Symbols> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbols> symbols) {
        this.symbols = symbols;
    }

    public static String getIdentity() {
        return "JinShan%s";
    }

    public static class Exchange {

        @JsonProperty("word_pl")
        private List<String> wordPl;
        @JsonProperty("word_third")
        private String wordThird;
        @JsonProperty("word_past")
        private String wordPast;
        @JsonProperty("word_done")
        private String wordDone;
        @JsonProperty("word_ing")
        private String wordIng;
        @JsonProperty("word_er")
        private String wordEr;
        @JsonProperty("word_est")
        private String wordEst;

        public List<String> getWordPl() {
            return wordPl;
        }

        public void setWordPl(List<String> wordPl) {
            this.wordPl = wordPl;
        }

        public String getWordThird() {
            return wordThird;
        }

        public void setWordThird(String wordThird) {
            this.wordThird = wordThird;
        }

        public String getWordPast() {
            return wordPast;
        }

        public void setWordPast(String wordPast) {
            this.wordPast = wordPast;
        }

        public String getWordDone() {
            return wordDone;
        }

        public void setWordDone(String wordDone) {
            this.wordDone = wordDone;
        }

        public String getWordIng() {
            return wordIng;
        }

        public void setWordIng(String wordIng) {
            this.wordIng = wordIng;
        }

        public String getWordEr() {
            return wordEr;
        }

        public void setWordEr(String wordEr) {
            this.wordEr = wordEr;
        }

        public String getWordEst() {
            return wordEst;
        }

        public void setWordEst(String wordEst) {
            this.wordEst = wordEst;
        }
    }

    public static class Parts {

        private String part;
        private List<String> means;

        public void setPart(String part) {
            this.part = part;
        }

        public String getPart() {
            return part;
        }

        public void setMeans(List<String> means) {
            this.means = means;
        }

        public List<String> getMeans() {
            return means;
        }

    }

    public static class Symbols {

        @JsonProperty("ph_en")
        private String phEn;
        @JsonProperty("ph_am")
        private String phAm;
        @JsonProperty("ph_other")
        private String phOther;
        @JsonProperty("ph_en_mp3")
        private String phEnMp3;
        @JsonProperty("ph_am_mp3")
        private String phAmMp3;
        @JsonProperty("ph_tts_mp3")
        private String phTtsMp3;
        private List<Parts> parts;

        public String getPhEn() {
            return phEn;
        }

        public void setPhEn(String phEn) {
            this.phEn = phEn;
        }

        public String getPhAm() {
            return phAm;
        }

        public void setPhAm(String phAm) {
            this.phAm = phAm;
        }

        public String getPhOther() {
            return phOther;
        }

        public void setPhOther(String phOther) {
            this.phOther = phOther;
        }

        public String getPhEnMp3() {
            return phEnMp3;
        }

        public void setPhEnMp3(String phEnMp3) {
            this.phEnMp3 = phEnMp3;
        }

        public String getPhAmMp3() {
            return phAmMp3;
        }

        public void setPhAmMp3(String phAmMp3) {
            this.phAmMp3 = phAmMp3;
        }

        public String getPhTtsMp3() {
            return phTtsMp3;
        }

        public void setPhTtsMp3(String phTtsMp3) {
            this.phTtsMp3 = phTtsMp3;
        }

        public List<Parts> getParts() {
            return parts;
        }

        public void setParts(List<Parts> parts) {
            this.parts = parts;
        }
    }

}
