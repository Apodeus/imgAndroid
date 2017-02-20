package newera.myapplication.image.processing.shaders;

import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_histogram;
import newera.myapplication.ScriptC_hue;
import newera.myapplication.image.Image;

/**
 * Created by Romain on 19/02/2017.
 */

public class HistogramEqualize extends Shader {
    private Bitmap icone = null;

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            for (Bitmap[] b1 : image.getBitmaps())
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    ScriptC_histogram rsHistogram = new ScriptC_histogram(renderScript);
                    rsHistogram.set_size(image.getWidth() *  image.getHeight());

                    rsHistogram.forEach_calculHistogram(in, out);
                    rsHistogram.invoke_createRemapArray();

                    rsHistogram.forEach_YUVToRGB(out, in);


                    in.copyTo(b);
                }
        }
        refreshImage();
    }

    public HistogramEqualize(MainActivity activity) {
        super(activity);
    }

    public String getName(){
        return activity.getResources().getString(R.string.shaderHistogramName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcone() {
        return icone;
    }

}
