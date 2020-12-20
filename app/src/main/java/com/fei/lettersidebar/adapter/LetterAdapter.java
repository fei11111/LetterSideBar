package com.fei.lettersidebar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fei.lettersidebar.R;
import com.fei.lettersidebar.mode.SortModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: LetterAdapter
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-18 21:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-18 21:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.ViewHolder> {

    private Context mContext;
    private List<SortModel> sortModelList;//已经排序了

    public LetterAdapter(Context mContext, List<SortModel> sortModelList) {
        this.mContext = mContext;
        this.sortModelList = sortModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getFirstLetterPosition(sortModelList.get(position).getSortLetter().charAt(0)) == position) {
            //当前字母是第一次出现
            holder.tvSortLetter.setVisibility(View.VISIBLE);
            holder.tvSortLetter.setText(sortModelList.get(position).getSortLetter());
        } else {
            holder.tvSortLetter.setVisibility(View.GONE);
        }
        holder.tvName.setText(sortModelList.get(position).getName());
    }

    /**
     * 通过ASCII码 获取首字母的位置
     */
    public int getFirstLetterPosition(int letter) {
        for (int i = 0; i < sortModelList.size(); i++) {
            if (sortModelList.get(i).getSortLetter().toUpperCase().charAt(0) == letter) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 当前位置获取首字母
     */
    public String getFirstLetter(int position) {
        if (position < 0) {
            return "A";
        } else if (position >= sortModelList.size()) {
            return "#";
        }
        return sortModelList.get(position).getSortLetter();
    }


    @Override
    public int getItemCount() {
        return sortModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSortLetter;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSortLetter = this.itemView.findViewById(R.id.tv_sort_letter);
            tvName = this.itemView.findViewById(R.id.tv_name);
        }
    }

}
