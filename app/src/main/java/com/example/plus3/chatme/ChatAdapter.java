package com.example.plus3.chatme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Plus 3 on 07-02-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Chat> mDataSet;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;


    public ChatAdapter(List<Chat> dataset, String id){
        this.mDataSet = dataset;
        this.mId = id;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.msgr);
        }
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(viewType == CHAT_RIGHT){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.right,parent,false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left,parent,false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        Chat chat = mDataSet.get(position);
        holder.mTextView.setText(chat.getBody());
    }

    @Override
    public int getItemViewType(int position) {
        if(mDataSet.get(position).getMessageBy().equals(mId))
            return CHAT_RIGHT;
        return CHAT_LEFT;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

