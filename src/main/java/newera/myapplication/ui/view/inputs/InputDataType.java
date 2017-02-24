package newera.myapplication.ui.view.inputs;

/**
 * Created by echo on 24/02/2017.
 */
public class InputDataType {
    private EInputType inputType;
    private int[] settings;
    private String userLabel;
    private String dataLabel;

    public InputDataType()
    {

    }

    public InputDataType(EInputType inputType, String dataLabel, String userLabel, int[] settings)
    {
        this.inputType = inputType;
        this.dataLabel = dataLabel;
        this.userLabel = userLabel;
        this.settings = settings;
    }

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

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }
}
