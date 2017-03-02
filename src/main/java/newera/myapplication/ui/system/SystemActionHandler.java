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

    /**
     * create a request to allow the creation of a directory for saving content
     */
    public static void requestCreateDirectory() {

        if (ContextCompat.checkSelfPermission(Activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(Activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CREATE_DIRECTORY
                );
            }
        }

    }

    public static void handleRequestPermissionsResult(int requestCode,
                                                      String permissions[], int[] grantResults){
        switch (requestCode) {
            case REQUEST_CREATE_DIRECTORY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }

        }
    }
}
