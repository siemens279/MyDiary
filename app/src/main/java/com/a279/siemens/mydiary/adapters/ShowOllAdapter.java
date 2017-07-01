package com.a279.siemens.mydiary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.R;
import java.util.ArrayList;

public class ShowOllAdapter extends RecyclerView.Adapter<ShowOllAdapter.ViewHolder> {

    public ArrayList<Diar> diarList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTema, tvText, tvDate;
        public ViewHolder(View v) {
            super(v);
            tvTema = (TextView) v.findViewById(R.id.textViewTemaItem);
            tvText = (TextView) v.findViewById(R.id.textViewTextItem);
            tvDate = (TextView) v.findViewById(R.id.textViewDateItem);
        }
    }

    public ShowOllAdapter(ArrayList<Diar> d) {
        diarList = new ArrayList<>(d);
    }
    public ShowOllAdapter(ArrayList<Diar> itemsData, OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        diarList = new ArrayList<>(itemsData);
    }

    @Override
    public ShowOllAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_oll_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTema.setText(diarList.get(position).tema);
        holder.tvText.setText(diarList.get(position).text);
        holder.tvDate.setText(diarList.get(position).date);
//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//            }
//        });

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return diarList.size();
    }

    public void refreshData(ArrayList<Diar> d){
        //Чистим коллекцию с данными
        diarList.clear();
        //наполняем измененными данными
        diarList = new ArrayList<>(d);
        //передергиваем адаптер
        notifyDataSetChanged();
    }


}
