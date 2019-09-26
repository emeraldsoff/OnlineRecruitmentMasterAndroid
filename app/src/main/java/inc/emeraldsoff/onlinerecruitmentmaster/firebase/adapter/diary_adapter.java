package inc.emeraldsoff.onlinerecruitmentmaster.firebase.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.firebase.model.diarycard_page_gen;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_edit_page;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_show_page;

//import com.google.android.material.card.MaterialCardView;

public class diary_adapter extends FirestoreRecyclerAdapter<diarycard_page_gen, diary_adapter.diary_holder> {
    //    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:MM:SS a", Locale.US);
//    private List list;
//    private Context mcontext;

    public diary_adapter(@NonNull FirestoreRecyclerOptions<diarycard_page_gen> options) {
        super(options);
//        this.list = list;
//        this.mcontext = mcontext;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onBindViewHolder(@NonNull final diary_holder holder, final int position, @NonNull final diarycard_page_gen model) {
//        List mylist = (List) list.get(position);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        final FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        fdb.setFirestoreSettings(settings);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Context mcontext = holder.diary_no.getContext();
        final String docid, folder_name, app_userid;

        final boolean b;
//        final SharedPreferences mpref = mcontext.getSharedPreferences("User", Context.MODE_PRIVATE);
        int x = position + 1;
        holder.diary_no.setText(String.valueOf(x));
        holder.diary_date.setText(model.getT());
        holder.diary_note.setText(model.getdata());
        docid = model.getDocid();
        folder_name = model.getFolder_name();
        app_userid = mAuth.getUid();
//        DocumentReference docref = fdb.document("prospect_users" + "/" + app_userid);
//        docref.get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc != null && doc.exists()) {
//                        StringBuilder ufname = new StringBuilder();
//                        StringBuilder umname = new StringBuilder();
//                        StringBuilder ulname = new StringBuilder();
//                        StringBuilder validitydate = new StringBuilder();
//                        StringBuilder ifval = new StringBuilder();
//                        StringBuilder expire = new StringBuilder();
//                        StringBuilder install = new StringBuilder();
//                        StringBuilder uemail = new StringBuilder();
//                        StringBuilder uphone = new StringBuilder();
//                        validitydate.append(doc.get("ValidityDate"));
//                        install.append(doc.get("InstallDate"));
//                        expire.append(doc.get("ExpiryDate"));
//                        ufname.append(doc.get("FirstName"));
//                        umname.append(doc.get("MiddleName"));
//                        ulname.append(doc.get("LastName"));
//                        uemail.append(doc.get("EmailId"));
//                        uphone.append(doc.get("MobileNo"));
//                        ifval.append(doc.getBoolean("IF_VALID"));
////                        Boolean x = Boolean.parseBoolean(ifval.toString());
//                    }
//                }
//            }
//        });

        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();
        holder.diary_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_diary_show_page.class)
                        .putExtra("docid", docid)
                        .putExtra("folder_name", folder_name));
//                Toasty.info(mcontext,"FROM DIARY_ADAPTER: "+docid,4,true).show();
            }
        });

        holder.diary_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_diary_show_page.class)
                        .putExtra("docid", docid)
                        .putExtra("folder_name", folder_name));
//                Toasty.info(mcontext,"FROM DIARY_ADAPTER: "+docid,4,true).show();
            }
        });

        holder.diary_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_copy_edit_delete_share);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_copy:
                                ClipboardManager clipboard = (ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(model.getDt() + ", " + model.getT(),
                                        holder.diary_note.getText().toString().trim());
                                clipboard.setPrimaryClip(clip);
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;
                            case R.id.menu_edit:
                                if (b) {
                                    mcontext.startActivity(new Intent(mcontext, activity_diary_edit_page.class)
                                            .putExtra("docid", docid)
                                            .putExtra("folder_name", folder_name));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;
                            case R.id.menu_delete:
                                if (b) {

                                    new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                            .setMessage("Are you sure to delete this document?")
                                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String collection = "prospect" + "/" + app_userid;

                                                    DocumentReference user = fdb.collection(collection + "/" + "personal_diary" + "/" +
                                                            folder_name + "/" + "pages").document(docid);
                                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toasty.success(mcontext, "Diary page deleted From DataBase Successfully..!!",
                                                                    Toast.LENGTH_LONG, true).show();

//                                                            mcontext.startActivity(new Intent(mcontext, activity_contentdiary_pages.class)
//                                                            .putExtra("folder_name",folder_name));
//                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                                            finish();
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
//                                    Toasty.info(mcontext, "delete: " + docid, 4, true).show();
                                    return true;
                                } else {
                                    return false;
                                }
                            case R.id.menu_share:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "" + holder.diary_note.getText().toString().trim());
                                shareIntent.putExtra(Intent.EXTRA_TITLE, model.getDt() + ", " + model.getT());
                                mcontext.startActivity(Intent.createChooser(shareIntent, model.getDt() + ", " + model.getT()));
//                                Toasty.info(mcontext, "share: " + holder.diary_note.getText(), 4, true).show();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public diary_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_diary, parent, false);
        return new diary_holder(view);
    }

    class diary_holder extends RecyclerView.ViewHolder {
        TextView diary_no;
        TextView diary_date;
        //        TextView cli_dob;
        TextView diary_note;


        diary_holder(View itemView) {
            super(itemView);
            diary_no = itemView.findViewById(R.id.diary_no);
            diary_date = itemView.findViewById(R.id.diary_date);
            diary_note = itemView.findViewById(R.id.diary_note);

        }
    }
}
