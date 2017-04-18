package newera.EliJ.ui.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by echo on 24/03/2017.
 */
public class ToolConfig {
    private float sizeModifier;
    private Paint paint;

    public ToolConfig()
    {
        this.paint = new Paint();
        paint.setColor(Color.RED);
        sizeModifier = 1f;
    }

    public float getSizeModifier() {
        return sizeModifier;
    }

    public Paint getPaint() {
        return paint;
    }

    public Paint getEraser()
    {
        Paint p = new Paint();
        p.setAlpha(0);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        p.setAntiAlias(true);

        return p;
    }
}