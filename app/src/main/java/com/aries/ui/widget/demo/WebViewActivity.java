package com.aries.ui.widget.demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.AppUtil;
import com.aries.ui.widget.demo.util.FastFileUtil;
import com.aries.ui.widget.demo.util.NotificationUtil;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import butterknife.BindView;

/**
 * @Author: AriesHoo on 2019/3/1 12:00
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.lLayout_containerWebView) LinearLayout lLayoutContainer;

    private String url = "";
    private AlertDialog mAlertDialog;
    protected AgentWeb mAgentWeb;

    public static void start(Activity mActivity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        AppUtil.startActivity(mActivity, WebViewActivity.class, bundle);
    }

    @Override
    protected void setTitleBar() {
        titleBar.setRightTextDrawable(R.drawable.ic_share)
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtil.shareShareText(mContext, url);
                    }
                })
                .setTitleMainTextMarquee(true)
                .addLeftAction(titleBar.new ImageAction(R.drawable.ic_close, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                }));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView(Bundle bundle) {
        url = getIntent().getStringExtra("url");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(lLayoutContainer, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ContextCompat.getColor(mContext, R.color.colorTextBlack))
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        titleBar.setTitleMainText(title);
                    }
                })
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setAgentWebWebSettings(new AbsAgentWebSettings() {
                    private AgentWeb mAgentWeb;

                    @Override
                    protected void bindAgentWebSupport(AgentWeb agentWeb) {
                        this.mAgentWeb = agentWeb;
                    }

                    /**
                     * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
                     * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
                     * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
                     * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
                     * @param webView
                     * @param downloadListener
                     * @return WebListenerManager
                     */
                    @Override
                    public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                        return super.setDownloader(webView,
                                DefaultDownloadImpl
                                        .create((Activity) webView.getContext(),
                                                webView,
                                                mDownloadListenerAdapter,
                                                mDownloadListenerAdapter,
                                                this.mAgentWeb.getPermissionInterceptor()));
                    }
                })
                .createAgentWeb()//
                .ready()
                .go(url);
    }

    private void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })//
                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            mContext.finish();
                        }
                    }).create();
            mAlertDialog.show();
            //show之后可获取对应Button对象设置文本颜色--show之前获取对象为null
            mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
        }
    }

    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        private DownloadingService mDownloadingService;

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Toast.makeText(WebViewActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onStart:" + url);
            // 是否开启断点续传
            extra.setOpenBreakPointDownload(true)
                    //下载通知的icon
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    // 连接最大时长
                    .setConnectTimeOut(6000)
                    // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setBlockMaxTime(10 * 60 * 1000)
                    // 下载最大时长
                    .setDownloadTimeOut(Long.MAX_VALUE)
                    // 串行下载更节省资源哦
                    .setParallelDownload(false)
                    // false 关闭进度通知
                    .setEnableIndicator(true)
                    // 自定义请求头
                    .addHeader("Cookie", "xx")
                    // 下载完成自动打开
                    .setAutoOpen(false)
                    // 强制下载，不管网络网络类型
                    .setForceDownload(true);
            return false;
        }

        /**
         *
         * 不需要暂停或者停止下载该方法可以不必实现
         * @param url
         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
         */
        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            mDownloadingService = downloadingService;
            Log.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }

        /**
         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
         * @param url
         * @param downloadingService
         */
        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
            mDownloadingService = null;
            Log.i(TAG, "onUnbindService:" + url);
        }

        /**
         *
         * @param url  下载链接
         * @param loaded  已经下载的长度
         * @param length    文件的总大小
         * @param usedTime   耗时 ，单位ms
         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
         */
        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            Log.i(TAG, "onProgress:" + mProgress);
            if (mProgress == 100) {
                mDownloadingService.shutdownNow().performReDownload();
            }
            super.onProgress(url, loaded, length, usedTime);
        }

        /**
         *
         * @param path 文件的绝对路径
         * @param url  下载地址
         * @param throwable    如果异常，返回给用户异常
         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
         */
        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            Log.i(TAG, "onResult_path:" + path + ";url:" + url + ";throwable:" + throwable);
            //下载成功
            if (null == throwable) {
                Log.i(TAG,"下载完成:开始安装");
                NotificationUtil.getInstance().cancelAll();
                FastFileUtil.installApk(new File(path));
                //do you work
            } else {//下载失败

            }
            // true  不会发出下载完成的通知 , 或者打开文件
            return false;
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mAgentWeb.back()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
        if (titleBar != null) {
            titleBar.getTextView(Gravity.CENTER | Gravity.TOP).requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
