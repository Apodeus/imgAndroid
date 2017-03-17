package newera.EliJ.image.processing.shaders;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;

import newera.EliJ.MainActivity;
import newera.EliJ.R;
import newera.EliJ.ScriptC_histogram;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;

/**
 * Created by Romain on 19/02/2017.
 */

public class HistogramEqualize extends Shader {

    public HistogramEqualize(MainActivity activity) {
        super(activity);
        this.drawableIconId = R.drawable.ic_histogram_straighten_black_24dp;
        this.clickableName = R.string.shaderHistogramName;
        this.item = EItems.F_HISTOGRAM_EQ;
    }

    @Override
    public void ApplyFilter(Image image) {
        if(image != null && !image.isEmpty()) {

            int[] histo = new int[256];
            for(int i = 0; i < 256; i++)
                histo[i] = 0;

            ScriptC_histogram rsHistogram = new ScriptC_histogram(renderScript);
            rsHistogram.set_size(image.getWidth() * image.getHeight());

            Allocation tmpArrayHistogram = Allocation.createSized(renderScript, Element.I32(renderScript), histo.length);
            tmpArrayHistogram.copyFrom(histo);

            //Get the total histogram of all bitmaps of the Image
            for (Bitmap[] arrBitmap : image.getBitmaps()){
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsHistogram.set_arr(tmpArrayHistogram);
                    rsHistogram.forEach_calculHistogram(in, out);
                }
            }

            //Copy the new histogram in the array histo
            tmpArrayHistogram.copyTo(histo);
            rsHistogram.set_histogram(histo);
            rsHistogram.invoke_createRemapArray();

            //Apply the equalization of the histogram
            for (Bitmap[] arrBitmap : image.getBitmaps()){
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsHistogram.forEach_Equalize(in, out);
                    out.copyTo(bitmap);
                }
            }
        }
    }

}
