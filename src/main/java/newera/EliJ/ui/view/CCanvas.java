package newera.EliJ.ui.view;

import android.graphics.Canvas;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.tools.Tool;

import java.util.ArrayList;

/**
 * Created by echo on 17/03/2017.
 */
public class CCanvas extends Canvas {
    private final static int PANEL_SIZE = 50;
    private int nw;
    private int nh;
    private CanvasTool[][] paintingCanvas;
    private Canvas currentCanvas;
    private Image image;


    public void initialize(Canvas canvas)
    {
        int w = image.getWidth();
        int h = image.getHeight();
        nw = w / PANEL_SIZE;
        nh = h / PANEL_SIZE;
        this.paintingCanvas = new CanvasTool[nw][nh];

        this.currentCanvas = canvas;

    }

    public void applyTool(Tool tool, ToolConfig config, int x, int y)
    {
        int toolSize = (int) (tool.getStandardSize() * config.getSizeModifier());
        CanvasTool[] panels = preparePanel(x, y, toolSize);

        for (CanvasTool b : panels)
        {
            if (!b.isInitialized())
                b.initialize();

            b.draw(tool, config, x, y);
        }
    }

    private CanvasTool[] preparePanel(int x, int y, int toolSize) {
        ArrayList<CanvasTool> result = new ArrayList<>();
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        for (int j = 0; j < nh; j++)
            for (int i = 0; i < nw; i++)
            {
                if (       paintingCanvas[i][j].getOffsetX() + paintingCanvas[i][j].getSize() > x - toolSize
                        && paintingCanvas[i][j].getOffsetX() - paintingCanvas[i][j].getSize() < x - toolSize
                        && paintingCanvas[i][j].getOffsetY() + paintingCanvas[i][j].getSize() > y - toolSize
                        && paintingCanvas[i][j].getOffsetY() - paintingCanvas[i][j].getSize() < y - toolSize)
                {
                    x1 = i;
                    y1 = j;
                }

                if (       paintingCanvas[i][j].getOffsetX() + paintingCanvas[i][j].getSize() > x + toolSize
                        && paintingCanvas[i][j].getOffsetX() - paintingCanvas[i][j].getSize() < x + toolSize
                        && paintingCanvas[i][j].getOffsetY() + paintingCanvas[i][j].getSize() > y + toolSize
                        && paintingCanvas[i][j].getOffsetY() - paintingCanvas[i][j].getSize() < y + toolSize)
                {
                    x2 = i;
                    y2 = j;
                }

            }

        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                result.add(paintingCanvas[i][j]);

        return (CanvasTool[]) result.toArray();
    }

    public void applyCanvasToImage(float alpha)
    {
        for (CanvasTool[] ab : paintingCanvas)
            for(CanvasTool b : ab)
                if (b.active)
                    currentCanvas.drawBitmap(b.getBitmap(), b.getOffsetX(), b.getOffsetY(), null);
    }


}
