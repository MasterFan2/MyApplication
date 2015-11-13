package com.jsbn.mgr.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsbn.mgr.R;

import butterknife.ButterKnife;

/**
 * Created by 13510 on 2015/9/14.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentFeature config = this.getClass().getAnnotation(FragmentFeature.class);
        if(config == null) throw new IllegalArgumentException("not set resources");
        if(config.layout() <= 0) throw new RuntimeException("resources error!");

        //
        View view = inflater.inflate(config.layout(), container, false);

        ButterKnife.bind(this, view);

        //
        initialize();

        //
        return view;
    }

    /**
     *
     * @param intent
     */
    public void animStart(Intent intent){
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     *
     * @param clazz
     */
    public void animStart(Class clazz){
        Intent intent = new Intent(getActivity(), clazz);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     * init
     */
    public abstract void initialize();
}
