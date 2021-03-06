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

package com.zvibadash.sudosolve.database;


import android.provider.BaseColumns;

public final class SSDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SSDBContract() {}

    /* Inner class that defines the table contents */
    public static class User implements BaseColumns {
        // That is the table's name.
        public static final String TABLE_NAME = "user";

        // Here, the schema is actually detailed.
        public static final String USERNAME = "username";
    }

    public static class Statistics implements BaseColumns {
        // That is the table's name.
        public static final String TABLE_NAME = "stat";

        // Here, the schema is actually detailed.
        public static final String USERNAME = User.USERNAME;
        public static final String STATISTICS_CONTENTS = "stat_cont";
    }

    public static class LastSessionForUser implements BaseColumns {
        // That is the table's name.
        public static final String TABLE_NAME = "last_sess";

        // Here, the schema is actually detailed.
        public static final String USERNAME = User.USERNAME;
        public static final String SESSION_CONTENTS = "sess_cont";
    }
}