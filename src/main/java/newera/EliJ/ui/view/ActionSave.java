package newera.EliJ.ui.view;

import android.content.Context;
import newera.EliJ.R;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionSave extends ActionClickable {

    public ActionSave(Context context)
    {
        super(context);
        this.drawableIconId = R.drawable.ic_sd_storage_black_24dp;
        this.clickableName = R.string.systemSaveName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(this, EItems.S_QUALITY_SAVE, view.getResources().getString(R.string.systemSavePicture));
        view.setCurrentAction(EItems.S_QUALITY_SAVE);
        return 0;
    }

}
