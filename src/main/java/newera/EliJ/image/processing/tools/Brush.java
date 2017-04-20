package newera.EliJ.image.processing.tools;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import newera.EliJ.R;
import newera.EliJ.ui.view.ToolConfig;

import java.net.ContentHandler;

/**
 * Created by echo on 17/03/2017.
 */
public class Brush extends Tool {
    private final static int RES_SIZE = 64;
    private int brushCircleId;
    private Bitmap circle;
    private Bitmap edge;
    private Context context;
    private Drawable drawable;
    private Canvas canvas;

    @Override
    public void initialize(Context context)
    {
        this.context = context;
        brushCircleId = R.drawable.brush_fill;
        this.standardSize = RES_SIZE;
        initializeBrush();
    }

    public void initializeBrush()
    {
        circle = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(circle);
        drawable = context.getResources().getDrawable(brushCircleId);
        drawable.setBounds(0, 0, RES_SIZE, RES_SIZE);
        drawable.draw(canvas);
    }

    @Override
    public Bitmap getBitmap(Paint paint)
    {
        circle = Bitmap.createBitmap(RES_SIZE, RES_SIZE, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(circle);
        drawable.setColorFilter(paint.getColor(), PorterDuff.Mode.MULTIPLY);
        drawable.draw(canvas);
        return circle;
    }


}
