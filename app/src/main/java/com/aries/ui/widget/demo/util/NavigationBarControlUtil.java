package com.aries.ui.widget.demo.util;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.i.NavigationBarControl;

import androidx.core.content.ContextCompat;

/**
 * @Author: AriesHoo on 2019/4/23 14:50
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class NavigationBarControlUtil {

    public static NavigationBarControl getNavigationBarControl(boolean isNavigation,boolean isNavigationPlus){
        return new NavigationBarControl() {
            @Override
            public boolean setNavigationBar(Dialog dialog, NavigationViewHelper helper, View bottomView) {
                Drawable drawableTop = ContextCompat.getDrawable(dialog.getContext(), R.color.colorLineGray);
                DrawableUtil.setDrawableWidthHeight(drawableTop, SizeUtil.getScreenWidth(), SizeUtil.dp2px(0.5f));
                helper.setNavigationViewDrawableTop(drawableTop)
                        .setPlusNavigationViewEnable(isNavigationPlus)
                        .setNavigationBarLightMode(true)
                        .setNavigationViewColor(Color.argb(isDialogDarkIcon() ? 0 : 60, 0, 0, 0))
                        .setNavigationLayoutColor(Color.WHITE);
                //导航栏在底部控制才有意义,不然会很丑;开发者自己决定;这里仅供参考
                return NavigationBarUtil.isNavigationAtBottom(dialog.getWindow()) && isNavigation;
            }
        };
    }

    public static boolean isDialogDarkIcon() {
        return (RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0));
    }
}
