package com.example.uhfsdkdemo.utils.versionUpdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.example.uhfsdkdemo.R;
import com.example.uhfsdkdemo.Server;
import com.example.uhfsdkdemo.api.AppClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VersionUpdateService extends Service {
    public static final String APP_NAME = "APP_NAME";
    public static final String URL = "URL";
    public static final String ICON_ID = "ICON_ID";
    public static final String MAIN_ACTIVITY_REFERENCE = "MAIN_ACTIVITY_REFERENCE";
    public static final String APK_CACHE_PATH = "APK_CACHE_PATH";

    private static int iconId = -1;
    private static String appName;
    private static String downUrl;
    private static String apkCachePath;

    private static final int DOWN_OK = 1; // 下载完成
    private static final int DOWN_ERROR = 0;

    private NotificationManager notificationManager;
    private Notification notification;

    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private File updateFile;

    private int notification_id = 0;
    private static Class<?> mCls;
    /***
     * 更新UI
     */
    final Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_OK:
                    // 下载完成，点击安装
                    Intent installApkIntent = getFileIntent(getApplication(), updateFile);
                    pendingIntent = PendingIntent.getActivity(VersionUpdateService.this, 0, installApkIntent, 0);
                    NotificationCompat.Builder builder1 = new NotificationCompat.Builder(getBaseContext());
                    builder1.setSmallIcon(iconId);
                    builder1.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconId));
                    builder1.setAutoCancel(true);
                    //禁止滑动删除
                    builder1.setOngoing(true);
                    //取消右上角的时间显示
                    builder1.setShowWhen(true);
                    builder1.setContentTitle(appName + "下载成功，点击安装");
                    builder1.setOngoing(true);
                    builder1.setShowWhen(false);
                    Notification notification = builder1.build();
                    notification.contentIntent = pendingIntent;
                    notificationManager.notify(notification_id, notification);
                    stopService(updateIntent);
                    stopSelf();
                    break;
                case DOWN_ERROR:
                    NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getBaseContext());
                    builder2.setSmallIcon(iconId);
                    builder2.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconId));
                    builder2.setAutoCancel(true);
                    //禁止滑动删除
                    builder2.setOngoing(true);
                    //取消右上角的时间显示
                    builder2.setShowWhen(true);
                    builder2.setContentTitle(appName + "下载失败");
                    //builder.setContentInfo(progress+"%");
                    builder2.setOngoing(true);
                    builder2.setShowWhen(false);
                    Notification notification2 = builder2.build();
                    notification2.contentIntent = pendingIntent;
                    notificationManager.notify(notification_id, notification2);
                    stopSelf();
                    break;
                default:
                    stopService(updateIntent);
                    stopSelf();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 启动下载服务
     *
     * @param context 上下文
     * @param appName 应用名称
     * @param iconId  图标资源id
     * @param cls     点击通知时跳转目标Activity
     */
    public static void beginUpdate(Context context, String appName, int iconId, Class<?> cls) {
        if (context == null) {
            throw new RuntimeException(
                    "Can't beginUpdate when the context is null!");
        }
        if (appName == null) {
            throw new RuntimeException(
                    "Can't beginUpdate when the appName is null!");
        }
        if (cls == null) {
            throw new RuntimeException(
                    "Can't beginUpdate when the activityReference is null!");
        }
        Intent updateIntent = new Intent(context, VersionUpdateService.class);
        updateIntent.putExtra(VersionUpdateService.APP_NAME, appName);
        updateIntent.putExtra(VersionUpdateService.ICON_ID, iconId);
        mCls = cls;
//        updateIntent.putExtra(VersionUpdateService.MAIN_ACTIVITY_REFERENCE, activityReference);
        context.startService(updateIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            appName = intent.getStringExtra(APP_NAME);
            downUrl = "http://123.57.175.155:9120/servlet/DownLoadServlet?filename=" + appName;
            iconId = intent.getIntExtra(ICON_ID, -1);
            // 创建通知
            createNotification();
            // 开始下载
            try {
                downloadUpdateFile(downUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    /***
     * 创建通知栏
     */
    RemoteViews contentView;

    @SuppressWarnings("deprecation")
    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setSmallIcon(iconId);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), iconId));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        contentView = new RemoteViews(getPackageName(), R.layout.layout_notification_item);
        contentView.setImageViewResource(R.id.notificationImage, iconId);
        contentView.setTextViewText(R.id.notificationTitle, appName + "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        builder.setContent(contentView);
        builder.setTicker("开始下载");
        notification = builder.build();
        updateIntent = new Intent();

        updateIntent = new Intent(this, mCls);
        updateIntent.getPackage();
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        notification.contentIntent = pendingIntent;
        notificationManager.notify(notification_id, notification);

    }

    /***
     * 下载文件
     */
    public void downloadUpdateFile(String down_url) throws Exception {
        final OkHttpClient okHttpClient = AppClient.getOkhttpClient();
        final Request request = new Request.Builder().url(down_url).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = DOWN_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    File dir = new File(Server.filePath);
                    if (!dir.exists())
                        dir.mkdirs();
                    updateFile = new File(dir, appName);
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(updateFile);
                    long length = 0;
                    long timeMillis = System.currentTimeMillis();
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        length += len;
                        if (System.currentTimeMillis() - timeMillis > 1000) {//1秒刷新一次进度
                            refreshProgress(total, length);
                            timeMillis = System.currentTimeMillis();
                        }
                    }
                    fos.flush();

                    // 下载成功
                    Message message = handler.obtainMessage();
                    message.what = DOWN_OK;
                    handler.sendMessage(message);
                    installApk(updateFile, VersionUpdateService.this);
                } catch (final IOException e) {
                    Message message = handler.obtainMessage();
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 刷新进度条
     *
     * @param total
     * @param current
     */
    private void refreshProgress(long total, long current) {
        double x_double = current * 1.0;
        double tempresult = x_double / total;
        DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        String result = df1.format(tempresult);
        contentView.setTextViewText(R.id.notificationPercent, (int) (Float.parseFloat(result) * 100) + "%");
        contentView.setProgressBar(R.id.notificationProgress, 100, (int) (Float.parseFloat(result) * 100), false);
        notificationManager.notify(notification_id, notification);
    }

    // 下载完成后打开安装apk界面
    public static void installApk(File file, Context context) {
        Intent openFile = getFileIntent(context, file);
        context.startActivity(openFile);

    }

    public static Intent getFileIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uriForFile = null;
            uriForFile = FileProvider.getUriForFile(context, "com.example.uhfsdkdemo.fileprovider", file);
            if (uriForFile != null)
                intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }

}