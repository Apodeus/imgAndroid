package newera.myapplication.ui.view;

import android.graphics.Bitmap;

import newera.myapplication.R;
import newera.myapplication.ui.Clickable;
import newera.myapplication.ui.system.PictureFileManager;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionGallery implements Clickable{

    private String name = "Gallery";
    private int nameId = R.string.galleryName;
    private Bitmap icone = null;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNameId() {
        return nameId;
    }

    @Override
    public Bitmap getIcone() {
        return icone;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        PictureFileManager.LoadPictureFromGallery();
        return 0;
    }
}
