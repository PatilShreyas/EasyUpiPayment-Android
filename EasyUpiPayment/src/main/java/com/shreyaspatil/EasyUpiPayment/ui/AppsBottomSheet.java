package com.shreyaspatil.EasyUpiPayment.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shreyaspatil.EasyUpiPayment.R;
import com.shreyaspatil.EasyUpiPayment.ui.adapter.AppsAdapter;

import java.util.List;

public class AppsBottomSheet extends BottomSheetDialogFragment {
    private final View.OnClickListener mListener;
    private RecyclerView mRecyclerView;
    private AppsAdapter mAdapter;
    private List<ResolveInfo> mList;
    private Intent mIntent;

    AppsBottomSheet(List<ResolveInfo> mList, Intent mIntent, View.OnClickListener mListener) {
        this.mList = mList;
        this.mIntent = mIntent;
        this.mListener = mListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apps_bottomsheet, container, false);
        setCancelable(false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        view.findViewById(R.id.cancelPaymentButton).setOnClickListener(mListener);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new AppsAdapter(getActivity(), mList, mIntent);
        mRecyclerView.setAdapter(mAdapter);


    }
}