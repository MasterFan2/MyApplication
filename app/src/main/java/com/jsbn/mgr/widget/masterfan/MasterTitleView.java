package com.jsbn.mgr.widget.masterfan;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsbn.mgr.R;

public class MasterTitleView extends RelativeLayout {

    private TextView leftBtn;

    private ImageView rightBtn;

    private TextView contentTxt;

    private View titleView;

    //title text color
    private int titleTextColor = 0xFFFFFF;
    private int titleBackgroundColor = 0xFF3F8AF8;


    private int rightButtonBackground = -1;

    public MasterTitleView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MasterTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MasterTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * initialize
     *
     * @param attrs
     * @param defStyle
     */
    private void init(Context mContext, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.m_title_layout, this);

        leftBtn = (TextView) view.findViewById(R.id.m_title_left_btn);
        rightBtn = (ImageView) view.findViewById(R.id.m_title_right_btn);
        contentTxt = (TextView) view.findViewById(R.id.m_title_context_txt);
//        MTViewUtils.inject(this, view);

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MasterTitleView, defStyle, 0);

        String strRightButtonText = a.getString(R.styleable.MasterTitleView_rightButtonText);
        String strTitleText = a.getString(R.styleable.MasterTitleView_titleText);
        String strLeftText = a.getString(R.styleable.MasterTitleView_leftButtonText);

        boolean leftButtonHide = a.getBoolean(R.styleable.MasterTitleView_leftButtonHide, false);
        boolean rightButtonHide = a.getBoolean(R.styleable.MasterTitleView_rightButtonHide, false);

        titleTextColor = a.getColor(R.styleable.MasterTitleView_titleTxtColor, titleTextColor);
        titleBackgroundColor = a.getColor(R.styleable.MasterTitleView_titleBackgroundColor, titleBackgroundColor);
        int titleBackgroundResource = a.getResourceId(R.styleable.MasterTitleView_titleBackground, -1);

        int leftBtnColor = a.getColor(R.styleable.MasterTitleView_leftButtonColor, -1);
        if(leftBtnColor != -1) leftBtn.setTextColor(leftBtnColor);

        rightButtonBackground = a.getResourceId(R.styleable.MasterTitleView_rightButtonBackground, -1);


        if(rightButtonBackground != -1){
            rightBtn.setBackgroundResource(rightButtonBackground);
        }

        if(TextUtils.isEmpty(strLeftText)) {
            leftBtn.setText(strLeftText);
        }

        if(titleBackgroundResource != -1) view.findViewById(R.id.title_root_view).setBackgroundResource(titleBackgroundResource);
        else                              view.findViewById(R.id.title_root_view).setBackgroundColor(titleBackgroundColor);

        //
        int leftResourceId = a.getResourceId(R.styleable.MasterTitleView_leftButtonResource, -1);
        if(-1 != leftResourceId){
            //leftBtn.setImageResource(leftResourceId);
        }

        contentTxt.setTextColor(titleTextColor);
        //
        if (leftButtonHide) hideLeftBtn();
        if (rightButtonHide) hideRightBtn();

        //
        setRightBtnText(strRightButtonText);
        setTitleText(strTitleText);

        //
        a.recycle();
    }

    public void setLeftBtnClickListener(OnClickListener listener) {
        leftBtn.setOnClickListener(listener);
    }

    public void setRightBtnClickListener(OnClickListener listener) {
        rightBtn.setOnClickListener(listener);
    }

    public void setRightBtnText(String text) {
//        if (TextUtils.isEmpty(text)) {
//            rightBtn.setText("  ");
//        } else {
//            rightBtn.setText(text);
//        }

    }

    public void setTitleText(String text) {
        contentTxt.setText(text);
    }

    public void hideRightBtn() {
//        rightBtn.setText("   ");
        rightBtn.setEnabled(false);
        rightBtn.setVisibility(View.INVISIBLE);
    }

    public  void setLeftBtnText(String txt) {
        leftBtn.setText(txt);
    }

    public void showRightBtn() {
//        rightBtn.setText("ɾ��");
        rightBtn.setEnabled(true);
        rightBtn.setVisibility(View.VISIBLE);
    }


    public void hideLeftBtn() {
        leftBtn.setVisibility(View.INVISIBLE);
    }
}