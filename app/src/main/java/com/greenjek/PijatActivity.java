package com.greenjek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PijatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pijat);
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
                intent.putExtra("title", "Bantuan Order Pijat");
                intent.putExtra("body", "Blablabla");
                intent.putExtra("back", "pijat");
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void openWhatsApp(View view) {
        String contact = "6285228575555";
        EditText txtNama   = (EditText)findViewById(R.id.txtNama);
        EditText txtHP   = (EditText)findViewById(R.id.txtHP);
        EditText txtAlamat   = (EditText)findViewById(R.id.txtAlamat);
        EditText txtJam   = (EditText)findViewById(R.id.txtJam);
        RadioButton radioPria = (RadioButton)findViewById(R.id.radioPria);
        RadioButton radioWanita = (RadioButton)findViewById(R.id.radioWanita);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioSex);
        EditText[] inputs = {txtNama,txtHP,txtAlamat,txtJam};
        int select = radioGroup.getCheckedRadioButtonId();
        Action act = new Action();

        if(act.validate(inputs) && select != -1) {
            RadioButton terapis = (RadioButton) findViewById(select);
            radioWanita.setError(null);

            String message =
                    "Order Pijat"+ "\n\n" +
                            "Nama : " + txtNama.getText().toString() + "\n" +
                            "HP : " + txtHP.getText().toString() + "\n" +
                            "Alamat : " + txtAlamat.getText().toString() + "\n\n" +
                            "Terapis : " + terapis.getText() + "\n\n" +
                            "Jam Terapi : " + txtJam.getText().toString();

            act.sendWhatsapp(getApplicationContext(), contact, message);
        } else {
            radioWanita.setError("eror");
        }
    }
}
