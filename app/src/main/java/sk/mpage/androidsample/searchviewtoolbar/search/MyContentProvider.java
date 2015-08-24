/*
 * Copyright (C) 2015 Maros Cavojsky, (mpage.sk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.mpage.androidsample.searchviewtoolbar.search;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import sk.mpage.androidsample.searchviewtoolbar.R;

public class MyContentProvider extends SearchRecentSuggestionsProvider{
    public final static String AUTHORITY = "sk.mpage.androidsample";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MyContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query= selectionArgs!=null && selectionArgs.length==1 ? selectionArgs[0] : null;
        int i=0;
        String[] names = getContext().getResources().getStringArray(R.array.names);

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_ICON_1,
                SearchManager.SUGGEST_COLUMN_QUERY,
                SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
        });

        for (String name : names){
            String[] firstLast = name.split(" ");
            if (firstLast.length!=2)
                continue;
            if (query!=null && name.toLowerCase().contains(query.toLowerCase())){
                matrixCursor.addRow(new Object[]{i,firstLast[0],firstLast[1],R.drawable.ic_account,query, Intent.ACTION_VIEW, i});
            }else if (TextUtils.isEmpty(query)){
                matrixCursor.addRow(new Object[]{i,firstLast[0],firstLast[1],R.drawable.ic_account,query, Intent.ACTION_VIEW, i});
            }

            i++;
        }
        Log.d("mysuggestprovider","Query: '"+query+"', Results: "+matrixCursor.getCount());
        return matrixCursor;
    }
}
