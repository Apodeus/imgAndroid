package newera.myapplication.image.processing.shaders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_sepia;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class Sepia extends Shader{

    private Bitmap icon;

    public Sepia(MainActivity activity) {
        super(activity);
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_sepia rsSepia = new ScriptC_sepia(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {

                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsSepia.forEach_sepia(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

    @Override
    public void initIcon(Context context, int iconSize) {
        Drawable d = context.getResources().getDrawable(R.drawable.ic_photo_filter_sepia_24dp);
        d.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        d.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(icon);
        d.draw(c);
    }

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderSepiaName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }


}