package co.nexus.votingapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import co.nexus.votingapp.Helpers.GlideApp;
import co.nexus.votingapp.R;

public class AdapterGridGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<StorageReference> items = new ArrayList<>();
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, StorageReference ref, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterGridGallery(Context context, ArrayList<StorageReference> items){
        this.context = context;
        this.items = items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView;
        public MyViewHolder(View v){
            super(v);
            imgView = v.findViewById(R.id.imageViewAdminGallery);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_gallery, parent, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder hold = (MyViewHolder) holder;
        hold.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, items.get(position), position);
                }
            }
        });

        GlideApp.with(context)
                .load(items.get(position))
                .into(hold.imgView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
