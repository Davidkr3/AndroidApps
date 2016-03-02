package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by david.cara on 3/2/16.
 */
public class utils {
    public static void makeInfoAlert(String alert, Activity activity)
    {
        //Enviem el missatge dient que 's'ha inserit correctament
        new AlertDialog.Builder(activity) //ens trobem en una funció de un botó, especifiquem la classe (no this)
                //.setTitle("DB")
                .setMessage(alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //no fem res
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
