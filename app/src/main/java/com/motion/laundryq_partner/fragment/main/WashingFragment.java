package com.motion.laundryq_partner.fragment.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WashingFragment extends Fragment {
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TabLayout tabLayout;

    public WashingFragment() {
        // Required empty public constructor
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_washing, container, false);
        ButterKnife.bind(this, v);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ReadyToWashFragment(), "Siap Cuci");
        adapter.addFragment(new ReadyToSendFragment(), "Siap Kirim");
        viewPager.setAdapter(adapter);
    }
}
