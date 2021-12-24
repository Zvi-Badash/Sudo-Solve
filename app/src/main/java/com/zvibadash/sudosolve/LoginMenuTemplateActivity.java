package com.zvibadash.sudosolve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LoginMenuTemplateActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.opExit) {
            // TODO: FINALIZE ALL RESOURCES HERE ALSO.
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }
}