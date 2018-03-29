package com.aries.ui.widget.demo.module.alert;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.view.radius.RadiusEditText;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.alert.UIAlertDialog;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.SizeUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created: AriesHoo on 2017/7/18 14:50
 * E-Mail: AriesHoo@126.com
 * Function: UIAlertDialog示例
 * Description:
 */
public class AlertActivity extends BaseActivity {

    @BindView(R.id.sBar_num) SeekBar sBarNum;
    @BindView(R.id.tv_statusNum) TextView tvNum;
    @BindView(R.id.sBtn_titleAlert) SwitchCompat sBtnTitle;
    @BindView(R.id.sBtn_titleColorAlert) SwitchCompat sBtnTitleColor;
    @BindView(R.id.sBtn_msgColorAlert) SwitchCompat sBtnMsgColor;
    @BindView(R.id.sBtn_buttonColorAlert) SwitchCompat sBtnButtonColor;
    @BindView(R.id.sBtn_backAlert) SwitchCompat sBtnBack;
    @BindView(R.id.rtv_showAlert) RadiusTextView rtvShow;

    private boolean isShowTitle = true;
    private boolean isDefaultTitleColor = false;
    private boolean isDefaultMsgColor = false;
    private boolean isDefaultButtonColor = false;
    private boolean isBackDim = true;

    private int num = 2;

