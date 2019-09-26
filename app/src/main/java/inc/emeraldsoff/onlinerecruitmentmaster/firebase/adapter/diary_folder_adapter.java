/*package inc.emeraldsoff.onlinerecruitmentmaster.firebase.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.firebase.model.diarycard_folder_gen;

public class diary_folder_adapter extends FirestoreRecyclerAdapter<diarycard_folder_gen, diary_folder_adapter.diary_holder> {
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);

    public diary_folder_adapter(@NonNull FirestoreRecyclerOptions<diarycard_folder_gen> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final diary_holder holder, int position, @NonNull final diarycard_folder_gen model) {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        final FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        fdb.setFirestoreSettings(settings);
        final Context mcontext = holder.diary_no.getContext();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final boolean b;
        final String folder_name, app_userid;
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();

        int x = position + 1;
        holder.diary_no.setText(String.valueOf(x));
        holder.diary_date.setText(fullFormat_onlydate.format(model.getTimestamp().toDate()));

        folder_name = model.getFolder_name();
        app_userid = mAuth.getUid();

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_delete);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                if (b || !b) {
                                    new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                            .setMessage("Are you sure to delete all documents of " +
                                                    fullFormat_onlydate.format(model.getTimestamp().toDate()) + "?")
                                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String collection = "prospect" + "/" + app_userid;
//                                                    CollectionReference col = fdb.collection(collection + "/" + "personal_diary" +
//                                                            "/"+ folder_name +"/"+ "pages");
                                                    DocumentReference user = fdb.collection(collection + "/" + "personal_diary").document(folder_name);
                                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toasty.success(mcontext, "All documents of " +
                                                                            fullFormat_onlydate.format(model.getTimestamp().toDate()) +
                                                                            " has been deleted from your diary..!!",
                                                                    Toast.LENGTH_LONG, true).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toasty.error(mcontext, "Error deleting document..!!",
                                                                    Toast.LENGTH_LONG, true).show();
                                                        }
                                                    });
                                                }
                                            }).setNegativeButton("No", null).show();
                                    return true;
                                } else {
                                    return false;
                                }
                        }
                        return false;
                    }
                });
                return false;
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_contentdiary_pages.class)
                        .putExtra("folder_name", folder_name));
            }
        });
    }

    @NonNull
    @Override
    public diary_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_folder_diary, parent, false);
        return new diary_holder(view);
    }

    class diary_holder extends RecyclerView.ViewHolder {
        TextView diary_no;
        TextView diary_date;
        CardView card;

        diary_holder(View itemView) {
            super(itemView);
            diary_no = itemView.findViewById(R.id.diary_no);
            diary_date = itemView.findViewById(R.id.diary_date);
            card = itemView.findViewById(R.id.folder_diary_card);
        }
    }
}
*/