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

public class mainbday_adapter extends FirestoreRecyclerAdapter<clicard_gen, mainbday_adapter.clibday_holder> {

    private onItemClickListener listener;
    private onLongItemClickListener listener2;

    public mainbday_adapter(@NonNull FirestoreRecyclerOptions<clicard_gen> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull clibday_holder holder, int position, @NonNull final clicard_gen model) {
        holder.cli_name.setText(model.getClient_name());
        holder.cli_birthday.setText(model.getBday_dd());

    }

    @NonNull
    @Override
    public clibday_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mainbday, parent, false);
        return new clibday_holder(v);
    }

    public void setOnLongItemClickListener(onLongItemClickListener listener2) {
        this.listener2 = listener2;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onLongItemClickListener {
        void onLongItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    //    public void deleteItem(int position) {
//        getSnapshots().getSnapshot(position).getReference().delete();
//    }
    class clibday_holder extends RecyclerView.ViewHolder {
        TextView cli_name;
        //        TextView cli_dob;
        TextView cli_birthday;

        clibday_holder(View itemView) {
            super(itemView);
            cli_name = itemView.findViewById(R.id.cliday_name);
            cli_birthday = itemView.findViewById(R.id.clibday_birthday);

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

