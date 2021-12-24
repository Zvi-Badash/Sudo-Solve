// TODO tomorrow:
//      * make the code cleaner, more functional styled.
//      * save the b64 file in the same ../... directory as the images, for better convention and
//          easier access.

package com.zvibadash.sudosolve;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraModeActivity extends MainMenuTemplateActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    static final String IMG_SUFFIX         = ".jpg";
    static final String TXT_SUFFIX         = ".txt";
    static final String RAW_IMAGE_PREFIX   = "SUDOSOLVE_RAW_";
    static final String FINAL_IMAGE_PREFIX = "SUDOSOLVE_FINAL_";
    static final String DATE_PATTERN       = "yyyy-MM-dd HH:mm:ss.SSS";

    static final int QUALITY = 30; // Trial & error, seems like a good value

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_mode);

        dispatchTakePictureIntent();
    }

    // This callback will be activated when the image capture activity returns with an
    // image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("CAMERA_ACTIVITY", currentPhotoPath);
        Bitmap res = setImageFromPath(currentPhotoPath);
        createTextB64File(res);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace(); // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.zvibadash.sudosolve.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException   {
        // Create an image file name
        String timeStamp = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(new Date());
        String imageFileName = RAW_IMAGE_PREFIX + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/decoded/");
        File image = File.createTempFile(
                imageFileName,     /* prefix */
                IMG_SUFFIX,        /* suffix */
                storageDir         /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getBase64FromImage(Bitmap original) {
        // First, rotate the image 90° to the right.
        Matrix rot = new Matrix();
        rot.postRotate(90);
        Bitmap rotated = Bitmap.createBitmap(original,
                0,
                0,
                original.getWidth(),
                original.getHeight(),
                rot,
                true
        );

        // Now, encode that image to a byte array.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotated.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos);

        // Return the byte array encoded in base64.
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap setImageFromPath(String path) {
        Bitmap res = BitmapFactory.decodeFile(path);
        ImageView ivTest = findViewById(R.id.ivTest);
        ivTest.setImageBitmap(res);

        return res;
    }

    private String createTextB64File(Bitmap bp) {
        String currentEncodedName = "";
        try {
            String timeStamp = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(new Date());
            currentEncodedName = FINAL_IMAGE_PREFIX + timeStamp + "_" + TXT_SUFFIX;

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext()
                    .openFileOutput(currentEncodedName, Context.MODE_PRIVATE));

            outputStreamWriter.write(getBase64FromImage(bp));
            outputStreamWriter.close();
        }

        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        return currentEncodedName;
    }
}