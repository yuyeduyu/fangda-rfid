package com.example.uhfsdkdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;

/**
 * Created by fengye on 2016/9/21.
 * email 1040441325@qq.com
 * 主界面加载fragment的适配器
 */
public class TabMainAdapter extends FragmentPagerAdapter {
    private int count;
    private String[] titles;
    private Fragment[] mFragments;
    public TabMainAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        this.count=fragments.length;
        this.titles=titles;
        this.mFragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitle(titles[position]);
    }

    /**
     * @param title
     * @return 绘制的标题
     */
    private CharSequence getTitle(String title){
        SpannableString sb=new SpannableString(title);
        return sb;
    }
}

