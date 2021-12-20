package com.zvibadash.sudosolve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class HomeActivity extends MainMenuTemplateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView iv = findViewById(R.id.ivHomeGif);

        // This was taken from https://github.com/bumptech/glide, displays a gif in an imageView.
        Glide.with(this).load(R.drawable.home_sudoku).into(iv);

        findViewById(R.id.btCameraMode).setOnClickListener(view -> {
            startActivity(new Intent(this, CameraModeActivity.class));
        });
    }
}