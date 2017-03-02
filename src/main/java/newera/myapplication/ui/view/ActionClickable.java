package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import newera.myapplication.R;
import newera.myapplication.ui.Clickable;

/**
 * Created by echo on 02/03/2017.
 */
public abstract class ActionClickable implements Clickable {

    private Bitmap icon;
    private Context context;
    protected int drawableIconId;
    protected int clickableName;

    public ActionClickable(Context context)
    {
        this.context = context;
    }

    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(drawableIconId);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName()
    {
        return context.getResources().getString(clickableName);
    }
}
