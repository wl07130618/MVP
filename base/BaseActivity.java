package com.jqielts.through.theworld.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.jqielts.through.theworld.R;
import com.jqielts.through.theworld.activity.LoginActivity;
import com.jqielts.through.theworld.activity.login.LoginMainActivity;
import com.jqielts.through.theworld.application.MainApplication;
import com.jqielts.through.theworld.config.AndroidWorkaround;
import com.jqielts.through.theworld.config.Config;
import com.jqielts.through.theworld.easekefu.DemoHelper;
import com.jqielts.through.theworld.model.ShareModel;
import com.jqielts.through.theworld.preferences.Preferences;
import com.jqielts.through.theworld.presenter.base.Presenter;
import com.jqielts.through.theworld.presenter.base.MvpView;
import com.jqielts.through.theworld.util.LogUtils;
import com.jqielts.through.theworld.util.NetworkUtils;
import com.jqielts.through.theworld.util.OSUtils;
import com.jqielts.through.theworld.view.dialog.DialogBuilder;
import com.jqielts.through.theworld.view.EmptyLayout;
import com.jqielts.through.theworld.widget.ChoiceItem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;

import static com.eduhdsdk.tools.FullScreenTools.getStatusBarHeight;

/**
 * Created by Administrator on 2017/3/29.
 */

public abstract class BaseActivity<P extends Presenter<V>, V extends MvpView> extends AppCompatActivity implements MvpView, LoaderManager.LoaderCallbacks<P>, View.OnClickListener {

    protected boolean isMiddle = false, isLeft = false, isRight = false;


    protected static final int CHOICE_THE_COUNTRY = 0x0000;
    protected static final int CHOICE_THE_MAJOR_CATEGORIES = 1;
    protected static final int CHOICE_THE_PROFESSIONAL_SMALL_CLASS = 2;
    protected static final int OPEN_NEW_OFFER = 0;
    protected static final int OPEN_NEW_OFFER_QUESTION = 1;
    protected static final int CLICK_NEW_ITEM = 0;
    protected static final int CLICK_NEW_ITEM_ACTION = 1;
    protected static final int ACTION_CHECK_PICTURE = 1;
    protected static final int ACTION_RELEASE = 2;

    protected final static int TYPE_PULLREFRESH = 1;
    protected final static int TYPE_UPLOADREFRESH = 2;

    protected static final int OFFER_ITEM_DETAIL = 3;

    protected static final int OFFER_ITEM_SHARE = 4;

    protected static final int OFFER_LIST_SEARCH = 0x01;

    protected static final int OFFER_LIST_TAG = 0x02;
    protected static final int CLICK_NEW_ITEM_ACTION_OTHER = 0x05;

    protected static final String DEVICE_ANDROID = "0";

    protected static final int SHARE_ADVERTICE = 1;


    protected static final String KICK_OUT = "KICK_OUT";

    protected  String huanxinId = "", baseId = "",headImg = "", nickName = "", sex = "", desc="", birthday = "", constellationStr="", userName="", isReading="", loaction="", university="", intentionCountry="";

    protected String phone = "", tag="", province="", city="", district="";

    protected int loginType = 0;

    protected boolean gradeCode = false, isBindingQQ = false, isBindingWeiXin = false, isBindingWeiBo = false, isBindingPhone = false, passwordIsNull = false;

    protected Preferences preferences;



    public final static int BASE_ACTIVITY_LOADER_ID = 100;
    protected ProgressDialog progressDialog;
    protected P presenter;

    protected Activity context;
    protected LinearLayout topBar_left_layout;
    public EmptyLayout mEmptyLayout;
    protected RelativeLayout topBar_right_layout;

    private TextView tab_title,topBar_right_text;

    private ImageView imgv_back, topBar_right_image;
    private LinearLayout common_parent;

    protected boolean isLogin = false;

    protected boolean isOnStart = false;

    protected String TAG;

    protected InputMethodManager inputManager;

