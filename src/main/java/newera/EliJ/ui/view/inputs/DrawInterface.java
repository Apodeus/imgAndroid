package newera.EliJ.ui.view.inputs;

import android.graphics.Canvas;
import android.view.MotionEvent;
import newera.EliJ.ui.view.inputs.components.IGenericBoxComponent;

/**
 * Created by echo on 14/04/2017.
 */
public class DrawInterface implements IGenericBoxComponent {
    public DrawInterface(GenericBox genericBox) {
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setStartingHeight(int height) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void initialize(int[] settings) {

    }

    @Override
    public void setIndex(int index) {

    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void enableEdit(MotionEvent event) {

    }

    @Override
    public void disableEdit() {

    }

    @Override
    public void handleEdit(MotionEvent event) {

    }
}
