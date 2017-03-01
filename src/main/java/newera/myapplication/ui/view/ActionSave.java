package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    private int nameId = R.string.saveName;
    private Bitmap icon = null;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNameId() {
        return nameId;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public void initIcon(Context context, int iconSize) {
        Drawable d = context.getResources().getDrawable(R.drawable.ic_sd_storage_black_24dp);
        d.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        d.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(icon);
        d.draw(c);
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.S_QUALITY_SAVE, view.getResources().getString(R.string.systemSavePicture));
        view.setCurrentAction(EItems.S_QUALITY_SAVE);
        return 0;
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
