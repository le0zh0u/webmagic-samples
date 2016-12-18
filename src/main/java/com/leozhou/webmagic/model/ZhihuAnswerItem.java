package com.leozhou.webmagic.model;

/**
 * Created by zhouchunjie on 2016/12/18.
 */
public class ZhihuAnswerItem {

    private String QuestionTitle;
    private String vote;
    private String content;
    private String userid;

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        QuestionTitle = questionTitle;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
