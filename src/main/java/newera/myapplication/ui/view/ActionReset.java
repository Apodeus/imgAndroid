package newera.myapplication.ui.view;

import android.graphics.Bitmap;

import newera.myapplication.R;
import newera.myapplication.ui.Clickable;
import newera.myapplication.ui.system.PictureFileManager;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionReset implements Clickable {

    private String name = "Reset";
    private int nameId = R.string.resetName;
    private Bitmap icone = null;

    @Override
    public String getName() {
        return name;
    }

    public int getNameId()
    {
        return nameId;
    }

    @Override
    public Bitmap getIcone() {
        return icone;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        view.reinitialize();
        return 0;
    }
}
