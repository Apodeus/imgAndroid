package newera.EliJ.ui.view.inputs;

/**
 * Created by echo on 24/02/2017.
 */
class InputDataType {
    private EInputType inputType;
    private int[] settings;
    private String userLabel;
    private String dataLabel;

    /**
     * To be given to a bundle or list for input box generations
     * @param inputType Type of the module
     * @param dataLabel Label to store and retrieve settings
     * @param userLabel Label/Hint for the user
     * @param settings Parameters for module's behavior
     */
    InputDataType(EInputType inputType, String dataLabel, String userLabel, int[] settings) {
        this.inputType = inputType;
        this.dataLabel = dataLabel;
        this.userLabel = userLabel;
        this.settings = settings;
    }

    EInputType getInputType() {
        return inputType;
    }

    int[] getSettings() {
        return settings;
    }

    String getDataLabel() {
        return dataLabel;
    }

    String getUserLabel() {
        return userLabel;
    }

}
