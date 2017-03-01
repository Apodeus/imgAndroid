package newera.myapplication.ui.system;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ui.view.StartupView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by echo on 09/02/2017.
 */
public class SystemActionHandler {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_CREATE_DIRECTORY = 3;
    private static MainActivity Activity;

    /**
     * Set the activity often required to interact with Android and other apps.
     * @param activity The MainActivity of the application
     */
    public static void setActivity(MainActivity activity)
    {
        Activity = activity;
        PictureFileManager.setActivity(activity);
    }

    public static void removeStartupView()
    {
        StartupView sv = (StartupView) Activity.findViewById(R.id.startupView);
        if (sv != null)
            sv.setVisibility(View.GONE);
            //((ViewGroup) sv.getParent()).removeView(sv);
    }

    /**
     * Called by MainActivity as it.
     */
    public static void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_GALLERY)&& resultCode == RESULT_OK)
        {
            PictureFileManager.HandleResult(data);
            removeStartupView();
        }
    }

    public static void requestCreateDirectory() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CREATE_DIRECTORY);

                // REQUEST_CREATE_DIRECTORY is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public static void handleRequestPermissionsResult(int requestCode,
                                                      String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_CREATE_DIRECTORY: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //You can do what you ask for here

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
