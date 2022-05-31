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

/* *
 * This activity should be reached after determining the sudoku that needs to be solved,
 * and this activity should be the ONLY place where you can SOLVE sudokus.
 */

// TODO: (!!!) modularize this awful code, create getSolved or something to relieve the monstrous code dump in the magic button.

package com.zvibadash.sudosolve.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.sudokuboard.SudokuBoardView;
import com.zvibadash.sudosolve.sudokuboard.SudokuDigit;
import com.zvibadash.sudosolve.sudokuboard.SudokuLogic;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class SudokuSolvingActivity extends AppCompatActivity {
    final public StringBuilder[] cachedSolve = {null};
    String board;
    Instant startTime = Instant.now();
    boolean hasUsedSolved = false;
    int timesMagicUsed = 0;
    KonfettiView konfettiView;

    private void celebrate(SudokuBoardView sbv) {
        Toast.makeText(SudokuSolvingActivity.this, "Hurray!", Toast.LENGTH_SHORT).show();
        sbv.celebrate();
        String stringDuration = Duration.between(startTime, Instant.now()).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase()
                .replaceAll("s", " Seconds")
                .replaceAll("m", " Minutes");

        // Play a celebrating sound
        MediaPlayer.create(this, R.raw.fireworks).start();

        // Display the confetti !
        konfettiView = findViewById(R.id.viewKonfetti);
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(2f, 8f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 15_000L);

        // Raise the dialog activity
        new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                Globals.CURRENT_CELEBRATE_BOARD = sbv.board;
                startActivity(
                        new Intent(SudokuSolvingActivity.this, CelebrateActivity.class)
                        .putExtra("duration", stringDuration)
                        .putExtra("timesMagicUsed", timesMagicUsed)
                );
            }
        }.start();
    }

    private void checkSolved(SudokuBoardView sbv) {
        for (SudokuDigit[] row : sbv.board)
            for (SudokuDigit cell : row)
                if (cell.getDigit() <= 0)
                    return;

        if (!SudokuLogic.getErroneousCells(sbv.board).isEmpty())
            return;

//        if (!hasUsedSolved)
//            return;

        celebrate(sbv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solving);

        // Set all view IDs
        SudokuBoardView sbv = findViewById(R.id.solvingSBV);
        Button[] digitControls = {
                findViewById(R.id.solvingBtSudokuKeyboardOne),
                findViewById(R.id.solvingBtSudokuKeyboardTwo),
                findViewById(R.id.solvingBtSudokuKeyboardThree),
                findViewById(R.id.solvingBtSudokuKeyboardFour),
                findViewById(R.id.solvingBtSudokuKeyboardFive),
                findViewById(R.id.solvingBtSudokuKeyboardSix),
                findViewById(R.id.solvingBtSudokuKeyboardSeven),
                findViewById(R.id.solvingBtSudokuKeyboardEight),
                findViewById(R.id.solvingBtSudokuKeyboardNine),
        };

        ImageButton btnErase = findViewById(R.id.solvingBtErase);
        ImageButton btnMagic = findViewById(R.id.solvingBtMagic);
        ImageButton btnRefresh = findViewById(R.id.solvingBtRefresh);
        ImageButton btnSolve = findViewById(R.id.solvingBtSolve);
        ImageButton btnErrors = findViewById(R.id.solvingBtErrors);


        // Get the board state, as given by the referring activity
        board = getIntent().getExtras().getString("board", "000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        // Set the onClick methods for the digit buttons
        for (int i = 0; i < digitControls.length; ++i) {
            int finalI = i;
            digitControls[i].setOnClickListener(view -> {
                sbv.setDigitInSelected(finalI + 1);
                sbv.unselect();
                checkSolved(sbv);
            });
        }

        // Set the onClick for the refresh button
        btnRefresh.setOnClickListener(v -> {
            AlertDialog adRefresh = new AlertDialog.Builder(this).create();
            adRefresh.setTitle("Refresh Sudoku");
            adRefresh.setMessage("Are you sure you want to restart?\nAll progress will be lost for this Sudoku.");
            adRefresh.setButton(AlertDialog.BUTTON_POSITIVE, "Refresh",
                    (dialog, which) -> {
                        sbv.setBoardFromString(board);
                        timesMagicUsed = 0;
                        hasUsedSolved = false;
                        startTime = Instant.now();
                        dialog.dismiss();
                    });
            adRefresh.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    (dialog, which) -> dialog.dismiss());
            adRefresh.show();
        });

        // Set the onClick for the eraser button
        btnErase.setOnClickListener(v -> {
            sbv.clearDigitInSelected();
            sbv.unselect();
        });

        // Set the onClick for the magic button
        btnMagic.setOnClickListener(v -> {
            ++timesMagicUsed;
            sbv.magicFillSelected(this, cachedSolve);
            sbv.unselect();
            checkSolved(sbv);
        });

        // Set the onClick for the solve button
        btnSolve.setOnClickListener(v -> {
            for (int i = 0; i < sbv.board.length; ++i) {
                for (int j = 0; j < sbv.board[0].length; ++j) {
                    sbv.selectedRow = i + 1;
                    sbv.selectedColumn = j + 1;
                    sbv.magicFillSelected(this, cachedSolve);
                }
            }
            sbv.unselect();
        });

        // Set the onClick for the error button
        btnErrors.setOnClickListener(v -> {
            sbv.unselect();
            sbv.shouldDrawErrors = true;

            // Add all currently erroneous cells to the inner array of errors.
            sbv.erroneousCells.addAll(SudokuLogic.getErroneousCells(sbv.board));
            sbv.invalidate();

            new CountDownTimer(1500, 100) {
                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    sbv.erroneousCells = new HashSet<>();
                    sbv.shouldDrawErrors = false;
                    sbv.invalidate();
                }
            }.start();
        });

        // Finally, set the board state to what was given from the referring activity
        new CountDownTimer(50, 10) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                sbv.setBoardFromString(board);
                sbv.requestAndPrepareSolve(SudokuSolvingActivity.this, cachedSolve);
            }
        }.start();
    }
}