package newera.EliJ.image.processing.shaders;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import newera.EliJ.R;
import newera.EliJ.ScriptC_grayscale;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;


/**
 * Created by romain on 09/02/17.
 */

public class GrayScale extends Shader{

    public GrayScale(Context context) {
        super(context);
        this.drawableIconId = R.drawable.ic_grayscale_gradient_black_24dp;
        this.clickableName = R.string.shaderGrayScaleName;
        this.item = EItems.F_GRAYSCALE;
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
        //refreshImage();
    }

}