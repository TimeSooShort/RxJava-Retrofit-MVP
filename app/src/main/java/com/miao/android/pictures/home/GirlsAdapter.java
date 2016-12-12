package com.miao.android.pictures.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.miao.android.pictures.bean.GirlsBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public class GirlsAdapter extends RecyclerArrayAdapter<GirlsBean.ResultsBean> {

    private OnMyItemClickListener mListener;

    public GirlsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GirlsViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onItemClick(position, holder);
                }
            }
        });
    }

    public interface OnMyItemClickListener{
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        mListener = listener;
    }
}
