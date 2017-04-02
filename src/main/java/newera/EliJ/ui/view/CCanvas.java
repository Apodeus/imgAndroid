package newera.EliJ.ui.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.tools.Tool;

/**
 * Created by echo on 17/03/2017.
 */
public class CCanvas extends Canvas {
    private final static int PANEL_SIZE = 50;
    private ToolCanvas[][] paintingCanvas;
    private boolean[][] absoluteAlpha;
    private Canvas currentCanvas;
    private Image image;


    public void initialize()
    {
        int w = image.getWidth();
        int h = image.getHeight();
        int nw = w / PANEL_SIZE;
        int nh = h / PANEL_SIZE;
        this.paintingCanvas = new ToolCanvas[nw][nh];
        this.absoluteAlpha = new boolean[w][h];
        for(boolean[] ar : this.absoluteAlpha)
            for(boolean a : ar)
                a = false;

    }

    public void applyTool(Tool tool, ToolConfig config, int x, int y)
    {
        int toolSize = (int) (tool.getStandardSize() * config.getSizeModifier());
        ToolCanvas[] panels = preparePanel(x, y, toolSize);

        for (ToolCanvas b : panels)
        {
            if (!b.isInitialized())
                b.initialize();

            b.draw(tool, config, x, y);
        }
    }

    private ToolCanvas[] preparePanel(int x, int y, int toolSize) {
        return null;
    }

    public void applyCanvasToImage(float alpha)
    {

    }


}