    private String mFormatName = "%1s<font color=\"red\">%2s</font>%3s";

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText(UIAlertDialog.class.getSimpleName());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_alert;
    }

    @Override
    protected void initView(Bundle var1) {

        sBarNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                num = progress + 1;
                tvNum.setText(num + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sBtnTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowTitle = isChecked;
                sBtnTitle.setText(isShowTitle ? "显示Title" : "隐藏Title");
                sBtnTitleColor.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);
            }
        });

        sBtnTitleColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultTitleColor = isChecked;
                sBtnTitleColor.setText(isDefaultTitleColor ? "默认Title文本颜色" : "自定义Title文本颜色");
            }
        });
        sBtnMsgColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultMsgColor = isChecked;
                sBtnMsgColor.setText(isDefaultMsgColor ? "默认Message文本颜色" : "自定义Message文本颜色");
            }
        });

        sBtnButtonColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultButtonColor = isChecked;
                sBtnButtonColor.setText(isDefaultButtonColor ? "默认Button文本颜色" : "自定义Button文本颜色");
            }
        });

        sBtnBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBackDim = isChecked;
                sBtnBack.setText(isBackDim ? "背景半透明" : "背景全透明");
            }
        });
        sBtnTitle.setChecked(true);
        sBtnTitleColor.setChecked(true);
        sBtnMsgColor.setChecked(true);
        sBtnButtonColor.setChecked(true);
        sBtnBack.setChecked(true);
        sBarNum.setProgress(1);
    }

    @OnClick({R.id.rtv_showAlert, R.id.rtv_showQqAlert, R.id.rtv_showAllAlert, R.id.rtv_editAlert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_showAlert:
                new UIAlertDialog.DividerIOSBuilder(this)
                        .setTitle(isShowTitle ? UIAlertDialog.class.getSimpleName() : "")
                        .setTitleTextColorResource(isDefaultTitleColor ? R.color.colorAlertTitle : android.R.color.holo_blue_dark)
                        .setMessage("1、本次更新修复多个重大BUG\n2、新增用户反馈接口")
                        .setMessageTextColorResource(isDefaultMsgColor ? R.color.colorAlertMessage : android.R.color.holo_green_dark)
                        .setNegativeButton(num > 1 ? "否定" : "", onAlertClick)
                        .setNegativeButtonTextColorResource(isDefaultButtonColor ? R.color.colorAlertButton : android.R.color.holo_red_dark)
                        .setPositiveButton("肯定", onAlertClick)
                        .setMinHeight(SizeUtil.dp2px(160))
                        .setPositiveButtonTextColorResource(isDefaultButtonColor ? R.color.colorAlertButton : android.R.color.holo_orange_dark)
                        .setNeutralButton(num > 2 ? "中性" : "", onAlertClick)
                        .setNeutralButtonTextColorResource(isDefaultButtonColor ? R.color.colorAlertButton : android.R.color.holo_purple)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showQqAlert:
                UIAlertDialog dialog = new UIAlertDialog.DividerQQBuilder(this)
                        .setTitle(isShowTitle ? "退出群聊" : "")
                        .setTitleTextColor(isDefaultTitleColor ? Color.BLACK : Color.BLUE)
                        .setMessage("你将退出  四川移动爱分享抢流量(XXXXXXXX) 退群通知仅群管理员可见。")
                        .setMessageTextColor(isDefaultMsgColor ? Color.BLACK : Color.GREEN)
                        .setNegativeButton(num > 1 ? "取消" : "", onAlertClick)
                        .setNegativeButtonTextColor(isDefaultButtonColor ? Color.BLACK : Color.RED)
                        .setPositiveButton("退出", onAlertClick)
                        .setPositiveButtonTextColor(isDefaultButtonColor ? Color.BLACK : Color.YELLOW)
                        .setNeutralButton(num > 2 ? "中性" : "", onAlertClick)
                        .setNeutralButtonTextColor(isDefaultButtonColor ? Color.BLACK : Color.BLUE)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f);
                dialog.show();
                break;
            case R.id.rtv_showAllAlert:
                UIAlertDialog alertDialog = new UIAlertDialog.DividerQQBuilder(this)
                        //设置背景--包括根布局及Button
                        .setBackgroundColor(Color.WHITE)
//                        .setBackground(drawable)
//                        .setBackgroundResource(resId)
                        //设置按下背景--Button
                        .setBackgroundPressedColor(Color.argb(255, 240, 240, 240))
//                        .setBackgroundPressed(drawable)
//                        .setBackgroundPressedResource(resId)
                        //背景圆角(当背景及按下背景为ColorDrawable有效)-根布局及Button
                        .setBackgroundRadius(6f)
//                        .setBackgroundRadiusResource(resId)
                        //设置统一padding
                        .setPadding(SizeUtil.dp2px(20))
                        //设置根布局最小高度
                        .setMinHeight(SizeUtil.dp2px(160))
                        .setElevation(12f)

                        //设置Title上边分割线颜色--推荐
                        .setTitleDividerColor(Color.RED)
//                        .setTitleDividerResource(resId)
//                        .setTitleDivider(drawable)
                        //设置Title分割线高度
                        .setTitleDividerHeight(SizeUtil.dp2px(4))
//                        .setTitleDividerHeightResource(resId)
                        //设置TextView对应的尺寸单位
                        .setTextSizeUnit(TypedValue.COMPLEX_UNIT_DIP)
                        .setLineSpacingExtra(0f)
                        .setLineSpacingMultiplier(1.0f)
                        //设置Title文本
                        .setTitle("UIAlertDialog示例头部")
//                        .setTitle(resId)
                        //设置Title文本颜色
                        .setTitleTextColor(Color.BLACK)
//                        .setTitleTextColor(ColorStateList)
//                        .setTitleTextColorResource(resId)
                        //设置Title文本尺寸
                        .setTitleTextSize(20f)
                        //设置Title文本对齐方式
                        .setTitleTextGravity(Gravity.CENTER)
                        //设置Title文本是否加粗
                        .setTitleTextFakeBoldEnable(false)

                        //设置Message文本
                        .setMessage(Html.fromHtml(String.format(mFormatName, "你将退出 ", "四川移动爱分享抢流量(XXXXXXXX)", "退群通知仅群管理员可见。")))
//                        .setMessage(resId)
                        //设置Message文本颜色
                        .setMessageTextColor(Color.BLACK)
//                        .setMessageTextColor(ColorStateList)
//                        .setMessageTextColorResource(resId)
                        //设置Message文本尺寸
                        .setMessageTextSize(16f)
                        //设置Message文本对齐方式
                        .setMessageTextGravity(Gravity.CENTER)
                        //设置Title文本是否加粗
                        .setMessageTextFakeBoldEnable(false)

                        //设置View --始终在Message下边
//                        .setView(View)
//                        .setView(layoutId)

                        //设置是否去掉Button按下阴影-5.0以后的新特性
                        .setBorderLessButtonEnable(true)
                        //文本及点击事件
                        .setNegativeButton("取消", onAlertClick)
//                        .setNegativeButton(resId,click)
                        //文本颜色
                        .setNegativeButtonTextColor(Color.BLACK)
//                        .setNegativeButtonTextColor(ColorStateList)
//                        .setNegativeButtonTextColorResource(resId)
                        //文本尺寸
                        .setNegativeButtonTextSize(18f)
                        //是否粗体
                        .setNegativeButtonFakeBoldEnable(false)

                        //文本及点击事件
                        .setNeutralButton("考虑", onAlertClick)
//                        .setNeutralButton(resId,click)
                        //文本颜色
                        .setNeutralButtonTextColor(Color.BLACK)
//                        .setNeutralButtonTextColor(ColorStateList)
//                        .setNeutralButtonTextColorResource(resId)
                        //文本尺寸
                        .setNeutralButtonTextSize(18f)
                        //是否粗体
                        .setNeutralButtonFakeBoldEnable(false)

                        //文本及点击事件
                        .setPositiveButton("退出", onAlertClick)
//                        .setPositiveButton(resId,click)
                        //文本颜色
                        .setPositiveButtonTextColor(Color.BLACK)
//                        .setPositiveButtonTextColor(ColorStateList)
//                        .setPositiveButtonTextColorResource(resId)
                        //文本尺寸
                        .setPositiveButtonTextSize(18f)
                        //是否粗体
                        .setPositiveButtonFakeBoldEnable(false)

                        //设置点击返回键是否可关闭Window
                        .setCancelable(true)
                        //设置点击非布局是否关闭Window
                        .setCanceledOnTouchOutside(true)
                        //设置Window dismiss()监听
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        //设置Window cancel()监听
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        })
                        //设置 window show()监听
                        .setOnShowListenerer(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {

                            }
                        })
                        //设置Window 键盘事件监听
                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                return false;
                            }
                        })
                        .setOnTextViewLineListener(new BasisDialog.OnTextViewLineListener() {
                            @Override
                            public void onTextViewLineListener(TextView textView, int lineCount) {
                                switch (textView.getId()) {
                                    case R.id.tv_titleAlertDialog:
                                        break;
                                    case R.id.tv_messageAlertDialog:
                                        break;
                                    case R.id.btn_negativeAlertDialog:
                                        break;
                                    case R.id.btn_neutralAlertDialog:
                                        break;
                                    case R.id.btn_positiveAlertDialog:
                                        break;
                                }
                            }
                        })

                        //创建Dialog
                        .create()
                        //设置Window宽度
                        .setWidth(WindowManager.LayoutParams.WRAP_CONTENT)
                        //设置Window高度
                        .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                        //设置Window 阴影程度
                        .setDimAmount(0.6f)
                        //设置window其它属性
