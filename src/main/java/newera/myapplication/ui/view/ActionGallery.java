package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_collections_black_24dp);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    @Override
    public Bitmap getIcon() { return icon; }


    @Override
    public int onClick(InputManager manager, CImageView view) {
        PictureFileManager.LoadPictureFromGallery();
        return 0;
    }
}
