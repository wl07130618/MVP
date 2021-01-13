package com.jqielts.tool.presenter.login;


import com.jqielts.tool.model.UserModel;
import com.jqielts.tool.presenter.base.MvpView;

/**
 * Created by Administrator on 2017/3/29.
 */

public interface ILoginView extends MvpView {


    void login(UserModel.UserBean data);

}
