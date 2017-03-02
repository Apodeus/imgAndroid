package newera.myapplication.image.processing.shaders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_grayscale;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class GrayScale extends Shader{

    private Bitmap icon;

    public GrayScale(MainActivity activity) {
        super(activity);
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {

            ScriptC_grayscale rsGrayscale = new ScriptC_grayscale(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsGrayscale.forEach_Grayscale(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

    @Override
    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_grayscale_gradient_black_24dp);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderGrayScaleName);
    }

    @Override
    public int getNameId() {
        return R.string.shaderGrayScaleName;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }

}