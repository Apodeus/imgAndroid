package newera.myapplication.ui.view.inputs;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Map;

/**
 * Created by echo on 22/02/2017.
 */
public interface IInputBox {
    int getValue();
    void drawBox(Canvas canvas);
    boolean handleTouch(MotionEvent event);
    void setLabel(String label);
    Map<String, Object> getParams();
}
