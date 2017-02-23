package newera.myapplication.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import newera.myapplication.R;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.shaders.Shader;

/**
 * Created by Emile Barjou-Suire on 19/02/2017.
 */

public class ShaderDialogBox extends DialogFragment{
    private Shader shader;
    private String name;
    private Image image;
    private Context context;

    public void setOnClick(Shader shader){
        this.shader = shader;
        this.name = shader.getName();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.name)
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        shader.ApplyFilter(image);
                   }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImage(Image image){
        this.image = image;
    }
}
