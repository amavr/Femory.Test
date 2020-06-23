package com.amavr.femory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amavr.femory.models.ItemInfo;
import com.amavr.tools.XMem;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    static final String TAG = "XDBG.ADP-ITEMS";

    Context context;
    List<ItemInfo> items;

    public ItemsAdapter(Context context, List<ItemInfo> items){
        this.context = context;
        this.items = items;
//        Log.d(TAG, String.format("ItemsAdapter created with %s items", items.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View view = inf.inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view, context);
//        Log.d(TAG, "onCreateViewHolder");
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder");
        final ItemInfo item = items.get(position);
        holder.bindItem(item, position);
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount");
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemInfo item;
        TextView tvName;
        CheckBox cbDone;

        Context context;
        int position = 0;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            tvName = itemView.findViewById(R.id.txtName);
            cbDone = itemView.findViewById(R.id.cbDone);
            cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setDone(isChecked);
                }
            });
        }

        public void bindItem(final ItemInfo item, int position) {
            this.item = item;
            this.position = position;

            tvName.setText(item.getName());
            cbDone.setChecked(item.isDone());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "clicked");
        }
    }

}
