package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.diary_manager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_edit_page;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.diary.activity_diary_show_page;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class diary_adapter extends RecyclerView.Adapter<diary_adapter.diary_viewholder> {

    private Context context;
    private Cursor cursor;
    private Cursor doc_cursor;
    private SimpleDateFormat fullFormat_time = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE', 'hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlytime = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    private SimpleDateFormat fullFormat_onlydate = new SimpleDateFormat("dd MMMM yyyy', 'EEEE", Locale.US);
    private SimpleDateFormat folderdoc = new SimpleDateFormat("YYYY-MMMM-dd', 'EEEE", Locale.US);
    private SimpleDateFormat foldername = new SimpleDateFormat("YYYY-MMMM-dd-EEEE", Locale.US);
    private SimpleDateFormat fullFormat_time_doc = new SimpleDateFormat("YYYY-MMMM-dd-EEEE-hh-mm-ss-a", Locale.US);
    private SimpleDateFormat fullFormat_prime_onlydate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

    int i = 0;

    public diary_adapter(Context mcontext, Cursor mcursor) {
        context = mcontext;
        cursor = mcursor;
        doc_cursor = mcursor;
    }

    @NonNull
    @Override
    public diary_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_diary, parent, false);
        return new diary_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final diary_viewholder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        final Context mcontext = holder.diary_holder.getContext();
//        final String app_userid;

        final boolean b;
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();

        final String content = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.content));
        final String created_at = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.created_at));
        final String created_at_date=cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.created_date));
        final String created_at_time=cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.created_time));

        String purpose = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.purpose));
        final String docid = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary._ID));
//        final String gender = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.diary.gender));



        holder.diary_onlytime.setText(created_at_time);
        holder.diary_note.setText(content);
        holder.diary_pupose.setText(purpose);

        if(position == 0) {
            i = 1;
            holder.diary_onlydate_section.setVisibility(View.VISIBLE);
            holder.diary_onlydate.setText(created_at_date);
        }
        else {
            int positionx = position-1;
            doc_cursor=cursor;
            doc_cursor.moveToPosition(positionx);
            String xcreated_at_date=doc_cursor.getString(doc_cursor.getColumnIndex(sqlite_basecolumns.diary.created_date));
            if(xcreated_at_date.equals(created_at_date)) {
                i=i+1;
                holder.diary_onlydate_section.setVisibility(View.GONE);
            } else {
                i=1;
                holder.diary_onlydate_section.setVisibility(View.VISIBLE);
                holder.diary_onlydate.setText(created_at_date);
            }
        }
        holder.diary_no.setText(String.valueOf(i));
        holder.diary_onlytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_open_copy_edit_delete_share);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                //TODO check everything
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_show:
                                //TODO change redirect class
                                mcontext.startActivity(new Intent(mcontext, activity_diary_show_page.class)
//                                        .putExtras()
                                        .putExtra("docid", docid));
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;

                            case R.id.menu_copy:
                                ClipboardManager clipboard = (ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(created_at_date + ", " + created_at_time,
                                        holder.diary_note.getText().toString().trim());
                                clipboard.setPrimaryClip(clip);
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;

                            case R.id.menu_edit:
                                if (b) {
                                    //TODO change redirect class
                                    mcontext.startActivity(new Intent(mcontext, activity_diary_edit_page.class)
                                            .putExtra("docid", docid));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;

                            case R.id.menu_delete:
                                sqlite_helper sqliteHelper = new sqlite_helper(mcontext);
                                final SQLiteDatabase sqlite = sqliteHelper.getWritableDatabase();
                                //TODO change db action
                                final Cursor x = sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sqlite_basecolumns.diary.created_at + " DESC"
                                );
                                new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                        .setMessage("Are you sure to delete this document?")
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //TODO change db action
                                                sqlite.delete(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                                                        sqlite_basecolumns.diary._ID + " = '" + docid + "'",
                                                        null
                                                );
                                                swapcursor(x);
                                            }
                                        }).setNegativeButton("No", null).show();
                                return true;

                            case R.id.menu_share:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "" + holder.diary_note.getText().toString().trim());
                                shareIntent.putExtra(Intent.EXTRA_TITLE, created_at_date + ", " + created_at_time);
                                mcontext.startActivity(Intent.createChooser(shareIntent, created_at_date + ", " + created_at_time));
