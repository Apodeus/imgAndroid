package newera.EliJ.ui.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import newera.EliJ.image.processing.tools.Tool;

/**
 * Created by echo on 24/03/2017.
 */
public class CanvasTool {
    private int offsetX;
    private int offsetY;
    private int size;

    private Bitmap bitmap;

    private Canvas canvas;
    public boolean active = false;
    public CanvasTool(int offsetX, int offsetY, int size)
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.size = size;
    }

    public boolean isInitialized()
    {
        return bitmap != null;
    }

    public boolean initialize()
    {
        this.bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
        this.active = true;
        return this.isInitialized();
    }

    public Canvas getCanvas()
    {
        return this.canvas;
    }

    public void draw(Tool tool, ToolConfig config, int x, int y) {
        //x = x % size;
        //y = y % size;
        x = x - offsetX - (int) (tool.getStandardSize()*config.getSizeModifier()/2);
        y = y - offsetY - (int) (tool.getStandardSize()*config.getSizeModifier()/2);
        canvas.drawBitmap(tool.getBitmap(), x, y, config.getPaint());

    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getSize() {
        return size;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