    protected LinearLayout choice_left_layout, choice_middle_layout, choice_right_layout;
    protected ChoiceItem choice_left_text, choice_middle_text, choice_right_text;


    protected ImmersionBar mImmersionBar;

    protected int backStatus = 0;


    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && OSUtils.isMIUI()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


    private void setStatusBarView(int color){
        //获取windowphone下的decorView
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        int       count     = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof View) {
            decorView.getChildAt(count - 1).setBackgroundColor(color);
        } else {
            //新建一个和状态栏高宽的view
            View statusView = createStatusBarView(this, color);
            decorView.addView(statusView);
        }
        ViewGroup rootView1 = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content));

        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        //rootview不会为状态栏留出状态栏空间
        if (null != rootView){
        ViewCompat.setFitsSystemWindows(rootView,true);
        rootView.setClipToPadding(true);
        }
    }

    private View createStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        progressDialog = new ProgressDialog(this);
        getSupportLoaderManager().initLoader(BASE_ACTIVITY_LOADER_ID, null, this);
        preferences = Preferences.getInstance(getApplicationContext());
        baseId = preferences.getStringData(Config.GUID);
        huanxinId = preferences.getStringData(Config.HUAN_XIN);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
        }


        fullScreen(this);


        if(OSUtils.isMIUI()){
            MIUISetStatusBarLightMode(this,true);
        }else if(OSUtils.isFlyme()){
            setFlymeLightStatusBar(this, true);
        }else{
            setAndroidNativeLightStatusBar(this, true);
        }

        mImmersionBar = ImmersionBar.with(this); //初始化，默认透明状态栏和黑色导航栏
        if (null != mImmersionBar){
            mImmersionBar.transparentBar().statusBarDarkFont(true).init();
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
//                mImmersionBar.statusBarDarkFont(true).init();
//            }else{
//                mImmersionBar.transparentBar().statusBarDarkFont(true).init();
//                mImmersionBar.statusBarDarkFont(true).hideBar(BarHide.FLAG_SHOW_BAR).init();
//            }
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.parseColor(("#1bb5d7")));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (Build.VERSION.SDK_INT < 28) {
            if (null != UIProvider.getInstance()){
                UIProvider.getInstance().pushActivity(this);
            }
//        }
//        setStatusBarView(R.color.bg_color);

//        if (Chat)

        JPushInterface.onResume(this);


        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (Build.VERSION.SDK_INT < 28) {
            if (null != UIProvider.getInstance()){
                UIProvider.getInstance().pushActivity(this);
            }
//        }

        JPushInterface.onPause(this);

        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);

//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        MainApplication.getInstance().addActivity(this);

        province = preferences.getStringData(Config.LOCATION_PROVINCE, "");
        city = preferences.getStringData(Config.LOCATION_CITY);
        district = preferences.getStringData(Config.LOCATION_DISTRICT);

        huanxinId = preferences.getStringData(Config.HUAN_XIN);
        baseId = preferences.getStringData(Config.GUID);
        headImg = preferences.getStringData(Config.HEADIMAGE);
        nickName = preferences.getStringData(Config.NICKNAME);
        userName = preferences.getStringData(Config.USERNAME);
        constellationStr = preferences.getStringData(Config.CONSTELLATION);
        birthday = preferences.getStringData(Config.BIRTHDAY);
        desc = preferences.getStringData(Config.INTRODUCE);
        sex = preferences.getStringData(Config.SEX);
        gradeCode = preferences.getBooleanData(Config.GRADE_CODE, false);
        isReading = preferences.getStringData(Config.IS_READING);
        loaction = preferences.getStringData(Config.LOCATION);
        university = preferences.getStringData(Config.UNIVERSITY);
        intentionCountry = preferences.getStringData(Config.INTENTION_COUNTRY);
        isLogin = !TextUtils.isEmpty(baseId);
        loginType = preferences.getIntData(Config.LOGIN_TYPE);
        isBindingPhone = preferences.getBooleanData(Config.IS_BINDING_PHONE, false);
        isBindingWeiBo = preferences.getBooleanData(Config.IS_BINDING_WEIBO, false);
        isBindingWeiXin = preferences.getBooleanData(Config.IS_BINDING_WEIXIN, false);
        isBindingQQ = preferences.getBooleanData(Config.IS_BINDING_QQ, false);
        passwordIsNull = preferences.getBooleanData(Config.PASSWORD_IS_NULL, false);
        phone = preferences.getStringData(Config.PHONE_NUMBER);
        tag = preferences.getStringData(Config.TAG);
        TAG = this.getClass().getSimpleName();
        if (presenter != null && !isOnStart) {
            presenter.attachView((V) this);
        }
        preferences.setBooleanData(Config.ISVEDIO, false);
        preferences.setBooleanData(Config.IS_EASE_KEFU, false);
        preferences.setBooleanData(Config.IS_ERP, false);
        preferences.setBooleanData(Config.IS_DIAMOND, false);

        if (!isOnStart){
            obtainView();
            obtainData();
            obtainListener();
            isOnStart = true;
        }

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        baseId = preferences.getStringData(Config.GUID);
//        headImg = preferences.getStringData(Config.HEADIMAGE);
//        nickName = preferences.getStringData(Config.NICKNAME);
//        userName = preferences.getStringData(Config.USERNAME);
//        constellationStr = preferences.getStringData(Config.CONSTELLATION);
//        birthday = preferences.getStringData(Config.BIRTHDAY);
//        desc = preferences.getStringData(Config.INTRODUCE);
//        sex = preferences.getStringData(Config.SEX);
//        gradeCode = preferences.getBooleanData(Config.GRADE_CODE, false);
//        isReading = preferences.getStringData(Config.IS_READING);
//        loaction = preferences.getStringData(Config.LOCATION);
//        university = preferences.getStringData(Config.UNIVERSITY);
//        isLogin = !TextUtils.isEmpty(baseId);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        MainApplication.getInstance().removeActivity(this);
//        overridePendingTransition(R.anim.ease_slide_in_from_left, R.anim.ease_slide_out_to_right);
        if (null != mImmersionBar){
            mImmersionBar.destroy();//不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }
        super.onDestroy();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()){
            destroy();
        }
    }

    private boolean isDestroyed = false;
    private void destroy()  {
        if (isDestroyed) {
            return;
        }
        // 回收资源
        obtainDestroy();
        isDestroyed = true;
    }

    protected abstract void obtainDestroy();



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hd_slide_in_from_left, R.anim.hd_slide_out_to_right);
    }

    /** * 显示loading对话框 * * @param msg */
    @Override
    public void showLoading(String msg) {
        progressDialog.setMessage(msg);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /** * 隐藏loading对话框 */
    @Override
    public void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /** * 显示错误信息 * * @param errorMsg */
    @Override
    public void showError(String errorMsg) {
        if (!Config.isDebug){
            if(!TextUtils.isEmpty(errorMsg) && !errorMsg.contains("500")
                    && !errorMsg.contains("404") && !errorMsg.contains("null")
                    && !errorMsg.contains("Failed") && !errorMsg.contains("failed")
                    && !errorMsg.contains("cannot") && !errorMsg.contains("time")
                    && !errorMsg.contains("Invalid") && !errorMsg.contains("Size")
                    && !errorMsg.contains("-1") && !errorMsg.contains("非法") && !errorMsg.contains("No address"))
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        // TODO: 子类要实现此方法创建Loader
        return null;
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        presenter = null;
    }

    public void setLeft(boolean b) {
        if (null == topBar_left_layout)
            topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
        if (backStatus == 0)
            topBar_left_layout.setOnClickListener(this);

        if (b) {
            topBar_left_layout.setVisibility(View.VISIBLE);
        } else {
            topBar_left_layout.setVisibility(View.GONE);
        }
    }

    public void settLeftView(int drawable) {
        if (null == topBar_left_layout){
            topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
        }
        if (imgv_back == null)
            imgv_back = (ImageView) findViewById(R.id.imgv_back);
        topBar_left_layout.setVisibility(View.VISIBLE);
        imgv_back.setImageResource(drawable);
        if (backStatus == 0)
            topBar_left_layout.setOnClickListener(this);
    }

    public void setTitle(String title, int res) {
        if (tab_title == null)
            tab_title = (TextView) findViewById(R.id.topBar_title);
        tab_title.setText(Html.fromHtml(title));

        if (topBar_left_layout == null){
            topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
            if (backStatus == 0)
                topBar_left_layout.setOnClickListener(this);
        }

        if(res != 0){
            if (imgv_back == null)
                imgv_back = (ImageView) findViewById(R.id.imgv_back);
            imgv_back.setImageResource(res);
        }
    }

    public void setTitleColor(String title, int res) {
        if (tab_title == null)
            tab_title = (TextView) findViewById(R.id.topBar_title);
        tab_title.setText(Html.fromHtml(title));
        tab_title.setTextColor(getResources().getColor(res));
        if (topBar_left_layout == null){
            topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
            if (backStatus == 0)
                topBar_left_layout.setOnClickListener(this);
        }
    }

    public void setTitle(String title) {
        if (tab_title == null)
            tab_title = (TextView) findViewById(R.id.topBar_title);

        tab_title.setText(Html.fromHtml(title));
        if (topBar_left_layout == null){
            topBar_left_layout = (LinearLayout) findViewById(R.id.topBar_left_layout);
            if (backStatus == 0)
                topBar_left_layout.setOnClickListener(this);
        }
    }


    public void setTitleBackground(int color){
        if (common_parent == null)
            common_parent = (LinearLayout) findViewById(R.id.common_parent);
        common_parent.setBackgroundResource(color);
    }

    public void setTitleHide(){
        if (common_parent == null){
            common_parent = (LinearLayout) findViewById(R.id.common_parent);
        }
        common_parent.setVisibility(View.GONE);
    }

    public void setTitleShow(){
        if (common_parent == null){
            common_parent = (LinearLayout) findViewById(R.id.common_parent);
        }
        common_parent.setVisibility(View.VISIBLE);
    }

    public void setTitleTextHint(){
        if (tab_title == null)
            tab_title = (TextView) findViewById(R.id.topBar_title);
        tab_title.setVisibility(View.GONE);
    }

    public void setTitleTextShow(){
        if (tab_title == null)
            tab_title = (TextView) findViewById(R.id.topBar_title);
        tab_title.setVisibility(View.VISIBLE);
    }

    public void setRightView(int drawable) {
        if (null == topBar_right_layout){
            topBar_right_layout = (RelativeLayout) findViewById(R.id.topBar_right_parent);
        }
        if (topBar_right_text == null)
            topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        if (topBar_right_image == null)
            topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);

        topBar_right_layout.setVisibility(View.VISIBLE);
        topBar_right_text.setVisibility(View.GONE);
        topBar_right_image.setVisibility(View.VISIBLE);
        topBar_right_image.setImageResource(drawable);
    }

    public void setRightText(String content) {
        if (topBar_right_layout == null)
            topBar_right_layout = (RelativeLayout) findViewById(R.id.topBar_right_parent);

        topBar_right_layout.setVisibility(View.VISIBLE);
        if (topBar_right_text == null)
            topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        if (topBar_right_image == null)
            topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);

        topBar_right_image.setVisibility(View.GONE);
        topBar_right_text.setVisibility(View.VISIBLE);
        topBar_right_text.setText(content);
    }

    public void setRightText(String content, int drawable) {
        if (topBar_right_layout == null)
            topBar_right_layout = (RelativeLayout) findViewById(R.id.topBar_right_parent);

        topBar_right_layout.setVisibility(View.VISIBLE);
        if (topBar_right_text == null)
            topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        if (topBar_right_image == null)
            topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);

        topBar_right_image.setImageResource(drawable);
        topBar_right_text.setText(content);
        topBar_right_text.setTextColor(getResources().getColor(R.color.text_content_professionals));
        topBar_right_text.setBackground(null);
    }

    public void setRightText(String content, int drawable, int resColor) {
        if (topBar_right_layout == null)
            topBar_right_layout = (RelativeLayout) findViewById(R.id.topBar_right_parent);

        topBar_right_layout.setVisibility(View.VISIBLE);
        if (topBar_right_text == null)
            topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        if (topBar_right_image == null)
            topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);

        topBar_right_image.setImageResource(drawable);
        topBar_right_text.setText(content);
        topBar_right_text.setTextColor(getResources().getColor(resColor));
        topBar_right_text.setBackground(null);
    }

    public void setRightTextColor(String content, int resColor) {
        if (topBar_right_layout == null)
            topBar_right_layout = (RelativeLayout) findViewById(R.id.topBar_right_parent);

        topBar_right_layout.setVisibility(View.VISIBLE);
        if (topBar_right_text == null)
            topBar_right_text = (TextView) findViewById(R.id.topBar_right_text);
        if (topBar_right_image == null)
            topBar_right_image = (ImageView) findViewById(R.id.topBar_right_image);

        topBar_right_image.setVisibility(View.GONE);
        topBar_right_text.setText(content);
        topBar_right_text.setTextColor(getResources().getColor(resColor));
        topBar_right_text.setBackground(null);
    }

    public void setChoiceLeft(String content){
        choice_left_layout = (LinearLayout) findViewById(R.id.choice_left_layout);
        if (choice_left_layout != null) {
            choice_left_layout.setVisibility(View.VISIBLE);
            choice_left_text = (ChoiceItem) findViewById(R.id.choice_left_add);
            if (choice_left_text != null) {
                choice_left_text.setText(content);
            }
        }
    }

    public void setChoiceMiddle(String content){
        choice_middle_layout = (LinearLayout) findViewById(R.id.choice_middle_layout);
        if (choice_middle_layout != null) {
            choice_middle_layout.setVisibility(View.VISIBLE);
            choice_middle_text = (ChoiceItem) findViewById(R.id.choice_middle_a);
            if (choice_middle_text != null) {
                choice_middle_text.setText(content);
            }
        }
    }

    public void setChoiceRightText(String content, boolean isVisible){
        choice_right_layout = (LinearLayout) findViewById(R.id.choice_right_layout);
        if (choice_right_layout != null) {
            choice_right_layout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            choice_right_text = (ChoiceItem) findViewById(R.id.choice_right_r);
            if (choice_right_text != null) {
                choice_right_text.setText(content);
            }
        }
    }

    public void setChoiceRightText(String content){

        setChoiceRightText(content, true);
    }

    @Override
    public void onClick(View v) {
        if (backStatus == 0) {
            switch (v.getId()) {
                case R.id.topBar_left_layout:
                    hideKeyboard();
                    finish();
                    overridePendingTransition(R.anim.hd_slide_in_from_left, R.anim.hd_slide_out_to_right);
                    break;
            }
        }
    }
    // Triggered when "Empty" button is clicked
    public void onShowEmpty(String content, int drawable) {
        if (mEmptyLayout != null) {
            if (!TextUtils.isEmpty(content)) {
                mEmptyLayout.setEmptyMessage(content);
            }
            if (drawable != -1) {
                mEmptyLayout.setEmptyDrawable(drawable);
            }
            mEmptyLayout.showEmpty();
        }
    }

    // Triggered when "Loading" button is clicked
