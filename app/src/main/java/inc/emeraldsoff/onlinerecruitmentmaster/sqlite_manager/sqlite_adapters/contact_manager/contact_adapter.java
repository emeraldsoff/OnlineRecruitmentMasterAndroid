package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_helper;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_editpeople;
import inc.emeraldsoff.onlinerecruitmentmaster.ui_data.people.activity_showpeopledetails;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class contact_adapter extends RecyclerView.Adapter<contact_adapter.contacts_viewholder> {

    private Context context;
    private Cursor cursor;

    public contact_adapter(Context mcontext, Cursor mcursor) {
        context = mcontext;
        cursor = mcursor;
    }

    @NonNull
    @Override
    public contacts_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_cid, parent, false);
        return new contacts_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contacts_viewholder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        final Context mcontext = holder.cli_card_holder.getContext();
//        final String app_userid;

        final boolean b;
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();

        final String name = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.client_name));
        final String mobile = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.mobile_no));
        String note = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.note));
        final String docid = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts._ID));
//        final String gender = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.gender));

        int i = position + 1;
        holder.cli_id.setText(String.valueOf(i));
        holder.cli_name.setText(name);
        holder.cli_phno.setText(mobile);
        if (note == null || note.isEmpty()) {
            holder.cli_note.setVisibility(View.GONE);
        } else {
            holder.cli_note.setText(note);
        }

        holder.cli_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
                        .putExtra("docid", docid));
            }
        });

        holder.cli_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_open_edit_delete_call_message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_show:
                                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
//                                        .putExtras()
                                        .putExtra("docid", docid));
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;
                            case R.id.menu_edit:
                                if (b) {
                                    mcontext.startActivity(new Intent(mcontext, activity_editpeople.class)
                                            .putExtra("docid", docid));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;
                            case R.id.menu_delete:
                                sqlite_helper sqliteHelper = new sqlite_helper(mcontext);
                                final SQLiteDatabase sqlite = sqliteHelper.getWritableDatabase();
                                final Cursor x = sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sqlite_basecolumns.contacts.client_name + " ASC"
                                );
                                new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                        .setMessage("Are you sure to delete this document?")
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sqlite.delete(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                                                        sqlite_basecolumns.contacts._ID + " = '" + docid + "'",
                                                        null
                                                );
                                                swapcursor(x);
                                            }
                                        }).setNegativeButton("No", null).show();


                                return true;

                            case R.id.menu_call:
                                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    askcallpermission();

                                } else {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobile, null));
                                    mcontext.startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                                return true;
                            case R.id.menu_message:
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                                intent.putExtra("sms_body", "");
                                mcontext.startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });

                return false;
            }
        });

        holder.cli_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
                        .putExtra("docid", docid));
            }
        });

        holder.cli_note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_open_edit_delete_call_message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_show:
                                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
//                                        .putExtras()
                                        .putExtra("docid", docid));
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;
                            case R.id.menu_edit:
                                if (b) {
                                    mcontext.startActivity(new Intent(mcontext, activity_editpeople.class)
                                            .putExtra("docid", docid));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;
                            case R.id.menu_delete:
                                sqlite_helper sqliteHelper = new sqlite_helper(mcontext);
                                final SQLiteDatabase sqlite = sqliteHelper.getWritableDatabase();
                                final Cursor x = sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sqlite_basecolumns.contacts.client_name + " ASC"
                                );
                                new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                        .setMessage("Are you sure to delete this document?")
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sqlite.delete(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                                                        sqlite_basecolumns.contacts._ID + " = '" + docid + "'",
                                                        null
                                                );
                                                swapcursor(x);
                                            }
                                        }).setNegativeButton("No", null).show();


                                return true;

                            case R.id.menu_call:
                                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    askcallpermission();

                                } else {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobile, null));
                                    mcontext.startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                                return true;
                            case R.id.menu_message:
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                                intent.putExtra("sms_body", "");
                                mcontext.startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });

                return false;
            }
        });

        holder.cli_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
                        .putExtra("docid", docid));
            }
        });

        holder.cli_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mcontext, v);
                menu.inflate(R.menu.menu_open_edit_delete_call_message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    menu.setGravity(Gravity.END);
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_show:
                                mcontext.startActivity(new Intent(mcontext, activity_showpeopledetails.class)
//                                        .putExtras()
                                        .putExtra("docid", docid));
//                                Toasty.info(mcontext, "copy: " + model.getDocid() + " --- " + app_userid, 4, true).show();
                                return true;
                            case R.id.menu_edit:
                                if (b) {
                                    mcontext.startActivity(new Intent(mcontext, activity_editpeople.class)
                                            .putExtra("docid", docid));
//                                    Toasty.info(mcontext, "edit: " + docid, 4, true).show();
                                    return true;
                                } else
                                    return false;
                            case R.id.menu_delete:
                                sqlite_helper sqliteHelper = new sqlite_helper(mcontext);
                                final SQLiteDatabase sqlite = sqliteHelper.getWritableDatabase();
                                final Cursor x = sqlite.query(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sqlite_basecolumns.contacts.client_name + " ASC"
                                );
                                new AlertDialog.Builder(mcontext).setIcon(R.drawable.ic_warning_pink_24dp).setTitle("Delete")
                                        .setMessage("Are you sure to delete this document?")
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sqlite.delete(sqlite_basecolumns.contacts.CONTACTS_TABLE_NAME + "",
                                                        sqlite_basecolumns.contacts._ID + " = '" + docid + "'",
                                                        null
                                                );
                                                swapcursor(x);
                                            }
                                        }).setNegativeButton("No", null).show();


                                return true;
                            case R.id.menu_call:
                                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    askcallpermission();

                                } else {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobile, null));
                                    mcontext.startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                                return true;
                            case R.id.menu_message:
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                                intent.putExtra("sms_body", "");
                                mcontext.startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });

                return false;
            }
        });

        holder.cli_phno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    askcallpermission();

                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobile, null));
                    mcontext.startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        holder.cli_phno.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                intent.putExtra("sms_body", "");
                mcontext.startActivity(intent);

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

    class contacts_viewholder extends RecyclerView.ViewHolder {
        TextView cli_name;
        TextView cli_phno;
        TextView cli_note;
        TextView cli_id;
        CardView cli_card_holder;

        contacts_viewholder(@NonNull View itemView) {
            super(itemView);
            cli_card_holder = itemView.findViewById(R.id.cid_card_holder);
            cli_name = itemView.findViewById(R.id.cli_name);
            cli_phno = itemView.findViewById(R.id.cli_phno);
            cli_note = itemView.findViewById(R.id.cli_note);
            cli_id = itemView.findViewById(R.id.id_no);


        }
    }
}
