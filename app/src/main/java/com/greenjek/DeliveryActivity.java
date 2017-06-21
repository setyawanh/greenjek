package com.greenjek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DeliveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                break;
            case R.id.action_help:
                //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,HelpActivity.class);
                intent.putExtra("title", "Bantuan Delivery Order");
                intent.putExtra("body", "Blablabla");
                intent.putExtra("back", "delivery");
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void openWhatsApp(View view) {
        String contact = "6285228575555";
        EditText txtNama   = (EditText)findViewById(R.id.txtJenis);
        EditText txtHP   = (EditText)findViewById(R.id.txtHP);
        EditText txtAlamat   = (EditText)findViewById(R.id.txtAlamat);
        EditText txtMenu   = (EditText)findViewById(R.id.txtMenu);
        EditText txtOutlet   = (EditText)findViewById(R.id.txtOutlet);
        EditText[] inputs = {txtNama,txtHP,txtAlamat,txtMenu,txtOutlet};
        Action act = new Action();

        if(act.validate(inputs)) {
            String message =
                    "Delivery Order"+ "\n\n" +
                            "Nama : " + txtNama.getText().toString() + "\n" +
                            "HP : " + txtHP.getText().toString() + "\n" +
                            "Alamat : " + txtAlamat.getText().toString() + "\n\n" +
                            "Menu/Barang : " + txtMenu.getText().toString()+ "\n\n"+
                            "Outlet : " + txtOutlet.getText().toString();

            act.sendWhatsapp(getApplicationContext(), contact, message);
        }
    }
}
