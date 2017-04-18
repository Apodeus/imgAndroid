package newera.EliJ.image.processing.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import newera.EliJ.R;

import java.net.ContentHandler;

/**
 * Created by echo on 17/03/2017.
 */
public class Brush extends Tool {
    private final static int RES_SIZE = 64;
    private int brushCircleId;
    private int brushEdgeId;
    private Bitmap circle;
    private Bitmap edge;
    private Context context;

    @Override
    public void initialize(Context context)
    {
        this.context = context;
        brushCircleId = R.drawable.brush_fill;
        brushEdgeId = R.drawable.brush_outline;
        this.standardSize = RES_SIZE;
        initializeBrush();
    }

    public void initializeBrush()
    {
        circle = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        edge = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circle);
        Canvas e = new Canvas(edge);
        Drawable d = context.getResources().getDrawable(brushCircleId);
        d.setBounds(0, 0, RES_SIZE, RES_SIZE);
        d.draw(c);
        d = context.getResources().getDrawable(brushEdgeId);
        d.setBounds(0, 0, RES_SIZE, RES_SIZE);
        d.draw(e);
    }

    @Override
    public Bitmap getBitmap()
    {
        return circle;
    }


}
