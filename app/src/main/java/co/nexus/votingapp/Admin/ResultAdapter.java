package co.nexus.votingapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.R;

public class ResultAdapter extends ArrayAdapter<Candidate> {
    private Context context;
    private ArrayList<Candidate> candidates;

    public ResultAdapter(Context context, ArrayList<Candidate> candidates){
        super(context, 0, candidates);
        this.context = context;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.item_result_candidate,parent,false);

        Candidate  candidate = candidates.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.textViewResName);
        name.setText(candidate.getName());

        TextView party = (TextView) listItem.findViewById(R.id.textViewResDept);
        party.setText(candidate.getDepartment());

        TextView count = (TextView) listItem.findViewById(R.id.textViewResVote);
        count.setText("Vote Count : "+candidate.getVoteCount());

        return listItem;
    }
}
