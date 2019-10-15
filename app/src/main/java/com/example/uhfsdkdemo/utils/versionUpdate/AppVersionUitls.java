package com.example.uhfsdkdemo.utils.versionUpdate;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.WaitDialog;
import com.example.uhfsdkdemo.api.AppClient;
import com.example.uhfsdkdemo.utils.versionUpdate.resultBack.AppVersionBack;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：lish on 2018-07-24.
 * 描述：
 */

public class AppVersionUitls {
    public static Integer getVersionNo(Context context) {
        Integer version = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 检测app版本是否需要更新
     *
     * @author lish
     * created at 2018-07-24 11:57
     */
    public static void checkVersion(final Context context, String appVersionTxt
            , final String appName, final WaitDialog loadingDialog, final Class<?> cls
            , final boolean isShown) {
        AppClient.getAppVersionApi().getAppVersion(appVersionTxt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppVersionBack>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getAppVersion:", e.toString());
                        if (isShown)
                            Toast.makeText(context, "获取版本信息失败", Toast.LENGTH_LONG).show();
                        if (loadingDialog != null) loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(AppVersionBack appVersion) {
                        if (loadingDialog != null) loadingDialog.dismiss();
                        if (AppVersionUitls.getVersionNo(context) < appVersion.getData().getVersionCode()) {
                            if (isShown )
                                shownUpdataDialog(context, appVersion.getData().getDes(), appName, cls);
                        } else {
                            if (isShown)
                                Toast.makeText(context, "当前为最新版本", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 更新dialog
     *
     * @author lish
     * created at 2018-07-24 12:35
     */
    public static void shownUpdataDialog(final Context context, String des, final String appName, final Class<?> cls) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("版本更新");
        normalDialog.setMessage(des);
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("下载安装",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          /*如果服务正在运行，直接return*/
                        if (isServiceRunning(context, "com.ascend.wangfeng.locationbyhand.util.versionUpdate.VersionUpdateService")) {
                            Log.e("服务正在运行", "return");
                            return;
                        }
                        VersionUpdateService.beginUpdate(context, appName, R.mipmap.download, cls);
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 判断服务是否运行
     */
    private static boolean isServiceRunning(Context context, final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }


    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * 规则是按日期订的例如：2.10.15   项目启动第2年的8月15号
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2,String type) throws Exception {

        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        if (!version1.startsWith(type) || !version2.startsWith(type))
            return -1;
        version1 = version1.replace(type, "");
        version2 = version2.replace(type, "");
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        for (int i = 0; i < versionArray1.length; i++) { //如果位数只有一位则自动补零（防止出现一个是04，一个是5 直接以长度比较）
            if (versionArray1[i].length() == 1) {
                versionArray1[i] = "0" + versionArray1[i];
            }
        }
        String[] versionArray2 = version2.split("\\.");
        for (int i = 0; i < versionArray2.length; i++) {//如果位数只有一位则自动补零
            if (versionArray2[i].length() == 1) {
                versionArray2[i] = "0" + versionArray2[i];
            }
        }
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
//如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
}
