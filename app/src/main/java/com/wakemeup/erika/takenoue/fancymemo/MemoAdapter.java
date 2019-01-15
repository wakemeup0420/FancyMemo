package com.wakemeup.erika.takenoue.fancymemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MemoAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Memo> mMemoList;

    public MemoAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMemoList(List<Memo> memoList) {
        mMemoList = memoList;
    }

    @Override
    public int getCount() {
        return mMemoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.memo_layout, null);
        }

        TextView textView1 = convertView.findViewById(R.id.TextView1);
        TextView textView2 = convertView.findViewById(R.id.TextView2);

        textView1.setText(mMemoList.get(position).getMemo());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (E) HH:mm", Locale.JAPANESE);
        textView2.setText(simpleDateFormat.format(mMemoList.get(position).getDate()));

        return convertView;
    }
}