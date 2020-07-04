package com.example.toursathi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toursathi.R;
import com.example.toursathi.ResponseModel.singleGroupModel;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.itemviewholder> {
    List<singleGroupModel> grouplist;
    Context context;
    OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }
    public GroupListAdapter(List<singleGroupModel> grouplist, Context context) {
        this.grouplist = grouplist;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupListAdapter.itemviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.singlegrouplayout, viewGroup, false);
        return new itemviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.itemviewholder itemviewholder, final int i) {
        itemviewholder.groupname.setText(grouplist.get(i).getGroupname());

        itemviewholder.groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return grouplist.size();
    }

    public class itemviewholder extends RecyclerView.ViewHolder {
        ImageView groupimage;
        TextView groupname;

        public itemviewholder(@NonNull View itemView) {
            super(itemView);
            groupimage = itemView.findViewById(R.id.groupimage);
            groupname = itemView.findViewById(R.id.groupname);
        }
    }
}
