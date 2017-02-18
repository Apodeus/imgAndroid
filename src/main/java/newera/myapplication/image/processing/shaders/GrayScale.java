package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import newera.myapplication.MainActivity;
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
        for(Bitmap[] b1 : image.getBitmaps())
            for(Bitmap b : b1)
            {
                Allocation in = Allocation.createFromBitmap(renderScript, b);
                Allocation out = Allocation.createTyped(renderScript, in.getType());

                ScriptC_mono mono = new ScriptC_mono(renderScript);

                mono.forEach_black_and_white(in, out);

                out.copyTo(b);
            }
    }

    public String getName()
    {
        return "GrayScale";
    }
}