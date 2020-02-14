package co.nexus.votingapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.nexus.votingapp.R;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private final String TAG = "DepartmentAdapter";
    private ArrayList<String> departments;
    private Context context;

    public DepartmentAdapter(Context context, ArrayList<String> departments) {
        this.departments = departments;
        this.context = context;
    }

    @NonNull
    @Override
    public DepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_election_department, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentAdapter.ViewHolder holder, int position) {
        String dept = departments.get(position);
        holder.textViewDeptTitle.setText(dept);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cardview clicked");
                context.startActivity(new Intent(context, AdminResultActivity.class).putExtra("department", dept));
            }
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDeptTitle;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeptTitle = itemView.findViewById(R.id.textViewDeptTitle);
            cardView = itemView.findViewById(R.id.lyt_parent_cardview);
        }
    }
}

