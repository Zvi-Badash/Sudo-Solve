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

import com.zvibadash.sudosolve.networking.DifficultyLevel;
import com.zvibadash.sudosolve.sudokuboard.SudokuCoordinatesHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class Globals {
    public static Session CURRENT_SESSION;
    public static boolean HAS_CONNECTION_TO_SERVER = false;

    private final static ArrayList<String> EASY_LIST;
    static { // Came from: https://stackoverflow.com/questions/33775273/how-to-declare-hashmap-in-global-and-add-values-into-it-only-for-the-first-time
        EASY_LIST = new ArrayList<>();
        EASY_LIST.add("005000008030000600910300750400567083000904075000020000569248017100000802820000590");
        EASY_LIST.add("060950800003000000495708126000100080051003000200005061009201035518006090000070018");
        EASY_LIST.add("920080405600495028804000697008003900000008070010060502790000004503870000100040050");
        EASY_LIST.add("049385002632000900001200007900000200465003701000490000006050084000002079104030506");
        EASY_LIST.add("300102007800000003001006005100825300008900070950600000209401050087203600506090402");
        EASY_LIST.add("020503007193080002070004010080000093004130085005000001000000034347601029019000506");
        EASY_LIST.add("003000009457100283091002040102940000075380006000000071049050802006200700008790000");
        EASY_LIST.add("670000258000200671012000040900100030100003026003600000320006090080902760796000012");
        EASY_LIST.add("850000000170800930600109700760083502085000000940502000016200470000714005000605100");
        EASY_LIST.add("000000000004003082003420910000041500000005070057030408090058104040190706200674039");
    }

    private final static ArrayList<String> MEDIUM_LIST;
    static {
        MEDIUM_LIST = new ArrayList<>();
        MEDIUM_LIST.add("583001000470000200100740000047950080092010050050200304700000000200000070038060400");
        MEDIUM_LIST.add("006308070702600000000920008504030200020076000000205007650700000009000706200560010");
        MEDIUM_LIST.add("800006020190000000000000004419830000000609200602001309060005003043090800028300100");
        MEDIUM_LIST.add("810020506200090803000800200007008100000900607002000000060109482009080305080200000");
        MEDIUM_LIST.add("000804003080073000000001804200040000000719000174306009001000730607000120800000096");
        MEDIUM_LIST.add("091503002820100500000020003360000000009005604047030008004000709900700021000900300");
        MEDIUM_LIST.add("200000607000107428040902130800000000000348050603000980002500803300800000000000560");
        MEDIUM_LIST.add("000000020650700003200008097000680000063201400007003008102006905000800301006000084");
        MEDIUM_LIST.add("260000700000860000904030080600503408000641000340000106020000300096078000030006800");
        MEDIUM_LIST.add("000000000020000000543019000930070502060400080104982006075000490300040010412000000");
    }

    private final static ArrayList<String> HARD_LIST;
    static {
        HARD_LIST = new ArrayList<>();
        HARD_LIST.add("010080064006004000000090020900003005031700008500960031002000000760000000000006010");
        HARD_LIST.add("000542080000060000500010004967000000000000090080090576700000030600000002839006000");
        HARD_LIST.add("100426370200000006000000000510800000849035007000001080000300050005010000030000020");
        HARD_LIST.add("400083006900004100080001000700105800210006040000030010090000001050002070000000400");
        HARD_LIST.add("000000000001060030000059080800520006602000050900631200000040063000370000004002000");
        HARD_LIST.add("000050000050097080000213540000305070000700300397000000000041890000000050500000020");
        HARD_LIST.add("000000057000060000081500000102000008007080000000000000006003902038720164000800705");
        HARD_LIST.add("100000000350008001094050020003000100000209000005030907430000000019080000500010800");
        HARD_LIST.add("000800306903005000084103900000000200000000078000006000000000400408651700001040003");
        HARD_LIST.add("000009200080700000207600380040007000802000000000051000010300002000508060600102030");
    }

    private final static ArrayList<String> INSANE_LIST;
    static {
        INSANE_LIST = new ArrayList<>();
        INSANE_LIST.add("000010008000000050000000390000400000000270080010000000206000000500004000000080000");
        INSANE_LIST.add("000070080000000000020000000000007009000000058000000064008000001003020006000000005");
        INSANE_LIST.add("560400902001007000000050000090070000000080000050000800000000090000000000000004000");
        INSANE_LIST.add("000930200000400000000000000436000000008020000000000008000000000040102005000000300");
        INSANE_LIST.add("000000000000000000000010500015000000000300000690100000006080000300709001100000000");
        INSANE_LIST.add("407000006000006000000049000000000000000420000000800400000090000040000050005000060");
        INSANE_LIST.add("000050000000000000000400027000304000000001000200000000100040003000000090030072000");
        INSANE_LIST.add("003100000000600900600000000201300000750400000000000000100000000030000000000004600");
        INSANE_LIST.add("003010805500000006700000001006000050000000000002000760000000007007000000000000000");
        INSANE_LIST.add("400009020000000070000500000040005000000200500000800000090050000501000000000090000");
    }

    public final static HashMap<DifficultyLevel, ArrayList<String>> OFFLINE_PUZZLES;
    static {
        OFFLINE_PUZZLES = new HashMap<>();
        OFFLINE_PUZZLES.put(DifficultyLevel.EASY, EASY_LIST);
        OFFLINE_PUZZLES.put(DifficultyLevel.MEDIUM, MEDIUM_LIST);
        OFFLINE_PUZZLES.put(DifficultyLevel.HARD, HARD_LIST);
        OFFLINE_PUZZLES.put(DifficultyLevel.INSANE, INSANE_LIST);
    }

    public final static ArrayList<ArrayList<SudokuCoordinatesHolder>> ROWS;
    public final static ArrayList<ArrayList<SudokuCoordinatesHolder>> COLS;
    public final static ArrayList<ArrayList<SudokuCoordinatesHolder>> BOXES;
    public final static ArrayList<ArrayList<ArrayList<SudokuCoordinatesHolder>>> NEIGHBORHOOD_TYPES;
    static {
        ROWS = new ArrayList<>();
        COLS = new ArrayList<>();
        BOXES = new ArrayList<>();
        NEIGHBORHOOD_TYPES = new ArrayList<>();

        for (int i = 0; i < 9; ++i) {
            ROWS.add(new ArrayList<>());
            COLS.add(new ArrayList<>());
            BOXES.add(new ArrayList<>());
        }

        for (int i = 1; i <= 9; ++i)
            for (int j = 1; j <= 9; ++j)
                ROWS.get(i - 1).add(new SudokuCoordinatesHolder(i, j));

        for (int i = 1; i <= 9; ++i)
            for (int j = 1; j <= 9; ++j)
                COLS.get(i - 1).add(new SudokuCoordinatesHolder(j, i));

        // First box at (1, 1)
        BOXES.get(0).add(new SudokuCoordinatesHolder(1, 1));
        BOXES.get(0).add(new SudokuCoordinatesHolder(1, 2));
        BOXES.get(0).add(new SudokuCoordinatesHolder(1, 3));
        BOXES.get(0).add(new SudokuCoordinatesHolder(2, 1));
        BOXES.get(0).add(new SudokuCoordinatesHolder(2, 2));
        BOXES.get(0).add(new SudokuCoordinatesHolder(2, 3));
        BOXES.get(0).add(new SudokuCoordinatesHolder(3, 1));
        BOXES.get(0).add(new SudokuCoordinatesHolder(3, 2));
        BOXES.get(0).add(new SudokuCoordinatesHolder(3, 3));

        // Second box at (1, 4)
        BOXES.get(1).add(new SudokuCoordinatesHolder(1, 4));
        BOXES.get(1).add(new SudokuCoordinatesHolder(1, 5));
        BOXES.get(1).add(new SudokuCoordinatesHolder(1, 6));
        BOXES.get(1).add(new SudokuCoordinatesHolder(2, 4));
        BOXES.get(1).add(new SudokuCoordinatesHolder(2, 5));
        BOXES.get(1).add(new SudokuCoordinatesHolder(2, 6));
        BOXES.get(1).add(new SudokuCoordinatesHolder(3, 4));
        BOXES.get(1).add(new SudokuCoordinatesHolder(3, 5));
        BOXES.get(1).add(new SudokuCoordinatesHolder(3, 6));

        // Third box at (1, 7)
        BOXES.get(2).add(new SudokuCoordinatesHolder(1, 7));
        BOXES.get(2).add(new SudokuCoordinatesHolder(1, 8));
        BOXES.get(2).add(new SudokuCoordinatesHolder(1, 9));
        BOXES.get(2).add(new SudokuCoordinatesHolder(2, 7));
        BOXES.get(2).add(new SudokuCoordinatesHolder(2, 8));
        BOXES.get(2).add(new SudokuCoordinatesHolder(2, 9));
        BOXES.get(2).add(new SudokuCoordinatesHolder(3, 7));
        BOXES.get(2).add(new SudokuCoordinatesHolder(3, 8));
        BOXES.get(2).add(new SudokuCoordinatesHolder(3, 9));



        // Fourth box at (4, 1)
        BOXES.get(3).add(new SudokuCoordinatesHolder(4, 1));
        BOXES.get(3).add(new SudokuCoordinatesHolder(4, 2));
        BOXES.get(3).add(new SudokuCoordinatesHolder(4, 3));
        BOXES.get(3).add(new SudokuCoordinatesHolder(5, 1));
        BOXES.get(3).add(new SudokuCoordinatesHolder(5, 2));
        BOXES.get(3).add(new SudokuCoordinatesHolder(5, 3));
        BOXES.get(3).add(new SudokuCoordinatesHolder(6, 1));
        BOXES.get(3).add(new SudokuCoordinatesHolder(6, 2));
        BOXES.get(3).add(new SudokuCoordinatesHolder(6, 3));

        // Fifth box at (4, 4)
        BOXES.get(4).add(new SudokuCoordinatesHolder(4, 4));
        BOXES.get(4).add(new SudokuCoordinatesHolder(4, 5));
        BOXES.get(4).add(new SudokuCoordinatesHolder(4, 6));
        BOXES.get(4).add(new SudokuCoordinatesHolder(5, 4));
        BOXES.get(4).add(new SudokuCoordinatesHolder(5, 5));
        BOXES.get(4).add(new SudokuCoordinatesHolder(5, 6));
        BOXES.get(4).add(new SudokuCoordinatesHolder(6, 4));
        BOXES.get(4).add(new SudokuCoordinatesHolder(6, 5));
        BOXES.get(4).add(new SudokuCoordinatesHolder(6, 6));

        // Sixth box at (4, 7)
        BOXES.get(5).add(new SudokuCoordinatesHolder(4, 7));
        BOXES.get(5).add(new SudokuCoordinatesHolder(4, 8));
        BOXES.get(5).add(new SudokuCoordinatesHolder(4, 9));
        BOXES.get(5).add(new SudokuCoordinatesHolder(5, 7));
        BOXES.get(5).add(new SudokuCoordinatesHolder(5, 8));
        BOXES.get(5).add(new SudokuCoordinatesHolder(5, 9));
        BOXES.get(5).add(new SudokuCoordinatesHolder(6, 7));
        BOXES.get(5).add(new SudokuCoordinatesHolder(6, 8));
        BOXES.get(5).add(new SudokuCoordinatesHolder(6, 9));



        // Seventh box at (7, 1)
        BOXES.get(6).add(new SudokuCoordinatesHolder(7, 1));
        BOXES.get(6).add(new SudokuCoordinatesHolder(7, 2));
        BOXES.get(6).add(new SudokuCoordinatesHolder(7, 3));
        BOXES.get(6).add(new SudokuCoordinatesHolder(8, 1));
        BOXES.get(6).add(new SudokuCoordinatesHolder(8, 2));
        BOXES.get(6).add(new SudokuCoordinatesHolder(8, 3));
        BOXES.get(6).add(new SudokuCoordinatesHolder(9, 1));
        BOXES.get(6).add(new SudokuCoordinatesHolder(9, 2));
        BOXES.get(6).add(new SudokuCoordinatesHolder(9, 3));

        // Eighth box at (7, 4)
        BOXES.get(7).add(new SudokuCoordinatesHolder(7, 4));
        BOXES.get(7).add(new SudokuCoordinatesHolder(7, 5));
        BOXES.get(7).add(new SudokuCoordinatesHolder(7, 6));
        BOXES.get(7).add(new SudokuCoordinatesHolder(8, 4));
        BOXES.get(7).add(new SudokuCoordinatesHolder(8, 5));
        BOXES.get(7).add(new SudokuCoordinatesHolder(8, 6));
        BOXES.get(7).add(new SudokuCoordinatesHolder(9, 4));
        BOXES.get(7).add(new SudokuCoordinatesHolder(9, 5));
        BOXES.get(7).add(new SudokuCoordinatesHolder(9, 6));

        // Ninth box at (7, 7)
        BOXES.get(8).add(new SudokuCoordinatesHolder(7, 7));
        BOXES.get(8).add(new SudokuCoordinatesHolder(7, 8));
        BOXES.get(8).add(new SudokuCoordinatesHolder(7, 9));
        BOXES.get(8).add(new SudokuCoordinatesHolder(8, 7));
        BOXES.get(8).add(new SudokuCoordinatesHolder(8, 8));
        BOXES.get(8).add(new SudokuCoordinatesHolder(8, 9));
        BOXES.get(8).add(new SudokuCoordinatesHolder(9, 7));
        BOXES.get(8).add(new SudokuCoordinatesHolder(9, 8));
        BOXES.get(8).add(new SudokuCoordinatesHolder(9, 9));

        NEIGHBORHOOD_TYPES.add(ROWS);
        NEIGHBORHOOD_TYPES.add(COLS);
        NEIGHBORHOOD_TYPES.add(BOXES);
    }
}
