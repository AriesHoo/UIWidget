package com.aries.ui.widget.demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.AppUtil;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;

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
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(lLayoutContainer, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .setIndicatorColor(getResources().getColor(R.color.colorTextBlack))
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecutityType(AgentWeb.SecurityType.default_check)
                .createAgentWeb()//
                .ready()
                .go(url);

//        mAgentWeb.getLoader().loadUrl(url);
    }

    private void showDialog() {
        if (mAlertDialog == null)
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                        }
                    })//
                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null)
                                mAlertDialog.dismiss();
                            mContext.finish();
                        }
                    }).create();
        mAlertDialog.show();
        //show之后可获取对应Button对象设置文本颜色--show之前获取对象为null
        mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED);
//        mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
//        mAlertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.BLUE);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","progress:"+newProgress);
        }
    };


    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            titleBar.setTitleMainText(title);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
