package com.jqielts.tool.presenter.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jqielts.tool.interactor.UserInteractor;
import com.jqielts.tool.interactor.UserInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class BasePresenter<V extends MvpView> implements Presenter<V> {
    /**
     * 当前连接的View
     */
    private V mvpView;

    private Gson gson;
    private GsonBuilder builder;

    protected UserInterface userInterface;


    public BasePresenter(){
        builder=new GsonBuilder();
        gson=builder.create();
        userInterface = UserInteractor.getInstance();
    }

    /**
     * Presenter与View建立连接
     * @param mvpView 与此Presenter相对应的View
     */
    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    /**
     * Presenter与View连接断开
     */
    @Override
    public void detachView() {
        this.mvpView = null;
    }

    /**
     * 是否与View建立连接
     * @return
     */
    public boolean isViewAttached() {
        return mvpView != null;
    }

    /**
     * 获取当前连接的View
     * @return
     */
    public V getMvpView() {
        return mvpView;
    }

    /**
     * 每次调用业务请求的时候都要先调用方法检查是否与View建立连接，没有则抛出异常
     */
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("请求数据前请先调用 attachView(MvpView) 方法与View建立连接");
        }
    }

    public  <T> JSONObject getJSONObjectByData(T t, Class<T> clazz) throws JSONException {
        String tmpObj = null;
        tmpObj=gson.toJson(t, clazz);
        JSONObject jsonObject = new JSONObject(tmpObj);
        return jsonObject;
    }

    public  <T> T getDataByJSONObject(JSONObject obj, Class<T> clazz) throws JSONException {

        T t = null;
        if (obj != null)
            t = gson.fromJson(obj.toString(), clazz);
        return t;
    }

    public JSONArray getJSONArrayByList(List<?> list, Class<?> T) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        String tmpObj = null;
        int count = list.size();
        for(int i = 0; i < count; i++)
        {
            tmpObj=gson.toJson(list.get(i), T);
            JSONObject jsonObject = new JSONObject(tmpObj);
            jsonArray.put(jsonObject);
            jsonObject = null;
            tmpObj = null;

        }
        return jsonArray;
    }

    public  JSONArray getJSONArrayByList(List<?> list, Class<?> T, int type, String... groupType) throws JSONException {
        String[] groups = groupType;
        JSONArray jsonArray = new JSONArray();
        String tmpObj = null;
        int count = list.size();
        for(int i = 0; i < count; i++)
        {
            tmpObj=gson.toJson(list.get(i), T);
            JSONObject jsonObject = null;
            if (type == 0){
                jsonObject = new JSONObject();
                jsonObject.put("picUrl", tmpObj);
            } else
                jsonObject = new JSONObject(tmpObj);
            for (int j=0 ; j<groups.length ; j=j+2){
                jsonObject.put(groups[j], groups[j+1]);
            }
            jsonArray.put(jsonObject);
            jsonObject = null;
            tmpObj = null;
        }
        return jsonArray;
    }

    public List getListByJSONArray(JSONArray jsonArray, Type type){
        List list = gson.fromJson(String.valueOf(jsonArray), type);
        return list;
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public <T> List<T> jsonToArrayList(JSONArray json, Class<T> clazz)
    {
        ArrayList arrayList;
        Type type = new TypeToken<ArrayList<JSONObject>>()
        {}.getType();
        ArrayList<JSONObject> jsonObjects = gson.fromJson(String.valueOf(json), type);
        if (clazz.getName() instanceof String)
            arrayList = new ArrayList<String>();
        else
            arrayList = new ArrayList<T>();
        for (JSONObject jsonObject : jsonObjects)
        {
            if (clazz.getName() instanceof String)
                arrayList.add(jsonObject.optString("picUrl"));
            else
                arrayList.add(gson.fromJson(jsonObject.toString(), clazz));
        }
        return arrayList;
    }
}
