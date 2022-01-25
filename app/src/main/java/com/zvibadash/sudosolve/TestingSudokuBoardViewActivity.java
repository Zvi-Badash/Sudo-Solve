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

package com.zvibadash.sudosolve;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class TestingSudokuBoardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_sudoku_board_view);

        SudokuBoardView sbv = findViewById(R.id.sbv);
        Button[] controls = {
                findViewById(R.id.btSudokuKeyboardOne),
                findViewById(R.id.btSudokuKeyboardTwo),
                findViewById(R.id.btSudokuKeyboardThree),
                findViewById(R.id.btSudokuKeyboardFour),
                findViewById(R.id.btSudokuKeyboardFive),
                findViewById(R.id.btSudokuKeyboardSix),
                findViewById(R.id.btSudokuKeyboardSeven),
                findViewById(R.id.btSudokuKeyboardEight),
                findViewById(R.id.btSudokuKeyboardNine),
                findViewById(R.id.btSudokuKeyboardDel)
        };

        for (int i = 0; i < controls.length; ++i) {
            int finalI = i;
            controls[i].setOnClickListener(view -> {
                if (view.getId() == R.id.btSudokuKeyboardDel)
                    sbv.clearDigitInSelected();
                else
                    sbv.setDigitInSelected(finalI + 1);
                sbv.unselect();
            });
        }
    }
}