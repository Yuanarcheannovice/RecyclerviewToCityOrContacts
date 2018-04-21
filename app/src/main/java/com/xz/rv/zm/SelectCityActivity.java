package com.xz.rv.zm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.jay.widget.StickyHeadersLinearLayoutManager;
import com.xz.xadapter.XRvPureAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xz
 * @date 2018/4/17 0017
 */

public class SelectCityActivity extends AppCompatActivity {
    private Disposable msubscribe;

    private SelectCityAdapter mLeftAdapter;
    private SelectCityRightAdapter mRightAdapter;
    private RecyclerView mRightRv;
    /**
     * 保存 title标识的position位置
     */
    private HashMap<String, Integer> mTagMap = new HashMap<>();
    private StickyHeadersLinearLayoutManager<SelectCityAdapter> mLeftManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_select_city);
        onInitView();
        onSetListener();
        onInitData();
    }


    protected void onInitView() {
        setTitle("选择城市");
        RecyclerView leftRv = findViewById(R.id.ahsc_left);
        //
        leftRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mLeftManager = new StickyHeadersLinearLayoutManager(this);
        leftRv.setLayoutManager(mLeftManager);
        mLeftAdapter = new SelectCityAdapter();
        leftRv.setAdapter(mLeftAdapter);
        //
        mRightRv = findViewById(R.id.ahsc_right);
        GridLayoutManager gridManager = new GridLayoutManager(this, 28);
        gridManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRightRv.setLayoutManager(gridManager);
        mRightAdapter = new SelectCityRightAdapter();
        mRightRv.setAdapter(mRightAdapter);
    }

    protected void onSetListener() {
        mLeftAdapter.setOnItemClickListener(new XRvPureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mLeftAdapter.getItem(position).getId() > 0) {

                }
            }
        });
        //添加按下时间，用来处理滑动时的事件
        mRightRv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            int oldPosition = -1;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childViewUnder = rv.findChildViewUnder(e.getX(), e.getY());
                if (childViewUnder != null) {
                    int childAdapterPosition = rv.getChildAdapterPosition(childViewUnder);
                    Integer integer = mTagMap.get(mRightAdapter.getItem(childAdapterPosition));
                    if (integer != null && oldPosition != integer) {
                        oldPosition = integer;
                        mLeftManager.scrollToPositionWithOffset(integer, 0);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    protected void onInitData() {
        String[] rightStr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        List<String> rightList = new ArrayList<>();
        rightList.add("#");
        rightList.add("热");
        rightList.addAll(Arrays.asList(rightStr));
        mRightAdapter.setDatas(rightList);

        msubscribe = Observable.create(new ObservableOnSubscribe<List<CityEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CityEntity>> emitter) throws Exception {
                List<CityEntity> provinceList = AddressUtil.getProvinceList(SelectCityActivity.this.getApplicationContext());
                //根据字母，把城市排序
                Collections.sort(provinceList, new Comparator<CityEntity>() {
                    @Override
                    public int compare(CityEntity o1, CityEntity o2) {
                        Collator collator = Collator.getInstance(Locale.CHINA);
                        return collator.compare(o1.getName(), o2.getName());
                    }
                });
                //插入当前定位
                provinceList.add(0, new CityEntity(815, "北京", "#", -1));
                //插入热门城市
                provinceList.add(1, new CityEntity(815, "北京", "热", -2));
                provinceList.add(2, new CityEntity(816, "上海", "热", -2));
                provinceList.add(3, new CityEntity(82, "深圳", "热", -2));

                List<CityEntity> cityList = new ArrayList<>();
                //把城市挑选出来,
                for (int i = 0; i < provinceList.size(); i++) {
                    CityEntity cityEntity = provinceList.get(i);
                    //parentId为0的是省份
                    if (cityEntity.getParentId() != 0) {
                        //在中间插入字母
                        if (i == 0) {
                            mTagMap.put(cityEntity.getInitials(), cityList.size());
                            //当第一个开始的时候，需要先设定 首字母
                            cityList.add(CityEntity.newInitials(cityEntity.getName(), cityEntity.getInitials()));
                        } else if (!TextUtils.equals(cityEntity.getInitials(), provinceList.get(i - 1).getInitials())) {
                            //如果当前首字母，与上一个首字母不同，那么取当前首字母作为 此字母 序列第一个
                            //因为从0开始计算，而集合大小，是从1开始的，所有添加坐标应该放在 添加地址前面
                            mTagMap.put(cityEntity.getInitials(), cityList.size());
                            cityList.add(CityEntity.newInitials(cityEntity.getInitials()));
                        }
                        cityList.add(cityEntity);
                    }
                }
                emitter.onNext(cityList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CityEntity>>() {
                    @Override
                    public void accept(List<CityEntity> citys) throws Exception {
                        mLeftAdapter.setDatas(citys, true);
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (msubscribe != null) {
            msubscribe.dispose();
        }
    }

}
