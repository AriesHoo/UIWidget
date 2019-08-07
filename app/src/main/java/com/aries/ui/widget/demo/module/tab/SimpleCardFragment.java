package com.aries.ui.widget.demo.module.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.aries.ui.widget.demo.R;


/**
 * @Author: AriesHoo on 2019/4/16 17:48
 * @E-Mail: AriesHoo@126.com
 * @Function: Fragment 示例
 * @Description:
 */
@SuppressLint("ValidFragment")
public class SimpleCardFragment extends Fragment {
    private String mTitle;

    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_simple_card, null);
        TextView card_title_tv = v.findViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);
        return v;
    }
}