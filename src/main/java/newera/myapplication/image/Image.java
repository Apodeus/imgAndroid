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
    private int w, h;

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

    public void setDim(int w, int h){
        this.w = w;
        this.h = h;
        this.bitmap = new Bitmap[w][h];
        for(int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                bitmap[x][y] = null;
            }
        }
    }


    public Bitmap getBitmap(){
        int nW = this.getWidth();
        int nH = this.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(nW, nH, bitmap[0][0].getConfig());
        Canvas canvas = new Canvas(newBitmap);

        //rework the following method for this call.
        drawOriginalBitmap(canvas, (int)((this.getWidth()-1) * (1f/2)), (int)((this.getHeight()-1) * (1f/2)), 1f);

        return newBitmap;
    }

    public void addBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.bitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public void draw(Canvas canvas, Paint paint, int coordX, int coordY, float scale){
        Rect dst = new Rect();

        int cx = (coordX - (int)((this.getWidth() - 1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight() - 1) * (scale/2)));
        //Log.i("", "coordX = " + coordX + "| coordY = " + coordY + "| cx = " + cx + " | cy = " + cy);

        for(int x = 0; x < this.getTileW(); ++x) {
            for (int y = 0; y < this.getTileH(); ++y) {
                //src.set(0, 0, this.getWidth(x,y) - 1, this.getHeight(x,y) - 1);

                dst.left   = cx + x*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));
                dst.top    = cy + y*(int)((PictureFileManager.DECODE_TILE_SIZE )*(scale));

                dst.right  = dst.left + (int)((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)((this.getHeight(x,y))*(scale));

                //Log.i("DRAW", "rect= x("+ dst.left+","+dst.right+"), y("+dst.top+","+dst.bottom+"), bitmap : w="+this.getWidth(x,y)+", h="+this.getHeight(x,y));

                canvas.drawBitmap(this.getBitmap(x, y), null, dst, paint);

            }
        }
    }

    public void drawOriginalBitmap(Canvas canvas, int coordX, int coordY, float scale){
        //Rect src = new Rect();
        Rect dst = new Rect();
        /*
        int cx = (coordX - (int)((this.getWidth()-1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight()-1) * (scale/2)));
        */
        for(int x = 0; x < this.getTileW(); ++x) {
            for (int y = 0; y < this.getTileH(); ++y) {
                //src.set(0, 0, this.getWidth(x,y) - 1, this.getHeight(x,y) - 1);

                dst.left   = x * PictureFileManager.DECODE_TILE_SIZE;
                dst.top    = y * PictureFileManager.DECODE_TILE_SIZE;

                dst.right  = dst.left + this.getWidth(x, y);
                dst.bottom = dst.top + this.getHeight(x, y);

                canvas.drawBitmap(this.getBitmap(x, y), null, dst, null);
            }
        }
    }

    public void initOriginalBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        this.originalBitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

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

    public void applyShader(Shader shader)
    {
        shader.ApplyFilter(this);
    }


    public boolean isEmpty(){
        return bitmap == null;
    }

    public int getWidth(){
        int bmp_w = 0;
        for (int x = 0; x < w; ++x) {
            bmp_w += bitmap[x][0].getWidth();
        }
        return bmp_w;
    }

    public int getHeight() {
        int bmp_h = 0;
        for (int y = 0; y < h; ++y) {
            bmp_h += bitmap[0][y].getHeight();
        }
        return bmp_h;
    }

    public int getWidth(int x, int y){
        return bitmap[x][y].getWidth();
    }

    public int getHeight(int x, int y) {
        return bitmap[x][y].getHeight();
    }

    public int getTileW() {
        return w;
    }

    public int getTileH() {
        return h;
    }

    public void saveInBundle(Bundle bundle)
    {
        bundle.putInt("Iw", w);
        bundle.putInt("Ih", h);
        for (int j = 0; j < w; j++)
            for (int i = 0; i < h; i++)
            {
                bundle.putParcelable("Ibitmap." + j + "." + i, bitmap[j][i]);
                bundle.putParcelable("IoriginalBitmap." + j + "." + i, originalBitmap[j][i]);
            }
    }

    public void loadFromBundle(Bundle bundle)
    {
        w = bundle.getInt("Iw");
        h = bundle.getInt("Ih");
        bitmap = new Bitmap[w][h];
        originalBitmap = new Bitmap[w][h];
        for (int j = 0; j < w; j++)
            for (int i = 0; i < h; i++)
            {
                bitmap[j][i] = bundle.getParcelable("Ibitmap." + j + "." + i);
                originalBitmap[j][i] = bundle.getParcelable("IoriginalBitmap." + j + "." + i);
            }
    }
}
