package com.jqielts.tool.presenter.base.loader;


import com.jqielts.tool.presenter.base.Presenter;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface PresenterFactory<T extends Presenter> {

    T create();

}
