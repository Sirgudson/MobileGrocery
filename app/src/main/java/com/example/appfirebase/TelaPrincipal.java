package com.example.appfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {
private Button br_deslogar;
private TextView nomeUsuario, emailUsuario;
protected String usuarioID;
protected FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        deslogar();
        this.iniciarComponentes();
    }//fim oncreate

    protected void onStart(){
        super.onStart();
        usuarioID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference obj= db.collection("Usuarios").document(usuarioID);
        obj.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value !=null)
                {
                    nomeUsuario.setText(value.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    private void iniciarComponentes(){
        nomeUsuario=findViewById(R.id.TextViewNomeUsuarioPrincipal);
        emailUsuario=findViewById(R.id.TextViewEmailUsuarioPrincipal);
        db=FirebaseFirestore.getInstance();
    }//fim iniciarComponentes

    private void deslogar(){
        br_deslogar= findViewById(R.id.buttonDeslogar);
        br_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent objIntent=new Intent(TelaPrincipal.this, FormLogin.class);
                startActivity(objIntent);
                finish();
            }
        });
    } //fim deslogar
}//fim classe