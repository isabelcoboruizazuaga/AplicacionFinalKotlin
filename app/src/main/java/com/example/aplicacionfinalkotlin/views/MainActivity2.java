package com.example.aplicacionfinalkotlin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aplicacionfinalkotlin.R;

public class MainActivity2 extends AppCompatActivity {

    ProgressBar pbarProgreso;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pDialog = new ProgressDialog(MainActivity2.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);

        MiTareaAsincronaDialog tarea2 = new MiTareaAsincronaDialog();
        tarea2.execute();


    }
    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer,
            Boolean> {
        private void tareaLarga()
        {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i=1; i<=10; i++) {
                tareaLarga();
                publishProgress(i*10);
                if(isCancelled())
                    break;
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }
        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MiTareaAsincronaDialog.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(MainActivity2.this, "Tarea finalizada!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity2.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}