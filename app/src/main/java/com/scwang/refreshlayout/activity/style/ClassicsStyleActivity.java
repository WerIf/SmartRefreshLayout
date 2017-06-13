package com.scwang.refreshlayout.activity.style;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.adapter.BaseRecyclerAdapter;
import com.scwang.refreshlayout.adapter.SmartViewHolder;
import com.scwang.smartrefreshlayout.api.RefreshLayout;
import com.scwang.smartrefreshlayout.constant.SpinnerStyle;
import com.scwang.smartrefreshlayout.header.ClassicsHeader;

import java.util.Arrays;
import java.util.Date;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ClassicsStyleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private enum Item {
        背后固定("下拉的时候Header固定在背后"),
        尺寸拉伸("下拉的时候Header的高度跟随变大"),
        位置平移("下拉的时候Header的位置向下偏移"),
        默认主题("更改为默认主题颜色"),
        橙色主题("更改为橙色主题颜色"),
        红色主题("更改为红色主题颜色"),
        绿色主题("更改为绿色主题颜色"),
        蓝色主题("更改为蓝色主题颜色"),
        ;
        public String name;
        Item(String name) {
            this.name = name;
        }
    }

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private ClassicsHeader mClassicsHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_classics);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout = (RefreshLayout)findViewById(R.id.smart);
        mRefreshLayout.autoRefresh();

        mClassicsHeader = (ClassicsHeader)mRefreshLayout.getRefreshHeader();
        mClassicsHeader.setLastUpdateTime(new Date());

        View view = findViewById(R.id.recycler);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2,this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.name);
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
            mRecyclerView = recyclerView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (Item.values()[position]) {
            case 背后固定:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.FixedBehind);
                /**
                 * 由于是后面才设置，需要手动更改视图的位置
                 * 如果在 onCreate 或者 xml 中设置好 就不用手动调整位置了
                 */
                ((ViewGroup)mRefreshLayout).bringChildToFront(mRecyclerView);
                break;
            case 尺寸拉伸:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
                break;
            case 位置平移:
                mClassicsHeader.setSpinnerStyle(SpinnerStyle.Translate);
                break;
            case 默认主题:
                setThemeColor(R.color.colorPrimary, R.color.colorPrimaryDark);
                ((View)mRefreshLayout).setBackgroundResource(android.R.color.transparent);
                mRefreshLayout.setPrimaryColorsId(android.R.color.transparent, android.R.color.tertiary_text_dark);
                break;
            case 蓝色主题:
                setThemeColor(R.color.colorPrimary, R.color.colorPrimaryDark);
                break;
            case 绿色主题:
                setThemeColor(android.R.color.holo_green_light, android.R.color.holo_green_dark);
                break;
            case 红色主题:
                setThemeColor(android.R.color.holo_red_light, android.R.color.holo_red_dark);
                break;
            case 橙色主题:
                setThemeColor(android.R.color.holo_orange_light, android.R.color.holo_orange_dark);
                break;
        }
        mRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary, int colorPrimaryDark) {
        mToolbar.setBackgroundResource(colorPrimary);
        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        ((View)mRefreshLayout).setBackgroundResource(colorPrimary);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
    }


}