package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_convolution;
import newera.myapplication.ScriptC_sobel;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class Convolution extends Shader{
    public enum ConvType{GAUSS, EDGE, LAPL, SOBEL, SOBEL_H, SOBEL_V}
    public ConvType matrix = ConvType.SOBEL;
    public int kernel_size = 3;

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

    private float factor_lapl = 1;
    private float[][] matrix_lapl = {
            {-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1},
    };

    private float factor_sobel = 1;
    private float[][][] matrix_sobel = {
            {{-1, -2, -1,},
             {0, 0, 0},
             {1, 2, 1},},
            {{-1, 0, 1,},
             {-2, 0, 2},
             {-1, 0, 1},},
    };

    public Convolution(MainActivity activity) {
        super(activity);
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_convolution rsConv = new ScriptC_convolution(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsConv.set_h(bitmap.getHeight());
                    rsConv.set_w(bitmap.getWidth());
                    rsConv.set_in(in);
                    if(matrix != ConvType.SOBEL) {
                        setupMatrix(rsConv, matrix);
                        rsConv.forEach_convolution(out);

                    } else {
                        Allocation sob_h = Allocation.createTyped(renderScript, in.getType());
                        Allocation sob_v = Allocation.createTyped(renderScript, in.getType());

                        setupMatrix(rsConv, ConvType.SOBEL_H);
                        rsConv.forEach_convolution(sob_h);
                        setupMatrix(rsConv, ConvType.SOBEL_V);
                        rsConv.forEach_convolution(sob_v);

                        ScriptC_sobel sobel = new ScriptC_sobel(renderScript);
                        sobel.set_in_h(sob_h);
                        sobel.set_in_v(sob_v);
                        sobel.forEach_sobel(out);
                    }
                    out.copyTo(bitmap);
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
        } else if (type == ConvType.LAPL){
            target = matrix_lapl;
            factor = factor_lapl;
        } else if (type == ConvType.SOBEL_H){
            target = matrix_sobel[0];
            factor = factor_sobel;
        } else if (type == ConvType.SOBEL_V){
            target = matrix_sobel[1];
            factor = factor_sobel;
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