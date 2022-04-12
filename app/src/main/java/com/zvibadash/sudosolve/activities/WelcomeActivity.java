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

package com.zvibadash.sudosolve.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.networking.APIClient;
import com.zvibadash.sudosolve.networking.APIInterface;
import com.zvibadash.sudosolve.networking.ResponseCheckConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new CountDownTimer(1_000, 1_000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        }.start();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking connection to server at " + APIInterface.BASE_URL + "...");
        progressDialog.show();

        APIInterface client = APIClient.getClient();
        client.isConnected().enqueue(new Callback<ResponseCheckConnection>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCheckConnection> call, @NonNull Response<ResponseCheckConnection> response) {
                progressDialog.dismiss();
                Globals.HAS_CONNECTION_TO_SERVER = response.isSuccessful();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCheckConnection> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("CONNECTION", t.getMessage());
            }
        });
    }
}