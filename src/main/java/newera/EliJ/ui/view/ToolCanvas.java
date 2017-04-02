package newera.EliJ.ui.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import newera.EliJ.image.processing.tools.Tool;

/**
 * Created by echo on 24/03/2017.
 */
public class ToolCanvas {
    private int offsetX;
    private int offsetY;
    private int size;
    private Bitmap bitmap;
    private Canvas canvas;

    public ToolCanvas(int offsetX, int offsetY, int size)
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
        return this.isInitialized();
    }

    public Canvas getCanvas()
    {
        return this.canvas;
    }

    public void draw(Tool tool, ToolConfig config, int x, int y) {
        x = x - offsetX;
        y = y - offsetY;
        canvas.drawBitmap(tool.getBitmap(), x, y, config.getPaint());

    }
}
