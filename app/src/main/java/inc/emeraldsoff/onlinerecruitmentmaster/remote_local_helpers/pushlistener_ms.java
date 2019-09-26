package inc.emeraldsoff.onlinerecruitmentmaster.remote_local_helpers;

import android.app.Activity;
import android.app.AlertDialog;

import com.microsoft.appcenter.push.PushListener;
import com.microsoft.appcenter.push.PushNotification;

import java.util.Map;

import inc.emeraldsoff.onlinerecruitmentmaster.R;

public class pushlistener_ms implements PushListener {

    @Override
    public void onPushNotificationReceived(Activity activity, PushNotification pushNotification) {

        /* The following notification properties are available. */
        String update_available = "New Update Available..!!";
        String title = pushNotification.getTitle();
        String message = pushNotification.getMessage();
        Map<String, String> customData = pushNotification.getCustomData();

        /*
         * Message and title cannot be read from a background notification object.
         * Message being a mandatory field, you can use that to check foreground vs background.
         */
        if (!title.isEmpty() && title.equals(update_available)) {
            updater.update_check(activity.getBaseContext());
        } else if (!title.isEmpty() && !title.equals(update_available)) {

            /* Display an alert for foreground push. */
//            AwesomeInfoDialog dialog = new AwesomeInfoDialog(activity);
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle(title)
                    .setMessage(message);
            if (!customData.isEmpty()) {
                dialog.setMessage(message + "\n" + customData);
            }
            dialog.setPositiveButton(android.R.string.ok, null)
                    .setIcon(R.drawable.ic_warning_pink_24dp)
//            dialog.setPositiveButtonText("OK")
//                    .setPositiveButtonClick(new Closure() {
//                        @Override
//                        public void exec() {
//
//                        }
//                    })
//                    .setDialogIconAndColor(R.drawable.ic_warning_pink_24dp, R.color.dialogInfoBackgroundColor)
                    .show();
        }
//        else {

        /* Display a toast when a background push is clicked. */
//            Toast.makeText(activity, String.format(activity.getString(R.string.push_toast), customData), Toast.LENGTH_LONG).show(); // For example R.string.push_toast would be "Push clicked with data=%1s"
//        }
    }
}
