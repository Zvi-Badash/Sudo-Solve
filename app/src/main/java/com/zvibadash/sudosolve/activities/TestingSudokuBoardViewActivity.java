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
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.networking.APIClient;
import com.zvibadash.sudosolve.networking.APIInterface;
import com.zvibadash.sudosolve.networking.RequestSolve;
import com.zvibadash.sudosolve.networking.ResponseSolved;
import com.zvibadash.sudosolve.sudokuboard.SudokuBoardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestingSudokuBoardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_sudoku_board_view);

        SudokuBoardView sbv = findViewById(R.id.sbv);
        Button[] digitControls = {
                findViewById(R.id.btSudokuKeyboardOne),
                findViewById(R.id.btSudokuKeyboardTwo),
                findViewById(R.id.btSudokuKeyboardThree),
                findViewById(R.id.btSudokuKeyboardFour),
                findViewById(R.id.btSudokuKeyboardFive),
                findViewById(R.id.btSudokuKeyboardSix),
                findViewById(R.id.btSudokuKeyboardSeven),
                findViewById(R.id.btSudokuKeyboardEight),
                findViewById(R.id.btSudokuKeyboardNine),
        };

        ImageButton btnErase = findViewById(R.id.btnErase);
        ImageButton btnMagic = findViewById(R.id.btnMagic);

        // Set the onClick for each button
        btnErase.setOnClickListener(v -> {
            sbv.clearDigitInSelected();
            sbv.unselect();
        });

        btnMagic.setOnClickListener(v -> {
            if (Globals.HAS_CONNECTION_TO_SERVER) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Solving...");
                progressDialog.show();

                APIInterface client = APIClient.getClient();
                String board = sbv.getBoardAsString();

                client.solve(new RequestSolve(board)).enqueue(new Callback<ResponseSolved>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseSolved> call, @NonNull Response<ResponseSolved> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            ResponseSolved responseFromAPI = response.body();
                            assert responseFromAPI != null;
                            StringBuilder solved = new StringBuilder(responseFromAPI.getSolved());
                            sbv.setDigitInSelected(solved.charAt(9 * (sbv.selectedRow - 1) + (sbv.selectedColumn - 1)) - '0');
                            Log.i("DIGIT", String.valueOf(solved.charAt(9 * (sbv.selectedRow - 1) + (sbv.selectedColumn - 1))));
                        } else
                            Log.e("CONNECTION", "ERROR CONNECTING.");
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseSolved> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("CONNECTION", t.getMessage());
                    }
                });
            }
        });

        for (int i = 0; i < digitControls.length; ++i) {
            int finalI = i;
            digitControls[i].setOnClickListener(view -> {
                sbv.setDigitInSelected(finalI + 1);
                sbv.unselect();
            });
        }
    }
}