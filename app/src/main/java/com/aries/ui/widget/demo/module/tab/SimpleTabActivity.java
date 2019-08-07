package com.aries.ui.widget.demo.module.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.module.tab.adapter.SimpleHomeAdapter;

import butterknife.BindView;


/**
 * @Author: AriesHoo on 2019/4/16 17:49
 * @E-Mail: AriesHoo@126.com
 * @Function: 示例主页
 * @Description:
 */
public class SimpleTabActivity extends BaseActivity {
    @BindView(R.id.lv_container_simple_tab) ListView mLvContainer;
    private Context mContext = this;
    private final String[] mItems = {"SlidingTabLayout", "CommonTabLayout", "SegmentTabLayout"};
    private final Class<?>[] mClasses = {SlidingTabActivity.class, CommonTabActivity.class,
            SegmentTabActivity.class};

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_simple_tab;
    }

    @Override
    protected void initView(Bundle var1) {
        mLvContainer.setCacheColorHint(Color.TRANSPARENT);
        mLvContainer.setFadingEdgeLength(0);
        mLvContainer.setAdapter(new SimpleHomeAdapter(mContext, mItems));

        mLvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, mClasses[position]);
                startActivity(intent);
            }
        });
    }

}
