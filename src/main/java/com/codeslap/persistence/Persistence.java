/*
 * Copyright 2012 CodeSlap
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeslap.persistence;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Returns the application persistence adapter. You must close the adapter when you don't need it anymore.
 *
 * @author cristian
 */
public class Persistence {
    private static final String TAG = Persistence.class.getSimpleName();
    private static final Map<String, SqlAdapter> QUICK_ADAPTERS = new HashMap<String, SqlAdapter>();

    /**
     * @param context used to open/create the database
     * @param dbName  database name identifier
     * @return an implementation of the {@link SqlAdapter} interface
     */
    public static SqlAdapter getSqliteAdapter(Context context, String dbName) {
        PersistenceLogManager.d(TAG, String.format("Getting database adapter for \"%s\" database", dbName));
        return new SqliteAdapterImpl(context, dbName);
    }

    /**
     * @param context used to open/create the database
     * @return an implementation of the {@link SqlAdapter} interface pointing to the first database created
     */
    public static SqlAdapter getSqliteAdapter(Context context) {
        PersistenceLogManager.d(TAG, String.format("Getting database adapter for \"%s\" database", PersistenceConfig.sFirstDatabase));
        return new SqliteAdapterImpl(context, PersistenceConfig.sFirstDatabase);
    }

    /**
     * @param context used to open/create the database
     * @return an implementation of the {@link RawQuery} interface. This will use the first database created.
     */
    public static RawQuery getRawQuery(Context context) {
        return new RawQueryImpl(context, PersistenceConfig.sFirstDatabase);
    }

    /**
     * @param context used to open/create the database
     * @param name    database name identifier
     * @return an implementation of the {@link RawQuery} interface
     */
    public static RawQuery getRawQuery(Context context, String name) {
        return new RawQueryImpl(context, name);
    }

    /**
     * @param context used to open/create the database
     * @param name    database name identifier
     * @return an implementation of the {@link SqlAdapter} interface that can be used once only
     */
    public static SqlAdapter getQuickAdapter(Context context, String name) {
        if (!QUICK_ADAPTERS.containsKey(name)) {
            QUICK_ADAPTERS.put(name, new QuickSqlAdapter(context, name));
        }
        return QUICK_ADAPTERS.get(name);
    }

    /**
     * @param context used to open/create the database
     * @return an implementation of the {@link SqlAdapter} interface that can be used once only. This will point
     *         to the first database created.
     */

    public static SqlAdapter quick(Context context) {
        return getQuickAdapter(context, PersistenceConfig.sFirstDatabase);
    }

    /**
     * @param context used to access to the preferences system
     * @param name    the name of the preference file
     * @return an implementation of the PreferencesAdapter interface.
     */
    public static PreferencesAdapter getPreferenceAdapter(Context context, String name) {
        return new PrefsAdapterImpl(context, name);
    }

    /**
     * @param context used to access to the preferences system
     * @return an implementation of the PreferencesAdapter interface pointing to the default sharepreferences
     */
    public static PreferencesAdapter getPreferenceAdapter(Context context) {
        return new PrefsAdapterImpl(context);
    }

    /**
     * Quick way to retrieve an object from the default preferences
     *
     * @param context  used to access to the preferences system
     * @param name    the name of the preference file
     * @param theClass the class to retrieve
     * @return a bean created from the preferences
     */
    public static <T> T quickPref(Context context, String name, Class<T> theClass) {
        PreferencesAdapter adapter = getPreferenceAdapter(context, name);
        return adapter.retrieve(theClass);
    }

    /**
     * Quick way to retrieve an object from the default preferences
     *
     * @param context  used to access to the preferences system
     * @param theClass the class to retrieve
     * @return a bean created from the preferences
     */
    public static <T> T quickPref(Context context, Class<T> theClass) {
        PreferencesAdapter adapter = getPreferenceAdapter(context);
        return adapter.retrieve(theClass);
    }
}
