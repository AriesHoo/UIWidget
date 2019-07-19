package com.aries.ui.manager;

import com.aries.ui.listener.LifecycleListener;

import androidx.fragment.app.Fragment;

/**
 * @Author: AriesHoo on 2019/7/18 13:58
 * @E-Mail: AriesHoo@126.com
 * @Function: 生命周期监控Fragment
 * @Description:
 */
public class LifecycleManagerFragment extends Fragment {

    private LifecycleListener mListener;

    public LifecycleManagerFragment(LifecycleListener listener) {
        mListener = listener;
    }


    @Override
    public void onStart() {
        if (mListener != null) {
            mListener.onStart();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mListener != null) {
            mListener.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mListener != null) {
            mListener.onDestroy();
        }
        super.onDestroy();
    }
}
