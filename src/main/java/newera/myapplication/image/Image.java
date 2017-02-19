package newera.myapplication.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import newera.myapplication.image.processing.shaders.Shader;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap[][] bitmap;
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
        for(int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                bitmap[x][y] = null;
            }
        }
    }

    public void addBitmap(Bitmap bitmap, int x, int y){
        if(bitmap == null)
            return;
        //this.bitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
        this.bitmap[x][y] = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public void draw(Canvas canvas, int coordX, int coordY, float scale){
        Rect src = new Rect();
        Rect dst = new Rect();
        for(int y = 0; y < this.getTileH(); ++y) {
            for (int x = 0; x < this.getTileW(); ++x) {
                src.set(0, 0, this.getWidth(x,y)-1, this.getHeight(x,y)-1);
                dst.left = (coordX - (int)((this.getWidth()-1) * (scale/2))) + (int)(x*(PictureFileManager.DECODE_TILE_SIZE-1)*(scale));
                dst.top = (coordY - (int)((this.getHeight()-1) * (scale/2))) + (int)(y*(PictureFileManager.DECODE_TILE_SIZE-1)*(scale));
                dst.right = dst.left + (int)((this.getWidth(x,y))*(scale));
                dst.bottom = dst.top + (int)((this.getHeight(x,y))*(scale));
                    /*dst.right = contentCoords.x + (int) (image.getWidth() * (contentScale/2));
                    dst.bottom =  contentCoords.y + (int) (image.getHeight() * (contentScale/2));*/
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
