package newera.myapplication.ui.system;

import android.content.Intent;
import newera.myapplication.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by echo on 09/02/2017.
 */
public class SystemActionHandler {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    /**
     * Set the activity often required to interact with Android and other apps.
     * @param activity The MainActivity of the application
     */
    public static void setActivity(MainActivity activity)
    {
        PictureFileManager.setActivity(activity);
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            PictureFileManager.HandleResult(data);
        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK)
            PictureFileManager.HandleResult(data);
    }
}