//                                Toasty.info(mcontext, "share: " + holder.diary_note.getText(), 4, true).show();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });

        holder.diary_data_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change redirect class
                /*
                mcontext.startActivity(new Intent(mcontext, activity_diary_show_page.class)
                        .putExtra("docid", docid));
                */
            }
        });

        holder.diary_data_holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_open_copy_edit_delete_share);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                //TODO check everything
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_show:
                                //TODO change redirect class
                                mcontext.startActivity(new Intent(mcontext, activity_diary_show_page.class)
//                                        .putExtras()
                                        .putExtra("docid", docid));
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;

                            case R.id.menu_copy:
                                ClipboardManager clipboard = (ClipboardManager) mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(created_at_date + ", " + created_at_time,
                                        holder.diary_note.getText().toString().trim());
                                clipboard.setPrimaryClip(clip);
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;

                            case R.id.menu_edit:
                                if (b) {
                                    //TODO change redirect class
                                    mcontext.startActivity(new Intent(mcontext, activity_diary_edit_page.class)
                                            .putExtra("docid", docid));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;

                            case R.id.menu_delete:
                                sqlite_helper sqliteHelper = new sqlite_helper(mcontext);
                                final SQLiteDatabase sqlite = sqliteHelper.getWritableDatabase();
                                //TODO change db action
                                final Cursor x = sqlite.query(sqlite_basecolumns.diary.DIARY_TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sqlite_basecolumns.diary.created_at + " DESC"
                                );
                                new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                        .setMessage("Are you sure to delete this document?")
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //TODO change db action
                                                sqlite.delete(sqlite_basecolumns.diary.DIARY_TABLE_NAME + "",
                                                        sqlite_basecolumns.diary._ID + " = '" + docid + "'",
                                                        null
                                                );
                                                swapcursor(x);
                                            }
                                        }).setNegativeButton("No", null).show();
                                return true;

                            case R.id.menu_share:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "" + holder.diary_note.getText().toString().trim());
                                shareIntent.putExtra(Intent.EXTRA_TITLE, created_at_date + ", " + created_at_time);
                                mcontext.startActivity(Intent.createChooser(shareIntent, created_at_date + ", " + created_at_time));
//                                Toasty.info(mcontext, "share: " + holder.diary_note.getText(), 4, true).show();
                                return true;
                        }
                        return false;
                    }
                });

                return false;
            }
        });

    }

    private void askcallpermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_CALL_PHONE);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

//    public void asksmspermission() {
//        if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context,
//                    new String[]{Manifest.permission.SEND_SMS},
//                    MY_PERMISSIONS_REQUEST_SEND_SMS);
//        }
//    }

    public void swapcursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    class diary_viewholder extends RecyclerView.ViewHolder {
        TextView diary_no;
        TextView diary_onlytime;
        TextView diary_onlydate;
        //        TextView cli_dob;
        TextView diary_note;
        TextView diary_pupose;
        CardView diary_holder;
        LinearLayout diary_data_holder;
        CardView diary_onlydate_section;

        diary_viewholder(@NonNull View itemView) {
            super(itemView);
            diary_data_holder = itemView.findViewById(R.id.linearLayout);
            diary_holder = itemView.findViewById(R.id.diary_holder);
            diary_pupose = itemView.findViewById(R.id.diary_purpose);
            diary_no = itemView.findViewById(R.id.diary_no);
            diary_onlytime = itemView.findViewById(R.id.diary_onlytime);
            diary_onlydate = itemView.findViewById(R.id.diary_onlydate);
            diary_note = itemView.findViewById(R.id.diary_note);
            diary_onlydate_section = itemView.findViewById(R.id.diary_onlydate_section);


        }
    }
}
