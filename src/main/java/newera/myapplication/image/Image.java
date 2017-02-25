package newera.myapplication.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import newera.myapplication.image.processing.shaders.Shader;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap[][] bitmap;
    private Bitmap[][] originalBitmap;
    private int w, h;
    private static boolean isInitialized = false;

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
        if(isInitialized == false){
            this.originalBitmap = new Bitmap[w][h];
        }
        for(int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                bitmap[x][y] = null;
                if(isInitialized == false){
                    originalBitmap[x][y] = null;
                }
            }
        }
    }

    public void finished(){
        this.isInitialized = true;
    }

    public Bitmap getBitmap(){
        int nW = this.getWidth();
        int nH = this.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(nW, nH, bitmap[0][0].getConfig());
        Canvas canvas = new Canvas(newBitmap);
        //rework the following method for this call.
        draw(canvas, (int)((this.getWidth()-1) * (1f/2)), (int)((this.getHeight()-1) * (1f/2)), 1f);
        return newBitmap;
    }

    public void addBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        //this.bitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        this.bitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        if(isInitialized == false){
            this.originalBitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        }
    }

    public void reinitializeBitmap(){
        this.bitmap = originalBitmap.clone();
    }

    public void draw(Canvas canvas, int coordX, int coordY, float scale){
        Rect src = new Rect();
        Rect dst = new Rect();
        int cx = (coordX - (int)((this.getWidth()-1) * (scale/2)));
        int cy = (coordY - (int)((this.getHeight()-1) * (scale/2)));
        for(int y = 0; y < this.getTileH(); ++y) {
            for (int x = 0; x < this.getTileW(); ++x) {
                src.set(0, 0, this.getWidth(x,y)-1, this.getHeight(x,y)-1);
                dst.left   = cx + x*(int)((PictureFileManager.DECODE_TILE_SIZE-1.0)*(scale));
                dst.top    = cy + y*(int)((PictureFileManager.DECODE_TILE_SIZE-1.0)*(scale));
                dst.right  = dst.left + (int)((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)((this.getHeight(x,y))*(scale));
                canvas.drawBitmap(this.getBitmap(x, y), src, dst, null);
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
}
