package com.quxianggif.user.adapter;

import com.quxianggif.common.ui.BaseFragment;
import com.quxianggif.core.model.Tab;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * author jingting
 * date : 2020/12/4上午11:36
 */
public class ProjectPagerAdapter extends FragmentPagerAdapter {

    private List<Tab> titles;
    private List<BaseFragment> fragments;

    public ProjectPagerAdapter(@NonNull FragmentManager fm, List<Tab> titles, List<BaseFragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getName();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

}
