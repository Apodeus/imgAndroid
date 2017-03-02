package newera.EliJ.ui.view.inputs.components;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by echo on 24/02/2017.
 */
public interface IGenericBoxComponent {
    int getHeight();
    void setStartingHeight(int height);
    Object getValue();
    void initialize(int[] settings);
    void setIndex(int index);
    void setLabel(String label);
    String getLabel();
    void draw(Canvas canvas);
    void enableEdit(MotionEvent event);
    void disableEdit();
    void handleEdit(MotionEvent event);
}
