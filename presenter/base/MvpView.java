package com.jqielts.tool.presenter.base;


import com.jqielts.tool.model.ShareModel;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface MvpView {
    /**
     * 显示loading对话框
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 隐藏loading对话框
     */
    void hideLoading();

    /**
     * 显示错误信息
     * @param errorMsg
     */
    void showError(String errorMsg);

    void updateCollection(boolean isCollection);

    void updateFavour(boolean isFavour);

    void shareMethod(ShareModel model);


}
