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

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.sudokuboard.SudokuBoardView;

public class SudokuSolvingActivity extends AppCompatActivity {
    final public StringBuilder[] cachedSolve = {null};
    String board;

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

        // Get the board state, as given by the referring activity
        board = getIntent().getExtras().getString("board", "000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        // Set the onClick methods for the digit buttons
        for (int i = 0; i < digitControls.length; ++i) {
            int finalI = i;
            digitControls[i].setOnClickListener(view -> {
                sbv.setDigitInSelected(finalI + 1);
                sbv.unselect();
            });
        }

        // Set the onClick for the refresh button
        btnRefresh.setOnClickListener(v -> sbv.setBoardFromString(board));

        // Set the onClick for the eraser button
        btnErase.setOnClickListener(v -> {
            sbv.clearDigitInSelected();
            sbv.unselect();
        });

        // Set the onClick for the magic button
        btnMagic.setOnClickListener(v -> sbv.magicFillSelected(this, cachedSolve));

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

        // Finally, set the board state to what was given from the referring activity
        new CountDownTimer(50, 10) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                sbv.setBoardFromString(board);
                sbv.requestAndPrepareSolve(SudokuSolvingActivity.this, cachedSolve);
            }
        }.start();
    }
}