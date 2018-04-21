package com.xz.rv.zm;

import android.support.annotation.NonNull;

import com.xz.xadapter.XRvPureDataAdapter;
import com.xz.xadapter.xutil.XRvViewHolder;

/**
 * Created by xz on 2018/4/19 0019.
 * 选择城市右边的 标识条
 */

public class SelectCityRightAdapter extends XRvPureDataAdapter<String> {
    @Override
    public int getItemLayout(int viewType) {
        return R.layout.item_home_select_city_right;
    }

    @Override
    public void onBindViewHolder(@NonNull XRvViewHolder holder, int position) {
        holder.setText(R.id.ihscr_tag, getItem(position));
    }
}
