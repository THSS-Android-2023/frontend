package com.example.internet.adapter.list;

import android.content.Context;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.SearchResultModel;

import java.util.List;

public class SearchResultListAdapter extends BaseListAdapter<SearchResultModel>{
    public SearchResultListAdapter(List<SearchResultModel> data, Context ctx){
        super(R.layout.model_search_result, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, SearchResultModel item){
        holder.setText(R.id.content, item.content)
                .setText(R.id.timestamp, item.timestamp);
    }
}
