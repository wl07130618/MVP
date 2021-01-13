package com.jqielts.through.theworld.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.jqielts.through.theworld.R;
import com.jqielts.through.theworld.presenter.base.loader.PresenterFactory;
import com.jqielts.through.theworld.presenter.base.loader.PresenterLoader;
import com.jqielts.through.theworld.presenter.chat.ChatPresenter;
import com.jqielts.through.theworld.presenter.chat.IChatView;
import com.jqielts.through.theworld.widget.UserDateButton;

/**
 * Created by Administrator on 2017/3/29.
 */

public class DemoActivity extends BaseActivity<ChatPresenter, IChatView> implements IChatView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onParseIntent();
//        startLoc();
    }

    @Override
    public Loader<ChatPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader(this, new PresenterFactory<ChatPresenter>() {
            @Override
            public ChatPresenter create() {
                return new ChatPresenter();
            }
        });
    }

    protected void obtainView(){
    }

    protected void obtainData(){

    }

    protected void obtainListener(){

    }

    public void commit() {

    }

    private void onParseIntent() { }

    private boolean isThirdLogin = false;

    private int loginType = 4;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void obtainDestroy() { }
}
