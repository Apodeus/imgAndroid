package newera.myapplication.ui;

import android.content.Context;
import android.graphics.Bitmap;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public interface Clickable {

    String getName();

    int getNameId();

    void initIcon(Context context, int iconSize);

    Bitmap getIcon();

    int onClick(InputManager manager, CImageView view);

}
