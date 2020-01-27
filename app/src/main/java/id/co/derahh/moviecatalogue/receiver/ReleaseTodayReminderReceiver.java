package id.co.derahh.moviecatalogue.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import id.co.derahh.moviecatalogue.API.APIClient;
import id.co.derahh.moviecatalogue.model.movie.Movie;
import id.co.derahh.moviecatalogue.R;
import id.co.derahh.moviecatalogue.activity.DetailActivity;
import id.co.derahh.moviecatalogue.database.DatabaseContract;
import id.co.derahh.moviecatalogue.model.movie.MovieResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseTodayReminderReceiver extends BroadcastReceiver {

    private static final String API_KEY = "06b9cd349f041c2e51292a90868062fc";
    private static final int NOTIF_ID_REPEATING = 102;
    private static final String CHANNEL_ID = "todayremainder" ;
    public static CharSequence CHANNEL_NAME = "NOTIFICATION";

    private APIClient api = APIClient.getInstance();

    public ReleaseTodayReminderReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String currentDate = sdf.format(date);

        api.getAPI().releaseMovie(API_KEY, currentDate, currentDate).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    Movie movie = response.body().getResults().get(i);
                    showNotification(context, movie);
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
    }

    private void showNotification(Context context, Movie movie) {

        Intent intent = new Intent(context, DetailActivity.class);
        Uri uri = Uri.parse(DatabaseContract.MovieColumns.CONTENT_URI + "/" + movie.getId());
        intent.setData(uri);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, "movie");
        intent.putExtra(DetailActivity.EXTRA_ID, movie.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(context.getString(R.string.release_today))
                .setContentText(movie.getTitle() + context.getString(R.string.has_release_today))
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        if (notificationManager != null) notificationManager.notify(movie.getId(), notification);
    }

    public void setRepeatingAlarm(Context context, String time) {
        String TIME_FORMAT = "HH:mm";
        if (isTimeInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intent = new Intent(context, ReleaseTodayReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    private boolean isTimeInvalid(String time, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(time);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
