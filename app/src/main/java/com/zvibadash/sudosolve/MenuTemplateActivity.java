package com.zvibadash.sudosolve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuTemplateActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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