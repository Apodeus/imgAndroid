package newera.myapplication.ui.system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import newera.myapplication.MainActivity;
import newera.myapplication.image.Image;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class PictureFileManager {
    public static final int DECODE_TILE_SIZE = 2048;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_CREATE_DIRECTORY = 3;
    private static int LAST_REQUEST;

    private static MainActivity Activity;

    private static String TmpPicturePath;

    private static File TmpPictureFile;

    private static ParcelFileDescriptor parcelFD;
    private static FileDescriptor fileDescriptor;
    private static Uri TmpUriFile;


    /** Set the reference to MainActivity for systems calls.
     * @param activity the MainActivity.
     */
    static void setActivity(MainActivity activity) {
        Activity = activity;
    }

    /**
     * Calls camera app to take a picture, then saves it in the application directory.
     * Picture can later be retrieved with RetrieveSavedPictureFromIntent().
     */
    public static void CreatePictureFileFromCamera()
    {
        LAST_REQUEST = REQUEST_IMAGE_CAPTURE;
        dispatchTakePictureIntent();
    }

    public static void SaveBitmap(Bitmap bitmap, int quality) throws IOException {

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            MediaScannerConnection.scanFile(Activity, new String[] { pictureFile.getPath() }, new String[] { "image/jpeg" }, null);
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Pictures/"
                + Activity.getApplicationContext().getPackageName()
        );

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "IMG_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    /**
     * Calls Gallery app to pick an already saved picture from phone.
     * Picture can later be retrieved with RetrieveSavedPictureFromIntent().
     */
    public static void LoadPictureFromGallery()
    {
        dispatchPickPictureFromGallery();
    }

    /**
     * Retrieve a picture previously saved with CreatePictureFileFromCamera() or LoadPictureFromGallery().
     * @return A new Image object of the saved picture
     */
    public static Image RetrieveSavedPictureFromIntent()
    {
        Image result = new Image();
        Bitmap img;

        try {
            if (TmpUriFile != null) {
                parcelFD = Activity.getContentResolver().openFileDescriptor(TmpUriFile, "r");
                fileDescriptor = parcelFD.getFileDescriptor();

                //img = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(fileDescriptor, true);
                int h = decoder.getHeight();
                int w = decoder.getWidth();
                int xm, xM, ym, yM;//4160*3120

                Rect r = new Rect();

                double width = Math.ceil((double)w / (double)DECODE_TILE_SIZE);
                double height = Math.ceil((double)h / (double)DECODE_TILE_SIZE);

                result.setDim((int)width, (int)height);
                result.initDimOriginalBitmap((int)width, (int)height);

                for(int y = 0; y < height; ++y){
                    for(int x = 0; x < width; ++x){
                        xm = x * (DECODE_TILE_SIZE - 1);
                        ym = y * (DECODE_TILE_SIZE - 1);

                        xM = Math.min( xm + DECODE_TILE_SIZE, w );
                        yM = Math.min( ym + DECODE_TILE_SIZE, h );

                        //Log.i("DBG", "rect= x("+xm +","+xM+"), y("+ym+","+yM+")");
                        r.set(xm, ym, xM, yM);

                        img = decoder.decodeRegion(r, null);

                        result.addBitmap(img, x, y);
                        result.initOriginalBitmap(img, x, y);
                    }
                }
                //result.setBitmap(img);
                parcelFD.close();
            } else {
                Log.i("", "ERROR: TmpUriFile is empty.");
            }
        } catch(IOException e) {
            Log.i("WARNING", "Cannot get file from Uri");
        }

        return result;
    }

    /**
     * Called by SystemActionHandler if an Intent returned something for this class.
     * @param data the data returned from the Intent.
     */
    static void HandleResult(Intent data)
    {
        if(LAST_REQUEST == REQUEST_IMAGE_GALLERY) {
            TmpUriFile = data.getData();
        }

        Activity.civ.setImage(RetrieveSavedPictureFromIntent());
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

    private static void dispatchPickPictureFromGallery()
    {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Activity.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        LAST_REQUEST = REQUEST_IMAGE_GALLERY;
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
        TmpUriFile = Uri.fromFile(TmpPictureFile);

    }
}
