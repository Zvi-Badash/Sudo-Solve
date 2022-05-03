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

import androidx.annotation.NonNull;

public class SudokuCoordinatesHolder {
    int row;
    int col;

    public SudokuCoordinatesHolder(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SudokuCoordinatesHolder other = (SudokuCoordinatesHolder) obj;
        return this.col == other.col && this.row == other.row;
    }

    @Override
    public int hashCode() {
        final int prime = 173;
        int result = 1;
        result = prime * result + col;
        result = prime * result + row;

        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + this.row + ", " + this.col + ")";
    }
}
