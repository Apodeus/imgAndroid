package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import newera.myapplication.image.Image;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class CImageView extends View {
    private Image image;
    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        image = null;
    }

    @Override
    public void onDraw(Canvas canvas){
        if (image == null || !image.isEmpty()){
            canvas.drawColor(Color.rgb(51,51,51));
        } else {
            canvas.drawBitmap(image.getBitmap(), 0, 0, null);
        }
    }


}
