package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.util.Log;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_convolution;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class Convolution extends Shader{
    public enum ConvType{GAUSS, EDGE}

    private float factor_gauss = 1;
    private float[][] matrix_gauss = {
            {0.077847f, 0.123317f, 0.077847f},
            {0.123317f, 0.195346f, 0.123317f},
            {0.077847f, 0.123317f, 0.077847f},
    };

    private float factor_edge = 1;
    private float[][] matrix_edge = {
            {0, 1, 0},
            {1, -4, 1},
            {0, 1, 0},
    };

    public Convolution(MainActivity activity) {
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

                    ScriptC_convolution conv = new ScriptC_convolution(renderScript);

                    conv.set_h(b.getHeight());
                    conv.set_w(b.getWidth());
                    conv.set_in(in);
                    setupMatrix(conv, ConvType.EDGE);

                    conv.forEach_convolution(out);

                    out.copyTo(b);
                }
        }
        refreshImage();
    }

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderConvolutionName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcone() {
        return null;
    }

    private void setupMatrix(ScriptC_convolution conv, ConvType type){
        float[][] target = null;
        float factor = 1;

        if (type == ConvType.EDGE){
            target = matrix_edge;
            factor = factor_edge;
        } else if (type == ConvType.GAUSS){
            target = matrix_gauss;
            factor = factor_gauss;
        }
        conv.set_matrix_size(target.length);
        conv.set_matrix_factor(factor);

        float[] mat = MatrixToArray(target);
        Allocation matAlloc = Allocation.createSized(renderScript, Element.F32(renderScript), mat.length);
        matAlloc.copyFrom(mat);
        conv.set_matrix2D(matAlloc);
    }

    private String a(float[] ar){
        String out = "";
        for(float f : ar){
            out += f + ", ";
        }
        return out;
    }

    private float[] MatrixToArray(float[][] mat){
        float arr[] = new float[mat.length*mat[0].length];
        int i = 0;
        for(int y = 0; y<mat.length; ++y){
            for(int x = 0; x<mat[0].length; ++x){
                arr[i++] = mat[x][y];
            }
        }
        return arr;
    }

}