//                        .setAttributes(WindowManager.LayoutParams)
                        //设置window动画
//                        .setWindowAnimations(resId)
                        //设置Window 位置
                        .setGravity(Gravity.CENTER);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                alertDialog.show();
                break;
            case R.id.rtv_editAlert:
                final RadiusEditText editText = new RadiusEditText(AlertActivity.this);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.topMargin = SizeUtil.dp2px(12);
                editText.getDelegate()
                        .setTextColor(Color.GRAY)
                        .setRadius(6f)
                        .setBackgroundColor(Color.WHITE)
                        .setStrokeColor(Color.GRAY)
                        .setStrokeWidth(SizeUtil.dp2px(0.5f));
                editText.setMinHeight(SizeUtil.dp2px(40));
                editText.setGravity(Gravity.CENTER_VERTICAL);
                editText.setPadding(SizeUtil.dp2px(12), 0, SizeUtil.dp2px(12), 0);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                editText.setHint("请输入内容");
                editText.setLayoutParams(params);
                new UIAlertDialog.DividerIOSBuilder(this)
                        .setTitle(isShowTitle ? UIAlertDialog.class.getSimpleName() : "")
                        .setTitleTextColorResource(isDefaultTitleColor ? R.color.colorAlertTitle : android.R.color.holo_blue_dark)
                        .setView(editText)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(text)) {
                                    return;
                                }
                                Toast.makeText(mContext, "你输入的是:" + text, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
//                editText.requestFocus();
                break;

        }
    }

    DialogInterface.OnClickListener onAlertClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg = "";
            switch (which) {
                case BUTTON_NEGATIVE:
                    msg = "否定";
                    break;
                case BUTTON_POSITIVE:
                    msg = "肯定";
                    break;
                case BUTTON_NEUTRAL:
                    msg = "中性";
                    break;
            }
            if (dialog instanceof UIAlertDialog) {
                msg = ((UIAlertDialog) dialog).getButton(which).getText().toString();
            }
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
