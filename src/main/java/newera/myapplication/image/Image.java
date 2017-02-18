package newera.myapplication.image;

import android.graphics.Bitmap;
import newera.myapplication.image.processing.shaders.Shader;

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

    public void applyShader(Shader shader)
    {
        shader.ApplyFilter(this);
    }

    public boolean isEmpty(){
        return bitmap == null;
    }

    public int getWidth(){
        int w = 0;
        for (int x = 0; x < w; ++x) {
            w += bitmap[x][0].getWidth();
        }
        return w;
    }

    public int getHeight() {
        int h = 0;
        for (int y = 0; y < h; ++y) {
            h += bitmap[0][y].getHeight();
        }
        return h;
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
