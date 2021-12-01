package com.zvibadash.sudosolve;


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