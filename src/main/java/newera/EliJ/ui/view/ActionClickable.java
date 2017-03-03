package newera.EliJ.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import newera.EliJ.R;
import newera.EliJ.ui.Clickable;

/**
 * Created by echo on 02/03/2017.
 */
public abstract class ActionClickable implements Clickable {

    private Bitmap icon;
    private Context context;
    protected int drawableIconId;
    protected int clickableName;

    /**
     * Create a button for CircleMenu with icon, label and identification.
     * @param context App's context (MainActivity or View)
     */
    public ActionClickable(Context context)
    {
        this.context = context;
    }

    /**
     * Generate app's icon with color before drawing it.
     * @param iconSize Size of the icon
     */
    public void initIcon(int iconSize) {
        Drawable drawable = context.getResources().getDrawable(drawableIconId);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    /**
     * @return Icon to be drawn
     */
    public Bitmap getIcon() {
        return icon;
    }

    /**
     * @return Name to be displayed
     */
    public String getName()
    {
        return context.getResources().getString(clickableName);
    }
}
