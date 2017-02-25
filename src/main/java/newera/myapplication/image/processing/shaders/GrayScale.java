package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_grayscale;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class GrayScale extends Shader{

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

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderGrayScaleName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcone() {
        return null;
    }

}