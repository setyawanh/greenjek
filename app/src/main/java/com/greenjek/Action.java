package com.greenjek;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Action {

    public void sendWhatsapp(Context cntx, String contact, String message) {
        boolean isWhatsappInstalled = this.whatsappInstalledOrNot(cntx, "com.whatsapp");

        if (isWhatsappInstalled) {
            String apiUri = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + message;
            Intent sendMessage = new Intent("android.intent.action.VIEW", Uri.parse(apiUri));
            sendMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cntx.startActivity(sendMessage);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(cntx, "WhatsApp tidak terinstall",Toast.LENGTH_LONG).show();
            cntx.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(Context cntx, String uri) {
        PackageManager pm = cntx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public boolean validate(EditText[] inputs) {
        boolean valid = true;
        for(TextView input: inputs) {
            if (input.getText().toString().length() == 0) {
                input.setError("Harus diisi!");
                valid = false;
            }
        }

        return valid;
    }

    public boolean validateRadio(RadioButton[] inputs) {
        boolean valid = false;
        for(RadioButton input: inputs) {
            if (input.isChecked()) {
                valid = true;
                break;
            } else {
                input.setError("Pilih salah satu!");
            }
        }


        return valid;
    }
}
