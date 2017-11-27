package com.app.s236372.smaom;

import android.provider.BaseColumns;

public class DBinfo
{
    public static final String DBnavn = "SmaomDB";
    public static final int DBversjon = 1;

    public class Nyoppgave implements BaseColumns
    {
        public static final String TABELL = "oppgave";
        public static final String KOL = "Tittel";
    }
}
