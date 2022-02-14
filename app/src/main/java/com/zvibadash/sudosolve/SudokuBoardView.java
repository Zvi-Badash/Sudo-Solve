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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

public class SudokuBoardView extends View {
    private final boolean isEditable;
    private int height, width;
    private int cellSize;
    @ColorInt private final int filledColor, hintColor, errorColor, lineColor, highlightedColor, lessHighlightedColor;
    private Paint linePaint;
    private Paint letterPaint;
    private Paint highlightedPaint;

    public int selectedRow = -1, selectedColumn = -1;
    private final SudokuDigit[][] board = new SudokuDigit[9][9];

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SudokuBoardView,
                0, 0);

        try {
            isEditable = a.getBoolean(R.styleable.SudokuBoardView_isEditable, false);
            filledColor = a.getColor(R.styleable.SudokuBoardView_filledColor, Color.BLUE);
            hintColor = a.getColor(R.styleable.SudokuBoardView_hintColor, Color.BLACK);
            errorColor = a.getColor(R.styleable.SudokuBoardView_errorColor, Color.RED);
            lineColor = a.getColor(R.styleable.SudokuBoardView_lineColor, Color.BLACK);
            highlightedColor = a.getColor(R.styleable.SudokuBoardView_highlightedColor, Color.parseColor("#AA6bd6d6"));
            lessHighlightedColor = ColorUtils.setAlphaComponent(highlightedColor, (int) (Color.alpha(highlightedColor) * 0.35F));
        } finally {
            a.recycle();
        }
    }

    private void init() {
        letterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setTextAlign(Paint.Align.CENTER);
        letterPaint.setTypeface(getResources().getFont(R.font.varela_round_regular));
        letterPaint.setTextSize(90);

        highlightedPaint.setColor(highlightedColor);
        highlightedPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < board.length; ++i)
            for (int j = 0; j < board[0].length; ++j)
                board[i][j] = new SudokuDigit(-1, SudokuDigitType.EMPTY);
    }

    private int getColorFromType(SudokuDigitType type) {
        switch (type) {
            case ERROR:
                return errorColor;
            case FILLED:
                return filledColor;
            default:
                return hintColor;
        }
    }

    private void drawGrid(Canvas canvas) {
        linePaint.setColor(lineColor);
        final float THIN = 5F;
        final float THICK = 15F;
        final float THICKEST = 27.5F;

        for (int i = 0; i <= 9; ++i) {
            linePaint.setStrokeWidth(i % 3 == 0 ? THICK : THIN);

            // The ith column
            canvas.drawLine(
                    i * cellSize, 0,
                    i * cellSize, height,
                    linePaint
            );

            // The ith row
            canvas.drawLine(
                    0, i * cellSize,
                    width, i * cellSize,
                    linePaint
            );
        }

        linePaint.setStrokeWidth(THICKEST);
        canvas.drawLine(
                0, 0,
                width, 0,
                linePaint
        );
        canvas.drawLine(
                0, 0,
                0, height,
                linePaint
        );
    }

    private void drawNumbers(Canvas canvas) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                SudokuDigit currDigit = board[i][j];
                if (1 <= currDigit.getDigit() && currDigit.getDigit() <= 9) {
                    letterPaint.setColor(getColorFromType(currDigit.getType()));
                    canvas.drawText(
                            Integer.toString(currDigit.getDigit()),
                            // TODO: FIGURE THIS OUT
                            // The text alignment solution is rather ugly, fix using Paint class methods.
                            (j + 0.5f) * cellSize, (i + 0.75f) * cellSize,
                            letterPaint
                    );
                }
            }
        }
    }

    private void highlightCell(Canvas canvas, int row, int column) {
        canvas.drawRect(
                (column - 1) * cellSize, (row - 1) * cellSize,
                (column) * cellSize, (row) * cellSize,
                highlightedPaint
        );
    }

    private void drawSelected(Canvas canvas) {
        // Change the color of the highlightedPaint
        highlightedPaint.setColor(highlightedColor);

        // Highlight the current cell
        highlightCell(canvas, selectedRow, selectedColumn);

        // get the left up indices of the box
        int left = 3 * (int) Math.floor(selectedColumn / 3F - 0.1) + 1;
        int up   = 3 * (int) Math.floor(selectedRow / 3F - 0.1) + 1;

        Log.i("SUDOKU_CONTROLS", "BOX: (" + left + ", " + up + ")");

        // Change the color of the highlightedPaint
        highlightedPaint.setColor(lessHighlightedColor);

        // Highlight the current row
        for (int i = 1; i <= board[0].length; ++i)
            if (i < up || i > up + 2)
                highlightCell(canvas, i, selectedColumn);

        // Highlight the current column
        for (int j = 1; j <= board.length; ++j)
            if (j < left || j > left + 2)
                highlightCell(canvas, selectedRow, j);

        // Highlight the current box
        for (int i = left; i < left + 3; ++i)
            for (int j = up; j < up + 3; ++j)
                highlightCell(canvas, j, i);
    }

    public void setDigitInSelected(int d) {
        if (selectedColumn == -1 || selectedRow == -1)
            return;

        if (board[selectedRow - 1][selectedColumn - 1].getType() == SudokuDigitType.HINTED && !isEditable)
            return;

        board[selectedRow - 1][selectedColumn - 1].setDigit(d);
        board[selectedRow - 1][selectedColumn - 1].setType(isEditable ? SudokuDigitType.HINTED : SudokuDigitType.FILLED);
        invalidate();
    }

    public void unselect() {
        selectedColumn = -1;
        selectedRow = -1;
        invalidate();
    }

    public void clearDigitInSelected() {
        setDigitInSelected(-1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();

        // Figure out how big we can make the board and it's cells.
        int maxdim = Math.min(w, h);
        this.width = maxdim;
        this.height = maxdim;
        this.cellSize = maxdim / 9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the selected cell
        drawSelected(canvas);

        // Draw the grid itself
        drawGrid(canvas);

        // Draw the numbers
        drawNumbers(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x < 0 || y < 0 || x > width - 35 || y > height - 35)
            return false;

        selectedRow = (int) Math.ceil(y / cellSize);
        selectedColumn = (int) Math.ceil(x / cellSize);

        invalidate();
        Log.i("SUDOKU_CONTROLS", "(" + selectedColumn + ", " + selectedRow + ")");

        return true;
    }
}