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

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.networking.APIClient;
import com.zvibadash.sudosolve.networking.APIInterface;
import com.zvibadash.sudosolve.networking.DifficultyLevel;
import com.zvibadash.sudosolve.networking.ResponseGenerated;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends MainMenuTemplateActivity {

    Spinner difficultySpinner;
    Button btCaptureMode, btRandomMode;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv = findViewById(R.id.ivHomeGif);
        difficultySpinner = findViewById(R.id.spDifficulty);
        btCaptureMode = findViewById(R.id.btCaptureMode);
        btRandomMode = findViewById(R.id.btRandomMode);
        initDifficultySpinner();

        // This was taken from https://github.com/bumptech/glide, displays a gif in an imageView.
        Glide.with(this)
                .load(R.drawable.home_sudoku)
                .into(iv);

        btCaptureMode.setOnClickListener(view -> {
            startActivity(new Intent(this, CaptureModeActivity.class));
        });

        btRandomMode.setOnClickListener(this::randomOnClick);

        if (!Globals.HAS_CONNECTION_TO_SERVER) {
            btCaptureMode.setEnabled(false);
            btCaptureMode.setBackgroundColor(Color.LTGRAY);
        }
    }

    private void initDifficultySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        difficultySpinner.setAdapter(adapter);
    }

    private void randomOnClick(View v) {
        String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
        if (selectedDifficulty.equals("Select…"))
            Toast.makeText(this, "Please select a difficulty level", Toast.LENGTH_SHORT).show();
        else {
            generateAndStartActivity(DifficultyLevel.valueOf(selectedDifficulty.toUpperCase()));
        }
    }

    private void generateAndStartActivity(DifficultyLevel difficulty) {
        if (!Globals.HAS_CONNECTION_TO_SERVER) {
            offlineGenerateAndStartActivity(difficulty);
            Log.i("CONNECTION", "CONNECTION FAILED: fetching offline Sudokus.");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();

        APIInterface client = APIClient.getClient();
        client.generate(difficulty).enqueue(new Callback<ResponseGenerated>() {
            @Override
            public void onResponse(@NonNull Call<ResponseGenerated> call, @NonNull Response<ResponseGenerated> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseGenerated responseFromAPI = response.body();
                    assert responseFromAPI != null;

                    String gen = responseFromAPI.getGenerated();
                    startActivity(new Intent(HomeActivity.this, SudokuSolvingActivity.class)
                            .putExtra("board", gen));
                } else
                    Log.e("CONNECTION", "ERROR CONNECTING.");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseGenerated> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("CONNECTION", t.getMessage());
            }
        });
    }

    private void offlineGenerateAndStartActivity(DifficultyLevel difficulty) {
        Random prng = new Random();
        ArrayList<String> offlinePuzzlesWithDiff = Objects.requireNonNull(Globals.OFFLINE_PUZZLES.get(difficulty));

        String gen = offlinePuzzlesWithDiff.get(prng.nextInt(offlinePuzzlesWithDiff.size()));
        startActivity(new Intent(HomeActivity.this, SudokuSolvingActivity.class)
                .putExtra("board", gen));
    }
}