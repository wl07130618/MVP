package com.jqielts.tool.presenter.base;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface Presenter<V extends MvpView> {

    /**
     * Presenter与View建立连接
     * @param mvpView 与此Presenter相对应的View
     */
    void attachView(V mvpView);

    /**
     * Presenter与View连接断开
     */
    void detachView();

}

