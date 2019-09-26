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

import static inc.emeraldsoff.onlinerecruitmentmaster.Constants.MY_PERMISSIONS_CALL_PHONE;

public class anniversary_adapter extends RecyclerView.Adapter<anniversary_adapter.mainanni_viewholder> {

    private Context context;
    private Cursor cursor;

    public anniversary_adapter(Context mcontext, Cursor mcursor) {
        context = mcontext;
        cursor = mcursor;
    }

    @NonNull
    @Override
    public mainanni_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_mainanni, parent, false);
        return new mainanni_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainanni_viewholder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        final Context mcontext = holder.mainanni_card.getContext();

        final boolean b, c;
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        b = netInfo != null && netInfo.isConnectedOrConnecting();

        final String name = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.client_name));
        final String anniversary = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.anni_dd));
        final String mobile = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.mobile_no));
        final String g = cursor.getString(cursor.getColumnIndex(sqlite_basecolumns.contacts.gender));
        Date currentdate = null;
        Date anniversary_date = null;
        SimpleDateFormat day_monFormat = new SimpleDateFormat("dd-MM", Locale.US);
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date now = new Date();
        try {
            currentdate = day_monFormat.parse(day_monFormat.format(now));
            anniversary_date = day_monFormat.parse(day_monFormat.format(fullFormat.parse(anniversary)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c = anniversary_date == currentdate;

        holder.name.setText(name);
        holder.anniversary.setText(anniversary);

        holder.mainanni_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupadapter(mcontext, v, mobile, name, g, c);
            }
        });

        holder.mainanni_card.setOnLongClickListener(new View.OnLongClickListener() {
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
                                    message = "Mrs./Miss " + name + " Wish you a very happy anniversary." +
                                            " May your a celebration of love turn out as beautiful as the both of you." +
                                            " Best wishes to you both on this momentous occasion. Congratulations..!!";
                                    break;
                                case "Male":
                                    message = "Mr. " + name + " Wish you a very happy anniversary." +
                                            " May your a celebration of love turn out as beautiful as the both of you." +
                                            " Best wishes to you both on this momentous occasion. Congratulations..!!";
                                    break;
                                case "":
                                    message = "Mr./Mrs./Miss " + name + " Wish you a very happy anniversary." +
                                            " May your a celebration of love turn out as beautiful as the both of you." +
                                            " Best wishes to you both on this momentous occasion. Congratulations..!!";
                                    break;
                                default:
                                    message = "Mr./Mrs./Miss " + name + " Wish you a very happy anniversary." +
                                            " May your a celebration of love turn out as beautiful as the both of you." +
                                            " Best wishes to you both on this momentous occasion. Congratulations..!!";
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

    class mainanni_viewholder extends RecyclerView.ViewHolder {
        CardView mainanni_card;
        TextView name;
        TextView anniversary;

        mainanni_viewholder(View itemView) {
            super(itemView);
            mainanni_card = itemView.findViewById(R.id.mainanni_card);
            name = itemView.findViewById(R.id.cliday_name);
            anniversary = itemView.findViewById(R.id.clianni_anniversary);
        }
    }

}
