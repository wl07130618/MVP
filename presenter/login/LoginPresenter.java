package com.jqielts.tool.presenter.login;

import android.text.TextUtils;

import com.jqielts.tool.config.ResultCode;
import com.jqielts.tool.model.CommonState;
import com.jqielts.tool.model.UserModel;
import com.jqielts.tool.network.ServiceResponse;
import com.jqielts.tool.presenter.base.BasePresenter;

/**
 * Created by Administrator on 2017/3/29.
 */

public class LoginPresenter extends BasePresenter<ILoginView> implements ILoginPresenter {


    /** * 登录 */
    @Override
    public void login(String name, String pwd) {
//        checkViewAttached();
        if (TextUtils.isEmpty(name)) {
            if (isViewAttached())
                getMvpView().showError("登录名不能为空");
        }else if (TextUtils.isEmpty(pwd)) {
            if (isViewAttached())
                getMvpView().showError( "密码不能为空");
        } else if (pwd.length() < 6){
            if (isViewAttached())
                getMvpView().showError("密码长度不得少于6位");
        } else {
            if (isViewAttached())
                getMvpView().showLoading("登录中...");
            ServiceResponse response = new ServiceResponse<UserModel>() {
                @Override
                public void onNext(UserModel model) {
                    super.onNext(model);
                    getMvpView().showError(model.getStatus());
                    if (model.getReqCode() == ResultCode.RESULT_OK){

                        if (isViewAttached())
                            getMvpView().login(model.getData());
                    }else{
                        if (isViewAttached())
                            getMvpView().showError(model.getStatus());
                    }
                    if (isViewAttached())
                        getMvpView().hideLoading();
                }
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (isViewAttached()){
                        getMvpView().hideLoading();
                        getMvpView().showError(e.getMessage());
                    }
                }
            };
            userInterface.login(name, pwd, response);
        }
    }

}
