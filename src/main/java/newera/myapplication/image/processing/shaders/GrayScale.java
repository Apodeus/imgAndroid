package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_mono;
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
            for (Bitmap[] b1 : image.getBitmaps())
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    ScriptC_mono mono = new ScriptC_mono(renderScript);

                    mono.forEach_black_and_white(in, out);

                    out.copyTo(b);
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