/*
 * MIT License
 *
 * Copyright (c) 2021 Zvi Badash
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zvibadash.sudosolve.networking;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.activities.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isInitialStickyBroadcast())
            return;

        // Upon calling this method, the internet connectivity has changed.
        // Either way, an action needs to be done:
        if (checkConnection(context)) {
            APIInterface client = APIClient.getClient();
            client.isConnected().enqueue(new Callback<ResponseCheckConnection>() {
                @Override
                public void onResponse(@NonNull Call<ResponseCheckConnection> call, @NonNull Response<ResponseCheckConnection> response) {
                    Globals.HAS_CONNECTION_TO_SERVER = response.isSuccessful();

                    if (Globals.HAS_CONNECTION_TO_SERVER) {
                        // Raise a dialog box
                        AlertDialog adGainedConn = new AlertDialog.Builder(context).create();
                        adGainedConn.setTitle("Internet Connection Was Restored");
                        adGainedConn.setMessage("Internet connection was restored. Please return to the home screen to play in online mode.\nIf you cancel, you'll need to restart the app to gain back all online features.");
                        adGainedConn.setButton(AlertDialog.BUTTON_POSITIVE, "Go Back",
                                (dialog, which) -> {
                                    context.startActivity(new Intent(context, HomeActivity.class));
                                    dialog.dismiss();
                                });
                        adGainedConn.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                (dialog, which) -> {
                                    Globals.HAS_CONNECTION_TO_SERVER = false;
                                    dialog.dismiss();
                                });
                        adGainedConn.show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseCheckConnection> call, @NonNull Throwable t) {
                    Log.e("CONNECTION", t.getMessage());
                }
            });
        } else {
            AlertDialog adLostConn = new AlertDialog.Builder(context).create();
            adLostConn.setTitle("Internet Connection Was Lost");
            adLostConn.setMessage("Internet connection was Lost. Please return to the home screen to play in offline mode.");
            adLostConn.setButton(AlertDialog.BUTTON_POSITIVE, "Go Back",
                    (dialog, which) -> {
                        Globals.HAS_CONNECTION_TO_SERVER = false;
                        context.startActivity(new Intent(context, HomeActivity.class));
                        dialog.dismiss();
                    });
            adLostConn.show();
        }

    }

    boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }
}
