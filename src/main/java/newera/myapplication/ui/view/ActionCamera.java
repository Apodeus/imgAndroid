package newera.myapplication.ui.view;

import android.content.Context;
import newera.myapplication.R;
import newera.myapplication.ui.system.PictureFileManager;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionCamera extends ActionClickable {

    public ActionCamera(Context context)
    {
        super(context);
        this.drawableIconId = R.drawable.ic_photo_camera_black_24dp;
        this.clickableName = R.string.systemCameraName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        PictureFileManager.CreatePictureFileFromCamera();
        return 0;
    }
}
