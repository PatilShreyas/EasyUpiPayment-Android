package com.shreyaspatil.EasyUpiPayment.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shreyaspatil.EasyUpiPayment.R;
import com.shreyaspatil.EasyUpiPayment.ui.PaymentUiActivity;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private Context mContext;
    private List<ResolveInfo> mList;
    private Intent mIntent;

    public AppsAdapter(Context mContext, List<ResolveInfo> mList, Intent mIntent) {
        this.mContext = mContext;
        this.mList = mList;
        this.mIntent = mIntent;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        final ResolveInfo info = mList.get(position);
        String name = String.valueOf(info.loadLabel(mContext.getPackageManager()));
        final Drawable icon = info.loadIcon(mContext.getPackageManager());
        holder.bind(name, icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mIntent;
                intent.setPackage(info.activityInfo.packageName);
                ((Activity)mContext).startActivityForResult(intent, PaymentUiActivity.PAYMENT_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //ViewHolder class for RecyclerView
    static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView nameView;

        AppViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.appIconView);
            nameView = itemView.findViewById(R.id.appNameView);
        }

        void bind(String name, Drawable icon) {
            nameView.setText(name);
           iconView.setImageDrawable(icon);
        }
    }
}
