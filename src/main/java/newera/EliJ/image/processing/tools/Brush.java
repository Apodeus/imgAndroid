package newera.EliJ.image.processing.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by echo on 17/03/2017.
 */
public class Brush extends Tool {
    private final static int RES_SIZE = 64;
    private Drawable brushCircle;
    private Drawable brushEdge;
    private Bitmap circle;
    private Bitmap edge;

    public void initializeBrush()
    {
        circle = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        edge = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circle);
        Canvas e = new Canvas(edge);
        brushCircle.draw(c);
        brushEdge.draw(e);
    }


}
