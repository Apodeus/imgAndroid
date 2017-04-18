package newera.EliJ.ui.system;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import newera.EliJ.MainActivity;
import newera.EliJ.image.Image;

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
    public static final int DECODE_TILE_SIZE = 512;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static int LAST_REQUEST;

    private static MainActivity Activity;

    private static File TmpPictureFile;

    private static Uri TmpUriFile;


    /**
     * Set the reference to MainActivity for systems calls.
     * @param activity App's MainActivity.
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

    /**
     * @param bitmap Bitmap to save
     * @param quality Quality of compressed Bitmap from 0 to 100 (correct would be 75-80)
     * @throws IOException
     * Save a bitmap at the location /Pictures/newera.EliJ/
     */
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


    /**
     * @return File to save picture to
     */
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

    public static void loadFromUri(Uri uri){
        TmpUriFile = uri;
        Image i = Activity.civ.getImage();
        if (i != null && !i.isEmpty())
            i.recycleBitmaps();
        Activity.civ.setImage(RetrieveSavedPictureFromIntent());
    }

    /**
     * Retrieve a picture previously saved with CreatePictureFileFromCamera() or LoadPictureFromGallery().
     * @return A new Image object of the saved picture
     */
    private static Image RetrieveSavedPictureFromIntent()
    {
        Image result = new Image();
        Bitmap img;

        try {
            if (TmpUriFile != null) {

                ParcelFileDescriptor parcelFD = Activity.getContentResolver().openFileDescriptor(TmpUriFile, "r");
                FileDescriptor fileDescriptor = parcelFD.getFileDescriptor();
                result.setOrig(TmpUriFile);

                Bitmap decoder = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                //BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(fileDescriptor, true);

                int h = decoder.getHeight();
                int w = decoder.getWidth();
                int xm, ym, xM, yM;//4160*3120

                Rect rect = new Rect();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inMutable = true;

                double rows = Math.ceil((double)w / (double)DECODE_TILE_SIZE);
                double lines = Math.ceil((double)h / (double)DECODE_TILE_SIZE);

                result.setDim((int)rows, (int)lines, w, h);
                //result.initDimOriginalBitmap((int)rows, (int)lines);

                for(int y = 0; y < lines; ++y){
                    for(int x = 0; x < rows; ++x){
                        xm = x * DECODE_TILE_SIZE;
                        ym = y * DECODE_TILE_SIZE;

                        xM = Math.min( (xm + DECODE_TILE_SIZE), w );
                        yM = Math.min( (ym + DECODE_TILE_SIZE), h );

                        rect.set(xm, ym, xM, yM);

                        //img = decoder.decodeRegion(rect, opts);
                        img = Bitmap.createBitmap(decoder, rect.left, rect.top, rect.width(), rect.height());
                        result.addBitmap(img, x, y);
                    }
                }
                parcelFD.close();

                /*for(int y = 0; y < lines; ++y)
                    for(int x = 0; x < rows; ++x)
                    {
                        img = result.getBitmap(x, y);
                        result.initOriginalBitmap(img, x, y);
                    }*/
                } else {
                Log.i("", "ERROR: TmpUriFile is empty.");
            }
        } catch(IOException e) {
            e.printStackTrace();
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

        Image i = Activity.civ.getImage();
        if (i != null && !i.isEmpty())
            i.recycleBitmaps();
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

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Activity.startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        LAST_REQUEST = REQUEST_IMAGE_GALLERY;
    }

    private static void createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Save a file: path for use with ACTION_VIEW intents

        TmpPictureFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        TmpUriFile = Uri.fromFile(TmpPictureFile);

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
