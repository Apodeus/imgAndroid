package newera.EliJ.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
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
    private int w, h, fw, fh;
    private int angle = 0;
    private static Uri origUri;



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
    public void setDim(int rows, int lines, int w, int h){
        this.w = rows;
        this.h = lines;
        this.fw = w;
        this.fh = h;
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
        int alpha = angle;
        rotateBitmaps(alpha);

        int nWidth = this.getWidth();
        int nHeight = this.getHeight();

        Log.i("DBG", "Save image : "+nWidth+"x"+nHeight);

        Bitmap newBitmap = Bitmap.createBitmap(nWidth, nHeight, bitmap[0][0].getConfig());
        Canvas canvas = new Canvas(newBitmap);

        drawOrientedBitmap(canvas, alpha);
        rotateBitmaps(-1 * alpha);
        //draw(canvas, new Paint(), nWidth/2, nHeight/2, 1);

        return newBitmap;
    }

    /**
     * Copy a bitmap at given coordinates in the tileset.
     * @param bitmap Bitmap to copy in the tileset
     * @param x Coordinate x
     * @param y Coordinate y
     */
    public void addBitmap(Bitmap bitmap, int x, int y){
        this.bitmap[x][y] = bitmap;
    }


    /**
     * Draw the whole tileset of Bitmap in the canvas.
     * @param canvas Canvas to draw in
     * @param paint Canvas's paint
     * @param coordX Coordinate X
     * @param coordY Coordinate Y
     * @param scale Scale to draw on
     */
    /*
    public void draw(Canvas canvas, Paint paint, int coordX, int coordY, float scale){
        Rect dst = new Rect();

        int cx = (coordX - (int)((this.getWidth() - 1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight() - 1) * (scale/2)));
        int lastW = 0, lastH = 0;
        for(int x = 0; x < this.getTileW(); ++x) {
            lastH = 0;
            for (int y = 0; y < this.getTileH(); ++y) {
                dst.left   = cx + (int)(lastW*(scale));//x*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));
                dst.top    = cy + (int)(lastH*(scale));//y*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));

                dst.right  = dst.left + (int)Math.ceil((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)Math.ceil((this.getHeight(x,y))*(scale));

                lastH += this.getHeight(x,y);

                /*canvas.save();
                canvas.rotate(this.angle, dst.left, dst.top);
                canvas.drawBitmap(this.getBitmap(x, y), null, dst, paint);
                //canvas.restore();
            }
            lastW += this.getWidth(x,0);
        }
    }
    */

    public void draw(Canvas canvas, Paint paint, int coordX, int coordY, float scale){
        Rect dst = new Rect();

        int cx = (coordX - (int)((this.getWidth() - 1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight() - 1) * (scale/2)));
        //int lastW = 0, lastH = 0;
        for(int x = 0; x < this.getTileW(); ++x) {
            //lastH = 0;
            for (int y = 0; y < this.getTileH(); ++y) {
                dst.left   = cx + x*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));
                dst.top    = cy + y*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));

                dst.right  = dst.left + (int)((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)((this.getHeight(x,y))*(scale));

                //lastH += this.getHeight(x,y);

                canvas.save();
                canvas.rotate(this.angle, dst.left, dst.top);
                canvas.drawBitmap(this.getBitmap(x, y), null, dst, paint);
                canvas.restore();
            }
            //lastW += this.getWidth(x,0);
        }
    }



    public void setOrig(Uri uri){
        this.origUri = uri;
    }

    /**
     * Reset edited bitmap at original's state.
     */
    public void reinitializeBitmap(){
        PictureFileManager.loadFromUri(origUri);
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
        return fw;
    }

    /**
     * @return Height of the full-sized bitmap.
     */
    public int getHeight() {
        return fh;
    }

    /**
     * Force a full free on all Image's bitmaps.
     */
    public void recycleBitmaps() {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
            {
                bitmap[i][j].recycle();
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

    private void drawOrientedBitmap(Canvas canvas, int alpha){
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
            this.angle = this.angle % 360;

            int tmp = this.w;
            this.w = this.h;
            this.h = tmp;

            tmp = fw;
            fw = fh;
            fh = tmp;

            bitmap = rotateArrayBitmap(bitmap, a);
        }
    }

    public int getAngle(){return this.angle;}

}











