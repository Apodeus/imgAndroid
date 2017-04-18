package newera.EliJ.ui.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.tools.Tool;
import newera.EliJ.ui.system.PictureFileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echo on 17/03/2017.
 */
public class CCanvas {
    private final static int PANEL_SIZE = 150;
    private int nw;
    private int nh;
    private CanvasTool[][] paintingCanvas;
    //private Canvas currentCanvas;
    private Image image;
    private CImageView cv;
    private boolean isInitialized;


    public CCanvas(CImageView cv)
    {
        this.cv = cv;
    }

    public void initialize(Image image)
    {
        this.image = image;
        int w = image.getWidth();
        int h = image.getHeight();
        nw = w / PANEL_SIZE;
        nh = h / PANEL_SIZE;
        this.paintingCanvas = new CanvasTool[nw][nh];
        for (int i = 0; i < nw; i++)
            for (int j = 0; j < nh; j++)
            {
                paintingCanvas[i][j] = new CanvasTool(i * PANEL_SIZE, j * PANEL_SIZE, PANEL_SIZE);
                //paintingCanvas[i][j].initialize();
            }

        //this.currentCanvas = canvas;
        isInitialized = true;
    }

    public void applyTool(Tool tool, ToolConfig config, int x, int y)
    {
        float s = cv.getContentScale();
        x = (int)(x) - (int)(cv.getContentCoords().x - cv.getImage().getWidth()* s / 2);
        y = (int)(y) - (int)(cv.getContentCoords().y - cv.getImage().getHeight()* s / 2);

        int toolSize = (int) (tool.getStandardSize() * config.getSizeModifier());
        List<CanvasTool> panels = preparePanel(x, y, toolSize);

        for (CanvasTool b : panels)
        {
            if (!b.isInitialized())
                b.initialize();

            b.draw(tool, config, (int)(x/s), (int)(y/s));
        }
    }

    public void erase(Tool tool, ToolConfig config, int x, int y)
    {
        float s = cv.getContentScale();
        x = (int)(x) - (int)(cv.getContentCoords().x - cv.getImage().getWidth()* s / 2);
        y = (int)(y) - (int)(cv.getContentCoords().y - cv.getImage().getHeight()* s / 2);

        int toolSize = (int) (tool.getStandardSize() * config.getSizeModifier());
        List<CanvasTool> panels = preparePanel(x, y, toolSize);

        for (CanvasTool b : panels)
        {
            if (!b.isInitialized())
                b.initialize();

            b.erase(tool, config, (int)(x/s), (int)(y/s));
        }
    }

    private List<CanvasTool> preparePanel(int x, int y, int toolSize) {
        float s = cv.getContentScale();
        ArrayList<CanvasTool> result = new ArrayList<>();
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;

        for (int j = 0; j < nh; j++)
            for (int i = 0; i < nw; i++)
            {
                if (       paintingCanvas[i][j].getOffsetX()*s + paintingCanvas[i][j].getSize()*s > x - toolSize/2*s
                        && paintingCanvas[i][j].getOffsetX()*s < x - toolSize/2*s
                        && paintingCanvas[i][j].getOffsetY()*s + paintingCanvas[i][j].getSize()*s > y - toolSize/2*s
                        && paintingCanvas[i][j].getOffsetY()*s < y - toolSize/2*s)
                {
                    x1 = i;
                    y1 = j;
                }

                if (       paintingCanvas[i][j].getOffsetX()*s + paintingCanvas[i][j].getSize()*s > x + toolSize/2*s
                        && paintingCanvas[i][j].getOffsetX()*s < x + toolSize/2*s
                        && paintingCanvas[i][j].getOffsetY()*s + paintingCanvas[i][j].getSize()*s > y + toolSize/2*s
                        && paintingCanvas[i][j].getOffsetY()*s < y + toolSize/2*s)
                {
                    x2 = i;
                    y2 = j;
                }

            }

        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                result.add(paintingCanvas[i][j]);

        return result;
    }

    public void applyCanvasToImage(float alpha, Canvas canvas, int coordX, int coordY, float scale)
    {

        int cx = (coordX - (int)((cv.getImage().getWidth() - 1) * (scale/2)));
        int cy = (coordY - (int)((cv.getImage().getHeight() - 1) * (scale/2)));

        for (CanvasTool[] ab : paintingCanvas)
            for(CanvasTool b : ab)
                if (b.active)
                {
                    Rect dst = new Rect();
                    dst.left = cx + (int) (scale * (b.getOffsetX() - 1));
                    dst.top = cy + (int) (scale * (b.getOffsetY() - 1));
                    dst.right = dst.left + (int) (scale * b.getSize());
                    dst.bottom = dst.top + (int) (scale * b.getSize());
                    Paint p = new Paint();
                    p.setAlpha((int)(255 * alpha));
                    canvas.drawBitmap(b.getBitmap(), null, dst, p);
                }
    }


    public boolean isInitialized() {
        return isInitialized;
    }
}
