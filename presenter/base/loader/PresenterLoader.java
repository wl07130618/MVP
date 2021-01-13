package com.jqielts.tool.presenter.base.loader;

import android.content.Context;
import android.support.v4.content.Loader;

import com.jqielts.tool.presenter.base.Presenter;


/**
 * Created by Administrator on 2017/3/29.
 */

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory factory) {
        super(context);
        this.factory = factory;
    }



    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override protected void onReset() {
        presenter = null;
    }
}
