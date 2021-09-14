package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.runforlove.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private static final String User_Values="User_Datas";
    private static final String User_Pseudo="User_Pseudo";

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Log.e("hide action bar",e.toString());
        }
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        id=sharedPreferences.getString(User_Pseudo,"");

        //Si l'utilisateur n'est pas identifié, il se connecte ou créé un compte
        if(id=="")
        {
            setContentView(R.layout.activity_main);

            Button btnConnexion= (Button) findViewById(R.id.connexion);
            btnConnexion.setOnClickListener(onClickListener);

            Button btnInscription= (Button) findViewById(R.id.inscription);
            btnInscription.setOnClickListener(onClickListener);
        }
        //Sinon il est redirigé vers la page principale
        else
        {
            Intent connexion = new Intent(MainActivity.this, Principal.class);
            startActivity(connexion);
            finish();
        }
    }

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.connexion:
                    Intent connexion = new Intent(MainActivity.this, connexion.class);
                    startActivity(connexion);
                    break;
                case R.id.inscription:
                    Intent inscription = new Intent(MainActivity.this, inscription.class);
                    startActivity(inscription);
                    break;
            }
        }
    };
}
