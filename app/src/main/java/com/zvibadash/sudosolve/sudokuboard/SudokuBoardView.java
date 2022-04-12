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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.zvibadash.sudosolve.Globals;
import com.zvibadash.sudosolve.R;
import com.zvibadash.sudosolve.networking.APIClient;
import com.zvibadash.sudosolve.networking.APIInterface;
import com.zvibadash.sudosolve.networking.RequestSolve;
import com.zvibadash.sudosolve.networking.ResponseSolved;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SudokuBoardView extends View {
    private final boolean isEditable;
    private int height, width;
    private int cellSize;
    @ColorInt private final int filledColor, hintColor, errorColor, lineColor, highlightedColor, lessHighlightedColor;
    private Paint linePaint;
    private Paint letterPaint;
    private Paint highlightedPaint;

    public int selectedRow = -1, selectedColumn = -1;
    public final SudokuDigit[][] board = new SudokuDigit[9][9];

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
        _init();
    }

    private void _init() {
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

    private int _getColorFromType(SudokuDigitType type) {
        switch (type) {
            case ERROR:
                return errorColor;
            case FILLED:
                return filledColor;
            default:
                return hintColor;
        }
    }

    private void _drawGrid(Canvas canvas) {
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

    private void _drawNumbers(Canvas canvas) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                SudokuDigit currDigit = board[i][j];
                if (1 <= currDigit.getDigit() && currDigit.getDigit() <= 9) {
                    letterPaint.setColor(_getColorFromType(currDigit.getType()));
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

    private void _highlightCell(Canvas canvas, int row, int column) {
        canvas.drawRect(
                (column - 1) * cellSize, (row - 1) * cellSize,
                (column) * cellSize, (row) * cellSize,
                highlightedPaint
        );
    }

    private void _drawSelected(Canvas canvas) {
        // Change the color of the highlightedPaint
        highlightedPaint.setColor(highlightedColor);

        // Highlight the current cell
        _highlightCell(canvas, selectedRow, selectedColumn);

        // get the left up indices of the box
        int left = 3 * (int) Math.floor(selectedColumn / 3F - 0.1) + 1;
        int up = 3 * (int) Math.floor(selectedRow / 3F - 0.1) + 1;

        Log.i("SUDOKU_CONTROLS", "BOX: (" + left + ", " + up + ")");

        // Change the color of the highlightedPaint
        highlightedPaint.setColor(lessHighlightedColor);

        // Highlight the current row
        for (int i = 1; i <= board[0].length; ++i)
            if (i < up || i > up + 2)
                _highlightCell(canvas, i, selectedColumn);

        // Highlight the current column
        for (int j = 1; j <= board.length; ++j)
            if (j < left || j > left + 2)
                _highlightCell(canvas, selectedRow, j);

        // Highlight the current box
        for (int i = left; i < left + 3; ++i)
            for (int j = up; j < up + 3; ++j)
                _highlightCell(canvas, j, i);
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

    public void setDigitHintInSelected(int d) {
        if (selectedColumn == -1 || selectedRow == -1)
            return;

        board[selectedRow - 1][selectedColumn - 1].setDigit(d);
        board[selectedRow - 1][selectedColumn - 1].setType(SudokuDigitType.HINTED);
        invalidate();
    }

    public void fillDigitInSelected(int d) {
        if (selectedColumn == -1 || selectedRow == -1)
            return;

        if (board[selectedRow - 1][selectedColumn - 1].getType() == SudokuDigitType.HINTED && !isEditable)
            return;

        board[selectedRow - 1][selectedColumn - 1].setDigit(d);
        board[selectedRow - 1][selectedColumn - 1].setType(SudokuDigitType.FILLED);
        invalidate();
    }

    public void unselect() {
        selectedColumn = -1;
        selectedRow = -1;
        invalidate();
    }

    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();
        for (SudokuDigit[] row : board)
            for (SudokuDigit digit : row)
                sb.append(1 <= digit.getDigit() && digit.getDigit() <= 9 ? String.valueOf(digit.getDigit()) : "0");
        return sb.toString();
    }

    public void clearDigitInSelected() {
        setDigitInSelected(-1);
    }

    public void setBoardFromString(String newBoard) {
        for (int i = 0; i < newBoard.length(); ++i) {
            int d = newBoard.charAt(i) - '0';
            selectedColumn = i % 9 + 1;
            selectedRow = i / 9 + 1;

            if (d == 0) {
                setDigitInSelected(d);
            } else {
                setDigitHintInSelected(d);
            }
        }
        unselect();
    }

    public void clearAllFilled() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                selectedRow = i + 1;
                selectedColumn = j + 1;
                if (board[selectedRow - 1][selectedColumn - 1].getType() == SudokuDigitType.FILLED)
                    clearDigitInSelected();
            }
        }
        unselect();
    }

    public void magicFillSelected(Context context, StringBuilder[] cachedSolve) {
        if (selectedColumn == -1 || selectedRow == -1)
            return;

        if (Globals.HAS_CONNECTION_TO_SERVER) {
            if (cachedSolve[0] != null) {
                this.fillDigitInSelected(cachedSolve[0].charAt(9 * (this.selectedRow - 1) + (this.selectedColumn - 1)) - '0');
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Solving...");
                progressDialog.show();

                APIInterface client = APIClient.getClient();
                String board = this.getBoardAsString();

                client.solve(new RequestSolve(board)).enqueue(new Callback<ResponseSolved>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseSolved> call, @NonNull Response<ResponseSolved> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            ResponseSolved responseFromAPI = response.body();
                            assert responseFromAPI != null;

                            if (cachedSolve[0] == null)
                                cachedSolve[0] = new StringBuilder(responseFromAPI.getSolved());

                            fillDigitInSelected(cachedSolve[0].charAt(9 * (selectedRow - 1) + (selectedColumn - 1)) - '0');
                            Log.i("DIGIT", String.valueOf(cachedSolve[0].charAt(9 * (selectedRow - 1) + (selectedColumn - 1))));
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
        }
    }

    public void requestAndPrepareSolve(Context context, StringBuilder[] cachedSolve) {
        if (Globals.HAS_CONNECTION_TO_SERVER) {
            if (cachedSolve[0] == null) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Solving...");
                progressDialog.show();

                APIInterface client = APIClient.getClient();
                String board = this.getBoardAsString();

                client.solve(new RequestSolve(board)).enqueue(new Callback<ResponseSolved>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseSolved> call, @NonNull Response<ResponseSolved> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            ResponseSolved responseFromAPI = response.body();
                            assert responseFromAPI != null;

                            cachedSolve[0] = new StringBuilder(responseFromAPI.getSolved());
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
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _init();

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
        _drawSelected(canvas);

        // Draw the grid itself
        _drawGrid(canvas);

        // Draw the numbers
        _drawNumbers(canvas);
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