package com.atguigu.chinarallway.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.atguigu.chinarallway.fragment.ProducerFragment;

/**
 * Created by Kiboooo on 2017/10/9.
 */

public class ProducerAdapter extends FragmentPagerAdapter {

    private String[] mTitle = {"梁板基本信息", "楔形块尺寸和泄水孔尺寸", "混凝土保护层厚度和预埋件情况"};
    private Context mContext;

    public ProducerAdapter(FragmentManager fm,Context MContext) {
        super(fm);
        mContext = MContext;
    }

    @Override
    public Fragment getItem(int position) {
        return ProducerFragment.newInstance(position);
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
