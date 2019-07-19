package com.aries.ui.widget.demo.module.title;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.module.alert.AlertActivity;
import com.aries.ui.widget.demo.util.SizeUtil;

import butterknife.BindView;

/**
 * @Author: AriesHoo on 2019/4/11 13:01
 * @E-Mail: AriesHoo@126.com
 * @Function: titleBar与底部状态栏使用
 * @Description:
 */
public class TitleEditActivity extends BaseActivity implements KeyboardHelper.OnKeyboardVisibilityChangedListener {


    @BindView(R.id.et_bottomTitle) EditText mEtBottom;
    @BindView(R.id.tv_tipTitleEdit) TextView mTvTip;

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setOnKeyboardVisibilityChangedListener(this);
    }

    @Override
    protected void setTitleBar() {
        titleBar.setRightTextDrawable(R.drawable.ic_menu)
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity( AlertActivity.class);
                    }
                });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_with_edit_text;
    }

    @Override
    protected void initView(Bundle var1) {
    }

    @Override
    public boolean onKeyboardVisibilityChanged(Activity activity, boolean isOpen, int heightDiff, int navigationHeight) {
        mTvTip.setText("软键盘开启状态:" + isOpen + ";追加paddingBottom:" + heightDiff + ";虚拟导航栏高度:" + navigationHeight);
        mTvTip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f);
        mTvTip.getPaint().setFakeBoldText(true);
        int padding = SizeUtil.dp2px(12);
        mTvTip.setPadding(padding, padding, padding, 0);
        return false;
    }
}
