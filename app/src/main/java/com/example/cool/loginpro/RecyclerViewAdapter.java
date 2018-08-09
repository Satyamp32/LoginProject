package com.example.cool.loginpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<User> list;
    private Context context;
    User user;

    RecyclerViewAdapter(Context context,List<User> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        User id=list.get(position);
        holder.mFirstName.setText(id.getmFirstName());
        holder.mLastName.setText(id.getmLastName());
        holder.mEmail.setText(id.getmEmailId());
        holder.mPassword.setText(id.getmPassword());
        holder.mMobileNo.setText(id.getmMobileNo());
        holder.mDob.setText(id.getmDob());
        holder.mId.setText((String.valueOf(id.getId())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mFirstName,mLastName,mEmail,mPassword,mMobileNo,mDob,mId;
        public MyViewHolder(View itemView) {
            super(itemView);
            mFirstName=(itemView.findViewById(R.id.textViewFirstName));
            mLastName=itemView.findViewById(R.id.textViewLastName);
            mEmail=itemView.findViewById(R.id.textViewEmailId);
            mPassword=itemView.findViewById(R.id.textViewPassword);
            mMobileNo=itemView.findViewById(R.id.textViewMobileNo);
            mDob=itemView.findViewById(R.id.textViewDob);
            mId=itemView.findViewById(R.id.textViewId);
        }
    }
}
