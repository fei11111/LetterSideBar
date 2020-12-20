package com.fei.lettersidebar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fei.lettersidebar.R;
import com.fei.lettersidebar.adapter.LetterAdapter;
import com.fei.lettersidebar.mode.SortModel;
import com.fei.lettersidebar.util.CharacterParser;
import com.fei.lettersidebar.util.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

/**
 * @ClassName: LetterFilterListView
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-19 11:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-19 11:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LetterFilterListView extends RelativeLayout implements LetterSideBar.OnLetterSelectListener {

    private static final String TAG = "LetterFilterListView";
    private Context mContext;
    private TextView mTvToast;
    private RecyclerView mRecyclerView;
    private LetterSideBar mLetterSideBar;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator mPinyinComparator;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    // 全部的数据集合
    private List<SortModel> mSourceDateList;
    private LetterAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    public LetterFilterListView(Context context) {
        this(context, null);
    }

    public LetterFilterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterFilterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
        initData();
    }

    private void initData() {
        mPinyinComparator = new PinyinComparator();
        mCharacterParser = CharacterParser.getInstance();
        mSourceDateList = new ArrayList<>();
    }

    private void initListener() {
        mLetterSideBar.setOnLetterSelectListener(this);
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    private void initView() {
        inflate(mContext, R.layout.view_list_filter, this);
        mTvToast = findViewById(R.id.tv_toast_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLetterSideBar = findViewById(R.id.letter_bar);
    }

    @Override
    public void onLetterSelect(String letter) {
        if (mTvToast.getVisibility() != View.VISIBLE) {
            //做判断，防止过多重新绘制
            mTvToast.setVisibility(View.VISIBLE);
        }
        mTvToast.setText(letter);
        if (mAdapter != null) {
            int firstLetterPosition = mAdapter.getFirstLetterPosition(letter.charAt(0));
            if (firstLetterPosition != -1) {
                mRecyclerView.smoothScrollToPosition(firstLetterPosition);
                mToPosition = firstLetterPosition;
                mShouldScroll = true;
            }
        }
    }

    @Override
    public void dismiss() {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(800);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTvToast.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTvToast.startAnimation(animation);
    }

    /**
     * 刷新数据
     */
    public void refreshData(List<SortModel> sortModels) {
        mSourceDateList = mSourceDateList;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置recyclerView
     */
    public void setData(List<String> citys) {
        if (citys == null) return;
        //格式化
        mSourceDateList = format(citys);
        //排序
        Collections.sort(mSourceDateList, mPinyinComparator);
        mAdapter = new LetterAdapter(mContext, mSourceDateList);
        mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        if (mSourceDateList.size() > 0) {
            //初始化字母表选中位置
            String sortLetter = mSourceDateList.get(0).getSortLetter();
            mLetterSideBar.refreshLetter(sortLetter);
        }
    }

    private List<SortModel> format(List<String> citys) {
        List<SortModel> sortModelList = new ArrayList<>();
        for (String city : citys) {
            SortModel sortModel = new SortModel();
            sortModel.setName(city);
            //拼音
            String selling = mCharacterParser.getSelling(city);
            String upperCase = selling.substring(0, 1).toUpperCase();
            if (upperCase.matches("[A-Z]")) {
                sortModel.setSortLetter(upperCase);
            } else {
                sortModel.setSortLetter("#");
            }
            sortModelList.add(sortModel);
        }
        return sortModelList;
    }

    private RecyclerView.OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                String firstLetter = mAdapter.getFirstLetter(firstVisibleItemPosition);
                mLetterSideBar.refreshLetter(firstLetter);
                if (mShouldScroll) {
                    //字母表滑动
                    mShouldScroll = false;
                    mLinearLayoutManager.scrollToPositionWithOffset(mToPosition, 0);
                }
            }

        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
