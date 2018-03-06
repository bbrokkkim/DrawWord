package com.example.kkk.drawword;

import android.app.Application;
import android.util.Log;

import com.agsw.FabricView.FabricView;

/**
 * Created by KKK on 2018-01-21.
 */

public class App extends Application {

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        System.gc();
        Log.d("TAG", "_----------------------p GC");
    }

    public String getServerUrl() {
        return getString(R.string.project_id);
    }
}
