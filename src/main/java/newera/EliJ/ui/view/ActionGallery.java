package newera.EliJ.ui.view;

import android.content.Context;
import newera.EliJ.R;
import newera.EliJ.ui.system.PictureFileManager;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionGallery extends ActionClickable {

    public ActionGallery(Context context)
    {
        super(context);
        this.drawableIconId = R.drawable.ic_collections_black_24dp;
        this.clickableName = R.string.systemGalleryName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        PictureFileManager.LoadPictureFromGallery();
        return 0;
    }
}
