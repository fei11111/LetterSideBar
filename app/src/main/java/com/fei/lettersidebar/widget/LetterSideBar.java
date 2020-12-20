package com.fei.lettersidebar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fei.lettersidebar.R;

import androidx.annotation.Nullable;

/**
 * @ClassName: LetterSideBar
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-17 20:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-17 20:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LetterSideBar extends View {

    // 26个字母
    public static String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private int mNormalColor = Color.BLUE;//没选中状态颜色
    private int mSelectColor = Color.RED;//选中状态颜色
    private String mCurrentLetter = "";//当前选中letter
    private float mLetterSize = 12f;//字体大小
    private Paint mNormalPaint;
    private Paint mSelectPaint;
    private int mWidth = 0;//View宽度
    private int mHeight = 0;//View高度
    private int mItemHeight;//一个字母的高度

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initPaint();
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalPaint.setColor(mNormalColor);
        mNormalPaint.setTextSize(mLetterSize);
        mNormalPaint.setDither(true);

        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(mSelectColor);
        mSelectPaint.setTextSize(mLetterSize);
        mSelectPaint.setDither(true);
    }


    /**
     * 获取属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);
        mNormalColor = typedArray.getColor(R.styleable.LetterSideBar_normalColor, mNormalColor);
        mSelectColor = typedArray.getColor(R.styleable.LetterSideBar_selectColor, mSelectColor);
        mLetterSize = typedArray.getDimension(R.styleable.LetterSideBar_letterSize, sp2px(mLetterSize));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高度肯定是Match_Parent,所以要获取宽度

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            //wrap_content
            mWidth = (int) mNormalPaint.measureText(mLetters[0]);
            mWidth += getPaddingLeft() + getPaddingRight();
        } else {
            mWidth = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        }
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        //每个字母的高度
        mItemHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mLetters.length;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < mLetters.length; i++) {
            float measureTextWidth = mNormalPaint.measureText(mLetters[i]);
            float x = mWidth / 2 - measureTextWidth / 2;
            Paint.FontMetrics fontMetrics = mNormalPaint.getFontMetrics();
            float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            float y = getPaddingTop() + mItemHeight * i + mItemHeight / 2 + dy;
            if (mLetters[i].equals(mCurrentLetter)) {
                //选中
                canvas.drawText(mLetters[i], x, y, mSelectPaint);
            } else {
                //未选中
                canvas.drawText(mLetters[i], x, y, mNormalPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();

                //保证在字母区域
                if (y < getPaddingTop() || y > mHeight - getPaddingBottom()) {
                    if (onLetterSelectListener != null) {
                        onLetterSelectListener.dismiss();
                    }
                    return true;
                }

                int position = (int) ((y - getPaddingTop() * 1f) / mItemHeight);
                if (position >= mLetters.length) {
                    return true;
                }
                String letter = mLetters[position];//手指选中的letter
                if (!letter.equals(mCurrentLetter)) {
                    //跟之前选中letter比较，不同才刷新
                    mCurrentLetter = letter;
                    //回调出去
                    if (onLetterSelectListener != null) {
                        onLetterSelectListener.onLetterSelect(mCurrentLetter);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (onLetterSelectListener != null) {
                    onLetterSelectListener.dismiss();
                }
        }
        return true;
    }

    /**
     * 根据recyclerView的改变而改变
     * */
    public void refreshLetter(String letter) {
        if(!letter.equals(mCurrentLetter)) {
            mCurrentLetter = letter;
            invalidate();
        }
    }


    private OnLetterSelectListener onLetterSelectListener;

    public void setOnLetterSelectListener(OnLetterSelectListener onLetterSelectListener) {
        this.onLetterSelectListener = onLetterSelectListener;
    }

    public interface OnLetterSelectListener {
        void onLetterSelect(String letter);

        void dismiss();
    }
}
