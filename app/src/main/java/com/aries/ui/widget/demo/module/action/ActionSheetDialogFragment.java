package com.aries.ui.widget.demo.module.action;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.util.NavigationBarControlUtil;
import com.aries.ui.widget.demo.util.SizeUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @Author: AriesHoo on 2019/4/23 14:33
 * @E-Mail: AriesHoo@126.com
 * @Function: DialogFragment使用示例
 * @Description:
 */
public class ActionSheetDialogFragment extends DialogFragment {

    private UIActionSheetDialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDialog = new UIActionSheetDialog.ListWeChatBuilder(getContext())
                .setBackgroundColor(Color.WHITE)
                .addItem("分享微信", R.drawable.ic_more_operation_share_friend)
                .addItem("分享朋友圈", R.drawable.ic_more_operation_share_moment)
                .addItem("分享微博", R.drawable.ic_more_operation_share_weibo)
                .addItem("分享短信", R.drawable.ic_more_operation_share_chat)
                .setTextDrawablePadding(SizeUtil.dp2px(28))
                .setNavigationBarControl(NavigationBarControlUtil.
                        getNavigationBarControl(true,
                                !NavigationBarControlUtil.isDialogDarkIcon()))
                .create()
                .setDragEnable(true);
        mDialog.getListView().setPadding(0, SizeUtil.dp2px(10), 0, SizeUtil.dp2px(10));
        return mDialog;
    }
}