//    public void onShowLoading() {
//        if (mEmptyLayout != null)
//            mEmptyLayout.showLoading();
//    }

    /*
* 显示失败view
* params content  显示的内容
* params state  失败状态
* */
    public void onShowError(String content, int drawable) {
        if (mEmptyLayout != null) {
            if (!TextUtils.isEmpty(content)) {
                mEmptyLayout.setErrorMessage(content);
            }
            if (drawable != -1) {
                mEmptyLayout.setEmptyDrawable(drawable);
            }
        }
        mEmptyLayout.showError();
    }

    /**
     * 通过xml查找相应的ID，通用方法
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected boolean isLoginOrNo(){
        if (!isLogin){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LoginMainActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

//    @Override
//    public void onContentChanged() {
//        super.onContentChanged();
//        Window.Callback cb = getCallB
//    }

    long mLasttime = 0;

    protected void checkNetworkOrNot(Object intent){
        if (NetworkUtils.getAPNType(getApplicationContext()) != NetworkUtils.NetWorkMethod.NO){
            if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                return;
            mLasttime = System.currentTimeMillis();
            if (intent instanceof Intent){
                startActivity((Intent) intent);

                overridePendingTransition(R.anim.hd_slide_in_from_right, R.anim.hd_slide_out_to_left);
            }else if(intent instanceof Runnable)
                runOnUiThread((Runnable) intent);
        }
        else
            LogUtils.showToastShort(getApplicationContext(),"无网络链接，请检查网络状态");
    }

    protected void checkNetworkOrNot(Object intent, Object intent2){
        if (NetworkUtils.getAPNType(MainApplication.getContext()) != NetworkUtils.NetWorkMethod.NO){
            if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                return;
            mLasttime = System.currentTimeMillis();
            if (intent instanceof Intent){
                startActivity((Intent) intent);
                overridePendingTransition(R.anim.hd_slide_in_from_right, R.anim.hd_slide_out_to_left);
            }else if(intent instanceof Runnable){
                    runOnUiThread((Runnable) intent);
            }
        }
        else{
            if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                return;
            mLasttime = System.currentTimeMillis();
            if (intent2 instanceof Intent){
                startActivity((Intent) intent2);
                overridePendingTransition(R.anim.hd_slide_in_from_right, R.anim.hd_slide_out_to_left);
            }else if(intent2 instanceof Runnable){
                    runOnUiThread((Runnable) intent2);
            }else{
                LogUtils.showToastShort(getApplicationContext(),"无网络链接，请检查网络状态");
            }
        }
    }

    protected void checkLasttime(Object intent){
            if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                return;
            mLasttime = System.currentTimeMillis();
            if (intent instanceof Intent){
                startActivity((Intent) intent);
                overridePendingTransition(R.anim.hd_slide_in_from_right, R.anim.hd_slide_out_to_left);
            }else if(intent instanceof Runnable)
                runOnUiThread((Runnable) intent);
    }

    protected void checkNetworkOrWifi(final Object intent){
        if (NetworkUtils.getAPNType(getApplicationContext()) == NetworkUtils.NetWorkMethod.WIFI){
            if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                return;
            mLasttime = System.currentTimeMillis();
            if (intent instanceof Intent){
                startActivity((Intent) intent);
                overridePendingTransition(R.anim.hd_slide_in_from_right, R.anim.hd_slide_out_to_left);
            }else if(intent instanceof Runnable)
                runOnUiThread((Runnable) intent);
        }
        else
        DialogBuilder.getInstance(this)
                .withTitle("提示")
                .withContent("未链接wifi的情况下观看视频会耗费较多流量，请问是否继续观看？")
                .withCancelText("取消")
                .withConfirmText("继续")
                .setConfirmClickListener(new DialogBuilder.OnClickListener() {
                    @Override
                    public void onClick(DialogBuilder dialogBuilder) {
                        checkNetworkOrNot(intent);
                    }
                }).show();
    }

    protected abstract void obtainView();

    protected abstract void obtainData();

    protected abstract void obtainListener();

    public boolean isLogin(){
        return isLoginOrNo();
    }

    public void checkNetwork(Object intent){
        checkNetworkOrNot(intent);
    }

    @Override
    public void updateCollection(boolean isCollection) {

    }

    @Override
    public void updateFavour(boolean isFavour) {

    }

    @Override
    public void shareMethod(ShareModel model) {

    }

    @Override
    public void shareMethod(com.jqielts.through.theworld.diamond.model.ShareModel model) {

    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove( listener );
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 隐藏软键盘
     */
    protected void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
