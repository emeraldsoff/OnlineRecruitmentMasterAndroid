package inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_adapters.contact_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import inc.emeraldsoff.onlinerecruitmentmaster.R;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_basecolumns;
import inc.emeraldsoff.onlinerecruitmentmaster.sqlite_manager.sqlite_commands;

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class birthday_adapter extends RecyclerView.Adapter<birthday_adapter.mainbday_viewholder> {

    private Context context;
    private Cursor cursor;


    public birthday_adapter(Context mcontext, Cursor mcursor) {
        context = mcontext;
        cursor = mcursor;
    }

    @NonNull
    @Override
    public mainbday_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_mainbday, parent, false);
        return new mainbday_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainbday_viewholder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        final Context mcontext = holder.bday_card.getContext();
        final boolean b, c;
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();

        final String name = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.client_name));
        final String birthday = cursor.getString(sqlite_commands.contacts_bday_dd);
        final String mobile = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.mobile_no));
        final String g = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.gender));
        Date currentdate = null;
        Date birthday_date = null;
        SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date now = new Date();
        try {
            currentdate = day_monFormat.parse(day_monFormat.format(now));
            birthday_date = day_monFormat.parse(day_monFormat.format(fullFormat.parse(birthday)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c = birthday_date == currentdate;

        try {
            holder.name.setText(name);
            holder.birthdy.setText(birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.bday_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupadapter(mcontext, v, mobile, name, g, c);
            }
        });

        holder.bday_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setupadapter(mcontext, v, mobile, name, g, c);
                return false;
            }
        });

    }

    private void setupadapter(final Context mcontext, View v, final String mobile,
                              final String name, final String g, final boolean c) {
        final PopupMenu menu = new PopupMenu(mcontext, v);
        menu.inflate(R.menu.menu_call_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            menu.setGravity(Gravity.END);
        }
        menu.show();
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_call:
                        if (ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            askcallpermission();

                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mobile, null));
                            mcontext.startActivity(intent);
                        }
                        return true;
                    case R.id.menu_message:
                        String gender = "";
                        if (g != null) {
                            gender = g;
                        }
                        String message;
                        if (c) {
                            switch (gender) {
                                case "Female":
                                    message = "Mrs./Miss " + name + " On your special day, I wish " +
                                            "you good health, happiness, and a fantastic birthday..!!";
                                    break;
                                case "Male":
                                    message = "Mr. " + name + " On your special day, I wish " +
                                            "you good health, happiness, and a fantastic birthday..!!";
                                    break;
                                default:
                                    message = "Mr./Mrs./Miss " + name + " On your special day, I wish " +
                                            "you good health, happiness, and a fantastic birthday..!!";
                                    break;
                            }
                        } else {
                            message = "";
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                        intent.putExtra("sms_body", message + "");
                        mcontext.startActivity(intent);
                        return true;
                }
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

    public void swapcursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    class mainbday_viewholder extends RecyclerView.ViewHolder {
        CardView bday_card;
        TextView name;
        TextView birthdy;

        mainbday_viewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cliday_name);
            birthdy = itemView.findViewById(R.id.clibday_birthday);
            bday_card = itemView.findViewById(R.id.bday_card);
        }
    }

}
