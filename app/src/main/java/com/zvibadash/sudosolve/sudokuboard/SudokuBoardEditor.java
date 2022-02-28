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

package com.zvibadash.sudosolve.sudokuboard;

import android.widget.Button;

public class SudokuBoardEditor {
    SudokuBoardView sbv;
    Button[] digitControls;
    Button btnMagic, btnErase;
    Boolean isSolve;
    int[][] solved;

    public SudokuBoardEditor(SudokuBoardView sbv, Button[] digitControls, Button btnMagic, Button btnErase, Boolean isSolve, int[][] solved) {
        this.sbv = sbv;
        this.digitControls = digitControls;
        this.btnMagic = btnMagic;
        this.btnErase = btnErase;
        this.isSolve = isSolve;
        this.solved = solved;

        // Set the onClick for each button
        btnErase.setOnClickListener(v -> {
            sbv.clearDigitInSelected();
        });

        btnMagic.setOnClickListener(v -> {
            if(isSolve)
                sbv.setDigitInSelected(solved[sbv.selectedRow - 1][sbv.selectedColumn - 1]);
        });

        for (int i = 0; i < digitControls.length; ++i) {
            int finalI = i;
            digitControls[i].setOnClickListener(view -> sbv.setDigitInSelected(finalI + 1));
        }
    }
}
