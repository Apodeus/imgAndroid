package newera.myapplication.ui.system;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import newera.myapplication.MainActivity;
import newera.myapplication.image.Image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class PictureFileManager {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static MainActivity Activity;

    private static String TmpPicturePath;

    private static File TmpPictureFile;

    static void setActivity(MainActivity activity) {
        Activity = activity;
    }

    /**
     * Calls camera app to take a picture, then saves it in the application directory.
     * Picture can later be retrieved with RetrievePictureFileFromCamera().
     */
    public static void CreatePictureFileFromCamera()
    {
        dispatchTakePictureIntent();
    }

    /**
     * Retrieve a picture previously saved with CreatePictureFileFromCamera().
     * @return A new Image object of the saved picture
     */
    public static Image RetrievePictureSavedFromCamera()
    {
        Image result = new Image();
        Bitmap img = BitmapFactory.decodeFile(TmpPicturePath);
        result.setBitmap(img);

        return result;
    }

    static void HandleResult(Intent data)
    {

    }

    private static void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Activity.getPackageManager()) != null)
        {
            try {
                createImageFile();
            }catch (IOException e) {
                Log.i("ERROR", "Failed to create ImageFile");
            }

            if (TmpPictureFile != null)
            {
                Uri pictureFileUri = FileProvider.getUriForFile(Activity, "com.newera.fileprovider", TmpPictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureFileUri);
                Activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    private static void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        TmpPicturePath = image.getAbsolutePath();
        TmpPictureFile = image;
    }
}
