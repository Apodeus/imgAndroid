package newera.myapplication.ui.view;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import newera.myapplication.R;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.Clickable;
import newera.myapplication.ui.system.PictureFileManager;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionSave implements Clickable{

    private String name = "Save";
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
        try {
            //improve this using a seekbar
            int quality = 85;
            PictureFileManager.SaveBitmap(view.getImage().getBitmap(), quality);
            return 0;
        }catch(IOException e){
            Log.i("", "Error: Save");
            return 1;
        }

    }


    /*
    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_CHANGE_HUE, view.getResources().getString(R.string.shaderChangeHueName));
        view.setCurrentAction(EItems.F_CHANGE_HUE);
        return 0;
    }
    */

}
