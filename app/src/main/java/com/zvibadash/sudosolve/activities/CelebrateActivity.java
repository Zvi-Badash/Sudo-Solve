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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.sudokuboard.SudokuBoardView;

import java.time.Duration;

public class CelebrateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_celebrate);
        this.setFinishOnTouchOutside(false);

        Button btShare = findViewById(R.id.btShare);
        Button btCancel = findViewById(R.id.btCancel);
        SudokuBoardView sbv = findViewById(R.id.celebratingSBV);
        TextView tvMessage = findViewById(R.id.tvCelebratingMessage);

        String timeDuration = getIntent().getExtras().getString("duration", "");
        int timesMagicUsed = getIntent().getExtras().getInt("timesMagicUsed", 0);

        StringBuilder sb = new StringBuilder();
        sb.append("Congratulations  \uD83C\uDF8A\uD83C\uDF89!!\n\n");
        sb.append("You have solved a Sudoku in ").append(timeDuration).append(", that is quite impressive!\n\n");
        if (timesMagicUsed > 0)
            sb.append("You have used ").append(timesMagicUsed).append(" \"Magic hints\", but that's perfectly okay \uD83D\uDE09\n\n");
        else
            sb.append("You haven't used a single \"Magic hint\", you did it yourself \uD83D\uDE04\n\n");
        sb.append("Please share your result with your friends!");
        tvMessage.setText(sb.toString());

        btShare.setOnClickListener(v -> shareResults(timeDuration, timesMagicUsed));

        btCancel.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));

        // Finally, set the board state to what was given from the referring activity
        new CountDownTimer(50, 10) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                sbv.setBoardFromArray(Globals.CURRENT_CELEBRATE_BOARD);
                sbv.invalidate();
            }
        }.start();
    }

    private void shareResults(String timeDuration, int timesMagicUsed) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        String message = "I have solved a Sudoku in SudoSolve!\n" +
                "It took " + timeDuration + " and I used " + timesMagicUsed + " \"Magic hints\"!";
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(share);
    }
}