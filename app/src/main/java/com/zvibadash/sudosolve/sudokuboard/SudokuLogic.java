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

import android.util.Log;

import com.zvibadash.sudosolve.Globals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public abstract class SudokuLogic {
    public static HashSet<SudokuCoordinatesHolder> getErroneousCells(SudokuDigit[][] board) {
        HashSet<SudokuCoordinatesHolder> erroneousCells = new HashSet<>();

        for (ArrayList<ArrayList<SudokuCoordinatesHolder>> neighborhoodType : Globals.NEIGHBORHOOD_TYPES) {
            for (ArrayList<SudokuCoordinatesHolder> neighborhood : neighborhoodType) {
                for (SudokuCoordinatesHolder cord : neighborhood) {
                    SudokuDigit digit = board[cord.row - 1][cord.col - 1];
                    if (digit.getType() == SudokuDigitType.FILLED && digit.getDigit() > 0 && Collections.frequency(neighborhood.stream().map(c -> board[c.row - 1][c.col - 1].getDigit()).collect(Collectors.toList()), digit.getDigit()) > 1)
                        erroneousCells.add(cord);
                }
            }
        }
        return erroneousCells;
    }
}
