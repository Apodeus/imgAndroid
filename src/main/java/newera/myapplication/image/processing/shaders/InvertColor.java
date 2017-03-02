package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_invert;
import newera.myapplication.image.Image;

/**
 * Created by Romain on 18/02/2017.
 */

public class InvertColor extends Shader {

    private Bitmap icon;

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_invert rsInvert = new ScriptC_invert(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsInvert.forEach_invert(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

    @Override
    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_invert_color_24dp);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    public InvertColor(MainActivity activity) {
        super(activity);
    }

    public String getName(){ return activity.getResources().getString(R.string.shaderInvertColorName); }

    @Override
    public int getNameId() {
        return R.string.shaderInvertColorName;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }


}
