package com.example.cool.loginpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MobileNoRecyclerViewAdapter extends RecyclerView.Adapter<MobileNoRecyclerViewAdapter.MobileViewHolder> {

    private Context mCtx;
    private List<User> usersList;

    public MobileNoRecyclerViewAdapter(Context mCtx, List<User> usersList){
        this.mCtx=mCtx;
        this.usersList=usersList;
    }

    @NonNull
    @Override
    public MobileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_user_mobile, null);
        return new MobileViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MobileViewHolder holder, int position) {

        User user = usersList.get(position);
        holder.mMobileNo.setText(user.getmMobileNo());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
        //return 0;
    }

    class MobileViewHolder extends RecyclerView.ViewHolder {

        TextView mMobileNo;
        public MobileViewHolder(View itemView) {
            super(itemView);
            mMobileNo=(itemView.findViewById(R.id.user_mobile_no));
        }
    }
}
