package com.boredream.bdchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.boredream.bdchat.R;
import com.boredream.bdcodehelper.entity.User;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NEW_FRIEND = 1;

    private Context context;
    private List<User> datas;
    private int type = TYPE_NORMAL;

    public ContactAdapter(Context context, List<User> datas) {
        this(context, datas, TYPE_NORMAL);
    }

    public ContactAdapter(Context context, List<User> datas, int type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends UserViewHolder {
        public Button btn_apply;

        public ViewHolder(final View itemView) {
            super(itemView);
            btn_apply = (Button) itemView.findViewById(R.id.btn_apply);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User data = datas.get(position);
        holder.bindData(data);

        if(type == TYPE_NEW_FRIEND) {
            holder.btn_apply.setVisibility(View.VISIBLE);
            holder.btn_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onContactCtrlListener != null) {
                        onContactCtrlListener.apply(data);
                    }
                }
            });
        }
    }

    private OnContactCtrlListener onContactCtrlListener;

    public void setOnContactCtrlListener(OnContactCtrlListener onContactCtrlListener) {
        this.onContactCtrlListener = onContactCtrlListener;
    }

    public interface OnContactCtrlListener {
        void apply(User user);
    }
}
