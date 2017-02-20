package newera.myapplication.image.processing.shaders;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.util.Log;

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
    public void ApplyFilter(Image image) {
        if(image != null && !image.isEmpty()) {


            int[] histo = new int[256];
            for(int i = 0; i < 256; i++)
                histo[i] = 0;

            ScriptC_histogram rsHistogram = new ScriptC_histogram(renderScript);
            rsHistogram.set_size(image.getWidth() * image.getHeight());

            Allocation arr = Allocation.createSized(renderScript, Element.I32(renderScript), histo.length);
            arr.copyFrom(histo);


            for (Bitmap[] b1 : image.getBitmaps()){
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsHistogram.set_arr(arr);
                    rsHistogram.forEach_calculHistogram(in, out);
                }
            }

            arr.copyTo(histo);

            for(Bitmap[] b1 : image.getBitmaps()){
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsHistogram.forEach_calculHistogram(in, out);
                    rsHistogram.set_histogram(histo);

                    rsHistogram.invoke_createRemapArray();
                    rsHistogram.forEach_YUVToRGB(out, in);

                    in.copyTo(b);
                }
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
