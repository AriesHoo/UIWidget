package com.aries.ui.widget.demo.module.loading;

import android.app.Dialog;
import android.os.Bundle;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.progress.UIProgressDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @Author: AriesHoo on 2019/4/23 15:16
 * @E-Mail: AriesHoo@126.com
 * @Function: DialogFragment使用示例
 * @Description:
 */
public class ProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new UIProgressDialog.WeBoBuilder(getContext())
                .setMessage(R.string.loading)
                .setIndeterminateDrawable(R.drawable.dialog_loading_wei_bo)
                .setBackgroundRadiusResource(R.dimen.dp_radius_loading)
                .create();
    }
}
