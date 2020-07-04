package com.example.toursathi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toursathi.Model.UploadImgModel;
import com.example.toursathi.R;

import java.util.List;

public class albumadapter extends RecyclerView.Adapter<albumadapter.albumviewholder> {
    List<UploadImgModel> uploadedimglist;
    Context context;
    OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }
    public albumadapter(List<UploadImgModel> uploadedimglist, Context context) {
        this.uploadedimglist = uploadedimglist;
        this.context = context;
    }

    @NonNull
    @Override
    public albumadapter.albumviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gridviewbasiclayout, viewGroup, false);
        return new albumviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull albumadapter.albumviewholder albumviewholder, final int i) {
        Glide.with(context)
                .load(uploadedimglist.get(i).getImagepath())
                .centerCrop()
                .into(albumviewholder.uploadedimage);



        albumviewholder.uploadedimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return uploadedimglist.size();
    }

    public class albumviewholder extends RecyclerView.ViewHolder {
        //TextView moviename;
        ImageView uploadedimage;

        public albumviewholder(@NonNull View itemView) {
            super(itemView);
            uploadedimage = itemView.findViewById(R.id.uploadedimage);
        }
    }
}
