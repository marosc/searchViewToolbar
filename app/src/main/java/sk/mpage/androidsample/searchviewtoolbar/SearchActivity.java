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


package sk.mpage.androidsample.searchviewtoolbar;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import sk.mpage.androidsample.searchviewtoolbar.fragments.ResultFragment;
import sk.mpage.androidsample.searchviewtoolbar.fragments.SearchFragment;

public class SearchActivity  extends AppCompatActivity {
    private Toolbar topToolBar;
    private SearchManager searchManager;
    private SearchView searchView;
    private String query = null;
    private TextView toolbarTitle;
    private int result_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.i("SEARCHVIEWMAIN", "handleIntent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("searchtoolbar","Query: "+query);
            getSupportActionBar().setTitle("Search for : \"" + query + "\"");

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("query", query);
            Fragment fragment = new SearchFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();

        }else if (Intent.ACTION_VIEW.equals(intent.getAction())){
            result_id = Integer.valueOf(intent.getData().getLastPathSegment());
            String[] names = getResources().getStringArray(R.array.names);
            query = names[result_id];
            getSupportActionBar().setTitle(query);
            Log.d("searchtoolbar", "Result: " + result_id);

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putString("name", query);
            Fragment fragment = new ResultFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.close){
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        else if (id == R.id.search) {
            if (query != null) {
                item.expandActionView();
                searchView.setQuery(query, false);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
