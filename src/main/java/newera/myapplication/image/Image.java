package newera.myapplication.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import newera.myapplication.image.processing.shaders.Shader;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap[][] bitmap;
    private Bitmap[][] originalBitmap;
    private int rows, lines;

    /**
     * @return bitmap's reference
     */
    public Bitmap getBitmap(int x, int y){
        return bitmap[x][y];
    }

    public Bitmap[][] getBitmaps()
    {
        return this.bitmap;
    }


    /**
     * @param rows number of bitmaps in width
     * @param lines number of bitmaps in height
     *       Initialize the array of Bitmaps
     */
    public void setDim(int rows, int lines){
        this.rows = rows;
        this.lines = lines;
        this.bitmap = new Bitmap[rows][lines];
        for(int x = 0; x < rows; ++x) {
            for (int y = 0; y < lines; ++y) {
                bitmap[x][y] = null;
            }
        }
    }

    /**
     * @return Bitmap : The full sized bitmap.
     */
    public Bitmap getBitmap(){
        int nWidth = this.getWidth();
        int nHeight = this.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(nWidth, nHeight, bitmap[0][0].getConfig());
        Canvas canvas = new Canvas(newBitmap);

        drawOriginalBitmap(canvas);
        return newBitmap;
    }

    /**
     * @param bitmap
     * @param x
     * @param y
     *
     * This method add, by copy, a bitmap in a certain location (x;y) in the array of Bitmap.
     */
    public void addBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.bitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    /**
     * @param canvas
     * @param paint
     * @param coordX
     * @param coordY
     * @param scale
     * Draw the array of Bitmap in the canvas.
     */
    public void draw(Canvas canvas, Paint paint, int coordX, int coordY, float scale){
        Rect dst = new Rect();

        int cx = (coordX - (int)((this.getWidth() - 1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight() - 1) * (scale/2)));
        //Log.i("", "coordX = " + coordX + "| coordY = " + coordY + "| cx = " + cx + " | cy = " + cy);

        for(int x = 0; x < this.getTileW(); ++x) {
            for (int y = 0; y < this.getTileH(); ++y) {

                dst.left   = cx + x*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));
                dst.top    = cy + y*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));

                dst.right  = dst.left + (int)((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)((this.getHeight(x,y))*(scale));

                //Log.i("DRAW", "rect= x("+ dst.left+","+dst.right+"), y("+dst.top+","+dst.bottom+"), bitmap : w="+this.getWidth(x,y)+", h="+this.getHeight(x,y));

                canvas.drawBitmap(this.getBitmap(x, y), null, dst, paint);
            }
        }
    }

    /**
     * @param canvas
     * Draw the full sized bitmap in the canvas.
     */
    public void drawOriginalBitmap(Canvas canvas){
        Rect dst = new Rect();

        for(int x = 0; x < this.getTileW(); ++x) {
            for (int y = 0; y < this.getTileH(); ++y) {
                dst.left   = x * PictureFileManager.DECODE_TILE_SIZE;
                dst.top    = y * PictureFileManager.DECODE_TILE_SIZE;

                dst.right  = dst.left + this.getWidth(x, y);
                dst.bottom = dst.top + this.getHeight(x, y);

                canvas.drawBitmap(this.getBitmap(x, y), null, dst, null);
            }
        }
    }

    /**
     * @param bitmap
     * @param x
     * @param y
     * Initialize a copy of the first Bitmap loaded as a save.
     */
    public void initOriginalBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.originalBitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public void initDimOriginalBitmap(int w, int h){
        this.rows = w;
        this.lines = h;
        this.originalBitmap = new Bitmap[w][h];
        for(int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                originalBitmap[x][y] = null;
            }
        }
    }

    public void reinitializeBitmap(){
        for(int x = 0; x < rows; x++) {
            for (int y = 0; y < lines; y++) {
                this.bitmap[x][y] = this.originalBitmap[x][y].copy(
                        this.originalBitmap[x][y].getConfig(),
                        this.originalBitmap[x][y].isMutable()
                );
            }
        }
    }

    public void applyShader(Shader shader)
    {
        shader.ApplyFilter(this);
    }


    public boolean isEmpty(){
        return bitmap == null;
    }

    public int getWidth(){
        int bmp_width = 0;
        for (int x = 0; x < rows; ++x) {
            bmp_width += bitmap[x][0].getWidth();
        }
        return bmp_width;
    }

    public int getHeight() {
        int bmp_height = 0;
        for (int y = 0; y < lines; ++y) {
            bmp_height += bitmap[0][y].getHeight();
        }
        return bmp_height;
    }

    /**
     * @param x
     * @param y
     * @return The width of a bitmap at the location (x;y) in the array
     */
    public int getWidth(int x, int y){
        return bitmap[x][y].getWidth();
    }

    /**
     * @param x
     * @param y
     * @return The Height of a bitmap at the location (x;y) in the array
     */
    public int getHeight(int x, int y) {
        return bitmap[x][y].getHeight();
    }

    /**
     * @return The number of Bitmap in width
     */
    public int getTileW() {
        return rows;
    }

    /**
     * @return The number of Bitmap in height
     */
    public int getTileH() {
        return lines;
    }

    public void saveInBundle(Bundle bundle)
    {
        bundle.putInt("Iw", rows);
        bundle.putInt("Ih", lines);
        for (int j = 0; j < rows; j++)
            for (int i = 0; i < lines; i++)
            {
                bundle.putParcelable("Ibitmap." + j + "." + i, bitmap[j][i]);
                bundle.putParcelable("IoriginalBitmap." + j + "." + i, originalBitmap[j][i]);
            }
    }

    public void loadFromBundle(Bundle bundle)
    {
        rows = bundle.getInt("Iw");
        lines = bundle.getInt("Ih");
        bitmap = new Bitmap[rows][lines];
        originalBitmap = new Bitmap[rows][lines];
        for (int j = 0; j < rows; j++)
            for (int i = 0; i < lines; i++)
            {
                bitmap[j][i] = bundle.getParcelable("Ibitmap." + j + "." + i);
                originalBitmap[j][i] = bundle.getParcelable("IoriginalBitmap." + j + "." + i);
            }
    }
}
