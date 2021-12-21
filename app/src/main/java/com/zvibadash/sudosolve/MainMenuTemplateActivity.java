package com.zvibadash.sudosolve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainMenuTemplateActivity extends AppCompatActivity {
    // Found this in StackOverflow when trying to display icons
    // inside the action bar menu.

    @SuppressLint("RestrictedApi") // Needed for this API
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            MenuBuilder mb = (MenuBuilder) menu;
            mb.setOptionalIconsVisible(true);
        }

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == R.id.opHelp) {
            startActivity(new Intent(this, HelpActivity.class)); return true;
        }
        else if (itemID == R.id.opHome) {
            startActivity(new Intent(this, HomeActivity.class)); return true;
        }
        else if (itemID == R.id.opLogout) {
            // TODO: Handle session save in the DB, only then exit.
            startActivity(new Intent(this, LoginActivity.class)); return true;
        }
        else if (itemID == R.id.opSettings) {
            startActivity(new Intent(this, SettingsActivity.class)); return true;
        }

        return super.onOptionsItemSelected(item);
    }
}