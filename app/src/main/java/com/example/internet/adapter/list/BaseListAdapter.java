package com.example.internet.adapter.list;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public abstract class BaseListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    Context m_ctx;
    public BaseListAdapter(int itemResId ,List<T> data, Context ctx) {
        super(itemResId, data);
        m_ctx = ctx;
    }

    protected abstract void convert(BaseViewHolder holder, T item);

    public void setManager(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(m_ctx);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

}
