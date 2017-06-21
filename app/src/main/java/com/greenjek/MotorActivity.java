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
import android.widget.Toast;

public class MotorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);
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
                intent.putExtra("title", "Bantuan Order Motor");
                intent.putExtra("body", "<h2>Hello world</h2><ul><li>cats</li><li>dogs</li></ul>");
                intent.putExtra("back", "motor");
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void openWhatsApp(View view) {
        String contact = "6285601435555";
        EditText txtNama   = (EditText)findViewById(R.id.txtJenis);
        EditText txtHP   = (EditText)findViewById(R.id.txtHP);
        EditText txtDari   = (EditText)findViewById(R.id.txtDari);
        EditText txtTujuan   = (EditText)findViewById(R.id.txtTujuan);
        EditText txtJam   = (EditText)findViewById(R.id.txtJam);
        EditText[] inputs = {txtNama,txtHP,txtDari,txtTujuan,txtJam};
        Action act = new Action();
        if(act.validate(inputs)) {
            String message =
                    "Order Ojek"+ "\n\n" +
                            "Nama : " + txtNama.getText().toString() + "\n" +
                            "HP : " + txtHP.getText().toString() + "\n\n" +
                            "Dari : " + txtDari.getText().toString() + "\n\n" +
                            "Tujuan : " + txtTujuan.getText().toString()+ "\n\n"+
                            "Jam : " + txtJam.getText().toString();

            act.sendWhatsapp(getApplicationContext(), contact, message);
        }
    }
}
