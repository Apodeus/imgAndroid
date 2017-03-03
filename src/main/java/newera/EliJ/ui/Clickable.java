package newera.EliJ.ui;

import android.graphics.Bitmap;
import newera.EliJ.ui.view.CImageView;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public interface Clickable {

    String getName();

    void initIcon(int iconSize);

    Bitmap getIcon();

    int onClick(InputManager manager, CImageView view);

}
