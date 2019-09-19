package com.summer.study.netty.demoOne.message;

import com.summer.study.netty.base.annotation.MessageId;

@MessageId(id= 1)
public class ReqSayHallo extends AbstractMessage{

    private int id;

    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
