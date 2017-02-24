package newera.myapplication.ui.view.inputs;

/**
 * Created by echo on 24/02/2017.
 */
public class InputDataType {
    private EInputType inputType;
    private int[] settings;
    private String label;

    public EInputType getInputType() {
        return inputType;
    }

    public void setInputType(EInputType inputType) {
        this.inputType = inputType;
    }

    public int[] getSettings() {
        return settings;
    }

    public void setSettings(int[] settings) {
        this.settings = settings;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
