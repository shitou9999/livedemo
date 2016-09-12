package com.kuainiutv.event;


import com.kuainiutv.modle.cons.Action;

/**
 * 标识一个具体的请求来源，回调方根据该请求来源作对应的响应。不传递内容性数据。
 *
 * @author nanck on 2016/6/22.
 * @see Action
 */
public class EmptyEvent {
    private Action action;

    public EmptyEvent(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
