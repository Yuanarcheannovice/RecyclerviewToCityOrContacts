package com.xz.rv.zm;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import com.jay.widget.StickyHeaders;
import com.xz.xadapter.XRvPureDataAdapter;
import com.xz.xadapter.xutil.XRvViewHolder;

/**
 * Created by xz on 2018/4/18 0018.
 * 选择城市
 */

public class SelectCityAdapter extends XRvPureDataAdapter<CityEntity> implements StickyHeaders, StickyHeaders.ViewSetup {
    private static final int HEADER_ITEM = 1000;

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getId() == -1) {
            return HEADER_ITEM;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemLayout(int viewType) {
        if (HEADER_ITEM == viewType) {
            return R.layout.item_home_select_city_head;
        } else {
            return R.layout.item_home_select_city;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull XRvViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER_ITEM) {
            if(TextUtils.equals(getItem(position).getInitials(),"#")){
                holder.setText(R.id.ihsch_title, "当前定位");
            }else if(TextUtils.equals(getItem(position).getInitials(),"热")){
                holder.setText(R.id.ihsch_title, "热门城市");
            }else {
                holder.setText(R.id.ihsch_title, getItem(position).getName());
            }
        } else {
           holder.setText(R.id.ihsc_name, getItem(position).getName());
        }
    }


    @Override
    public boolean isStickyHeader(int position) {
        return getItemViewType(position) == HEADER_ITEM;
    }

    @Override
    public void setupStickyHeaderView(View stickyHeader) {
        ViewCompat.setElevation(stickyHeader, 10);
    }

    @Override
    public void teardownStickyHeaderView(View stickyHeader) {
        ViewCompat.setElevation(stickyHeader, 0);
    }
}
