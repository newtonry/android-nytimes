package com.fadetoproductions.rvkn.nytimessearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.fadetoproductions.rvkn.nytimessearch.R;

import java.io.IOException;

/**
 * Created by rnewton on 8/9/16.
 */
public class Reachability {

    Context context;

    public Reachability(Context context) {
        this.context = context;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public Boolean checkAndHandleConnection() {
        if (!isOnline()) {
            Toast.makeText(context, R.string.network_unreachable, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
