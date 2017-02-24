package newera.myapplication.ui;

import android.graphics.Bitmap;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public interface Clickable {

    String getName();

    int getNameId();

    Bitmap getIcone();

    int onClick(InputManager manager, CImageView view);

}
