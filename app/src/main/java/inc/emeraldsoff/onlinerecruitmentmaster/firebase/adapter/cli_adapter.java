package inc.emeraldsoff.onlinerecruitmentmaster.firebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.firebase.model.clicard_gen;

//import com.google.android.material.card.MaterialCardView;

public class cli_adapter extends FirestoreRecyclerAdapter<clicard_gen, cli_adapter.cli_holder> {

    private onItemClickListener listener;
    private onLongItemClickListener listener2;

    public cli_adapter(@NonNull FirestoreRecyclerOptions<clicard_gen> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull cli_holder holder, int position, @NonNull final clicard_gen model) {
        int i = position + 1;
        holder.cli_id.setText(String.valueOf(i));
        holder.cli_name.setText(model.getClient_name());
        holder.cli_phno.setText(model.getMobile_no());
        if (model.getNote() == null || model.getNote().isEmpty() || model.getNote().equals("")) {
            holder.cli_note.setVisibility(View.GONE);
        } else {
            holder.cli_note.setText(model.getNote());
        }

//        holder.cli_birthday.setText(model.getBirthday());
    }

    @NonNull
    @Override
    public cli_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cid, parent, false);
        return new cli_holder(view);
    }

    public void setOnLongItemClickListener(onLongItemClickListener listener2) {
        this.listener2 = listener2;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    //    public void deleteItem(int position) {
//        getSnapshots().getSnapshot(position).getReference().delete();
//    }
    public interface onLongItemClickListener {
        void onLongItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    //    public void deleteItem(int position) {
//        getSnapshots().getSnapshot(position).getReference().delete();
//    }
    class cli_holder extends RecyclerView.ViewHolder {
        TextView cli_name;
        TextView cli_phno;
        TextView cli_note;
        TextView cli_id;

        cli_holder(View itemView) {
            super(itemView);
            cli_name = itemView.findViewById(R.id.cli_name);
            cli_phno = itemView.findViewById(R.id.cli_phno);
            cli_note = itemView.findViewById(R.id.cli_note);
            cli_id = itemView.findViewById(R.id.id_no);
//            cli_dob = itemView.findViewById(R.id.cli_dob);
//            cli_birthday = itemView.findViewById(R.id.cli_birthday);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View vi) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener2 != null) {
                        listener2.onLongItemClick(getSnapshots().getSnapshot(position), position);
                    }
                    return true;
                }
            });

        }
    }
}
