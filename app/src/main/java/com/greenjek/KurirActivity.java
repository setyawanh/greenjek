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

public class KurirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurir);
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
                intent.putExtra("title", "Bantuan Order Kurir");
                intent.putExtra("body", "Blablabla");
                intent.putExtra("back", "kurir");
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    public void openWhatsApp(View view) {
        String contact = "6285228575555";
        EditText txtJenis   = (EditText)findViewById(R.id.txtJenis);
        EditText txtAlamatAmbil   = (EditText)findViewById(R.id.txtAlamatAmbil);
        EditText txtPengirim   = (EditText)findViewById(R.id.txtPengirim);
        EditText txtHPPengirim   = (EditText)findViewById(R.id.txtHPPengirim);
        EditText txtAlamatAntar   = (EditText)findViewById(R.id.txtAlamatAntar);
        EditText txtPenerima   = (EditText)findViewById(R.id.txtPenerima);
        EditText txtHPPenerima   = (EditText)findViewById(R.id.txtHPPenerima);
        EditText txtKeterangan   = (EditText)findViewById(R.id.txtKeterangan);
        EditText[] inputs = {txtJenis,txtAlamatAmbil,txtPengirim,txtHPPengirim,txtAlamatAntar,txtPenerima,txtHPPenerima};
        Action act = new Action();

        if(act.validate(inputs)) {
            String message =
                    "Order Kurir"+ "\n\n" +
                            "Jenis Barang : " + txtJenis.getText().toString() + "\n\n" +
                            "Alamat Ambil : " + txtAlamatAmbil.getText().toString() + "\n" +
                            "Nama Pengirim : " + txtPengirim.getText().toString() + "\n" +
                            "Nomor HP Pengirim : " + txtHPPengirim.getText().toString() + "\n\n" +
                            "Alamat Antar : " + txtAlamatAntar.getText().toString() + "\n" +
                            "Nama Penerima : " + txtPenerima.getText().toString() + "\n" +
                            "Nomor HP Penerima : " + txtHPPenerima.getText().toString() + "\n\n" +
                            "Keterangan : " + txtKeterangan.getText().toString();

            act.sendWhatsapp(getApplicationContext(), contact, message);
        }
    }
}
