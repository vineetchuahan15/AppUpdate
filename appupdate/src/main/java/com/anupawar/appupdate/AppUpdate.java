package com.anupawar.appupdate;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.anupawar.appupdate.Common.Constant;
import com.anupawar.appupdate.Dialogs.UpdateActivity;
import com.anupawar.appupdate.Models.VersionModel;
import com.anupawar.appupdate.Net.CheckUpdateTask;
import com.anupawar.appupdate.Utils.FunctionUtils;
import com.anupawar.appupdate.Utils.NetWorkUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Map;

public class AppUpdate {
    private static final String TAG = "AppUpdate";

    private Context mContext;
    private String mUrl;
    private String mToastMsg;
    private CheckUpdateTask.Callback mCallback;
    private int mNotificationIcon;
    private long mTime;
    private boolean mIsShowToast = true;
    private boolean mIsShowNetworkErrorToast = true;
    private boolean mIsShowBackgroundDownload = true;
    private boolean mIsPost = false;
    private Map<String, String> mPostParams;
    private Class<? extends FragmentActivity> mCls;

    public AppUpdate() {
    }

    public void start() {
        if (!NetWorkUtils.getNetworkStatus(mContext)) {
            if (mIsShowNetworkErrorToast) {
                TastyToast.makeText(mContext, mContext.getResources().getString(R.string.update_lib_network_not_available),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }
            return;
        }
        if (TextUtils.isEmpty(mUrl)) {
            throw new RuntimeException("url not be null");
        }

        if (checkUpdateTime(mTime)) {
            return;
        }
        new CheckUpdateTask(mContext, mUrl, mIsPost, mPostParams, mInnerCallBack).start();
    }

    private CheckUpdateTask.Callback mInnerCallBack = new CheckUpdateTask.Callback() {
        @Override
        public void callBack(VersionModel model, boolean hasNewVersion) {
            if (model == null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mIsShowToast) {
                            TastyToast.makeText(mContext,
                                    TextUtils.isEmpty(mToastMsg)?  mContext.getResources().getString(R.string.update_lib_default_toast) : mToastMsg,
                                    TastyToast.LENGTH_SHORT,TastyToast.INFO);

                        }

                    }
                });
                return;
            }

           FunctionUtils.setLastCheckTime(mContext, System.currentTimeMillis());
            if (mCallback != null) {
                mCallback.callBack(model, hasNewVersion);
            }

            if (hasNewVersion || mIsShowToast) {
                start2Activity(mContext, model);
            }
        }

    };

    private void start2Activity(Context context, VersionModel model) {
        try {
            Intent intent = new Intent(context, mCls == null ? UpdateActivity.class : mCls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.MODEL, model);
            intent.putExtra(Constant.TOAST_MSG, mToastMsg);
            intent.putExtra(Constant.NOTIFICATION_ICON, mNotificationIcon);
            intent.putExtra(Constant.IS_SHOW_TOAST_MSG, mIsShowToast);
            intent.putExtra(Constant.IS_SHOW_BACKGROUND_DOWNLOAD, mIsShowBackgroundDownload);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    private boolean checkUpdateTime(long time) {
        long lastCheckUpdateTime = FunctionUtils.getLastCheckTime(mContext);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckUpdateTime > time) {
            return false;
        }
        return true;
    }

    public static class Builder {
        private AppUpdate wrapper = new AppUpdate();

        public Builder(Context context) {
            wrapper.mContext = context;
        }

        public Builder setUrl(String url) {
            wrapper.mUrl = url;
            return this;
        }

        public Builder setTime(long time) {
            wrapper.mTime = time;
            return this;
        }

        public Builder setNotificationIcon(int notificationIcon) {
            wrapper.mNotificationIcon = notificationIcon;
            return this;
        }

        public Builder setCustomsActivity(Class<? extends FragmentActivity> cls) {
            wrapper.mCls = cls;
            return this;
        }

        public Builder setCallback(CheckUpdateTask.Callback callback) {
            wrapper.mCallback = callback;
            return this;
        }

        public Builder setToastMsg(String toastMsg) {
            wrapper.mToastMsg = toastMsg;
            return this;
        }

        public Builder setIsShowToast(boolean isShowToast) {
            wrapper.mIsShowToast = isShowToast;
            return this;
        }

        public Builder setIsShowNetworkErrorToast(boolean isShowNetworkErrorToast) {
            wrapper.mIsShowNetworkErrorToast = isShowNetworkErrorToast;
            return this;
        }

        public Builder setIsShowBackgroundDownload(boolean isShowBackgroundDownload) {
            wrapper.mIsShowBackgroundDownload = isShowBackgroundDownload;
            return this;
        }

        public Builder setIsPost(boolean isPost) {
            wrapper.mIsPost = isPost;
            return this;
        }

        public Builder setPostParams(Map<String, String> postParams) {
            wrapper.mPostParams = postParams;
            return this;
        }

        public AppUpdate build() {
            return wrapper;
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());
}
