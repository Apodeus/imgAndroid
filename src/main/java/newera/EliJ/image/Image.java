package newera.EliJ.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import newera.EliJ.ui.system.PictureFileManager;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap[][] bitmap;
    private Bitmap[][] originalBitmap;
    private int w, h;
    private int angle = 0;

    /**
     * @return Edited tile reference
     */
    public Bitmap getBitmap(int x, int y){
        return bitmap[x][y];
    }

    /**
     * @return Tileset of edited bitmap
     */
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
        this.w = rows;
        this.h = lines;
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
    public Bitmap getBitmap(int alpha){
        rotateBitmaps(alpha);

        int nWidth = this.getWidth();
        int nHeight = this.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(nWidth, nHeight, bitmap[0][0].getConfig());
        Canvas canvas = new Canvas(newBitmap);

        drawOriginalBitmap(canvas, alpha);
        rotateBitmaps(-1 * alpha);

        return newBitmap;
    }

    /**
     * Copy a bitmap at given coordinates in the tileset.
     * @param bitmap Bitmap to copy in the tileset
     * @param x Coordinate x
     * @param y Coordinate y
     */
    public void addBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.bitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    /**
     * Draw the whole tileset of Bitmap in the canvas.
     * @param canvas Canvas to draw in
     * @param paint Canvas's paint
     * @param coordX Coordinate X
     * @param coordY Coordinate Y
     * @param scale Scale to draw on
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

                canvas.save();
                canvas.rotate(this.angle, dst.left, dst.top);
                canvas.drawBitmap(this.getBitmap(x, y), null, dst, paint);
                canvas.restore();
            }
        }
    }

    /**
     * Store a copy of original bitmap on given coordinates.
     * @param bitmap Original tile
     * @param x Coordinate x
     * @param y Coordinate y
     */
    public void initOriginalBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.originalBitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    /**
     * Generate an empty tileset for original bitmap's storage.
     * @param w Number of tiles on w axis
     * @param h Number of tiles on h axis
     */
    public void initDimOriginalBitmap(int w, int h){
        this.w = w;
        this.h = h;
        this.originalBitmap = new Bitmap[w][h];
        for(int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                originalBitmap[x][y] = null;
            }
        }
    }

    /**
     * Reset edited bitmap at original's state.
     */
    public void reinitializeBitmap(){
        for(int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                this.bitmap[x][y] = this.originalBitmap[x][y].copy(
                        this.originalBitmap[x][y].getConfig(),
                        this.originalBitmap[x][y].isMutable()
                );
            }
        }
    }

    /**
     * @return True if no bitmap is set.
     */
    public boolean isEmpty(){
        return bitmap == null;
    }

    /**
     * @return Width of the full-sized bitmap.
     */
    public int getWidth(){
        int bmp_width = 0;
        for (int x = 0; x < w; ++x) {
            bmp_width += bitmap[x][0].getWidth();
        }
        return bmp_width;
    }

    /**
     * @return Height of the full-sized bitmap.
     */
    public int getHeight() {
        int bmp_height = 0;
        for (int y = 0; y < h; ++y) {
            bmp_height += bitmap[0][y].getHeight();
        }
        return bmp_height;
    }

    /**
     * Force a full free on all Image's bitmaps.
     */
    public void recycleBitmaps() {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
            {
                bitmap[i][j].recycle();
                originalBitmap[i][j].recycle();
            }
    }

    private int getWidth(int x, int y){
        return bitmap[x][y].getWidth();
    }

    private int getHeight(int x, int y) {
        return bitmap[x][y].getHeight();
    }

    private int getTileW() {
        return w;
    }

    private int getTileH() {
        return h;
    }

    private void drawOriginalBitmap(Canvas canvas, int alpha){
        Rect dst = new Rect();
        int shiftW = 0;
        int shiftH = 0;

        for(int x = 0; x < this.getTileW(); ++x) {
            int lastShift = 0;
            for (int y = 0; y < this.getTileH(); ++y) {
                dst.left = shiftW;
                dst.top = shiftH;

                shiftH += this.getHeight(x, y);
                lastShift = this.getWidth(x, y);


                dst.right  = dst.left + this.getWidth(x, y);
                dst.bottom = dst.top + this.getHeight(x, y);
                canvas.drawBitmap(this.getBitmap(x, y), null, dst, null);

            }
            shiftH = 0;
            shiftW += lastShift;
        }
    }

    private void rotateBitmaps(int angle){


        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){

                Bitmap tmp = this.getBitmap(x, y);
                bitmap[x][y] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
                tmp.recycle();

                tmp = this.originalBitmap[x][y];
                originalBitmap[x][y] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
                tmp.recycle();

            }
        }

    }

    private Bitmap[][] rotateArrayBitmap(Bitmap[][] bitmaps, int alpha){
        Bitmap[][] arrBmp = new Bitmap[w][h];

        for(int x = 0; x < w; x++){
            for(int y = 0; y < h; y++){
                if(alpha > 0)
                    arrBmp[x][y] = bitmaps[y][abs(x - w + 1)];
                if(alpha < 0)
                    arrBmp[x][y] = bitmaps[abs(y - h + 1)][x];
            }
        }

        return arrBmp;
    }

    public void setAngle(int a){
        if(!this.isEmpty()) {

            this.angle += a;
            //this.angle = this.angle % 360;

            int tmp = this.w;
            this.w = this.h;
            this.h = tmp;

            bitmap = rotateArrayBitmap(bitmap, a);
            originalBitmap = rotateArrayBitmap(originalBitmap, a);
        }
    }

    public int getAngle(){return this.angle;}


}











