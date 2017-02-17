package newera.myapplication.ui.view;

import android.graphics.Bitmap;

import newera.myapplication.ui.Clickable;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionCamera implements Clickable {

    private String name = "Camera";
    private Bitmap icone = null;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Bitmap getIcone() {
        return null;
    }

    @Override
    public int onClick() {
        PictureFileManager.CreatePictureFileFromCamera();
        return 0;
    }

}
