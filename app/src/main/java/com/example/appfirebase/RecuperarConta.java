package com.example.appfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecuperarConta extends AppCompatActivity {

    private EditText recuperarEmail;
    private AppCompatButton bt_recuperar;
    private ImageButton bt_back;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta);
        this.iniciarComponentes();
        this.voltarTelaLogin();
        this.coreRecuperarConta();

    }//fim oncreate

    private void coreRecuperarConta(){
        bt_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarConta();
            }
        });
    }//fim coreRecuperarConta

    private void recuperarConta(){
        String email= recuperarEmail.getText().toString();
        auth= FirebaseAuth.getInstance();
        if(!email.isEmpty()){//usuario preencheu o email? função isEmpty retorna true caso email não esteja preenchido, porém a ! nega o resultado
            //ou seja, se tiver preenchido a função retorna false e ai entra no if,pq pra nos so interessa se o campo email estiver preenchido
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getBaseContext(), "Enviado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                }//fim oncomplet
            }).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            trataErro(e.toString());//trata erro nao existe, temos que ainda implementar
                        }
                    }
            );
        }//fim if deu certo
        else{//o usuario nao preencheu o campo email
            Toast.makeText(getBaseContext(), "Ensira um email!", Toast.LENGTH_SHORT).show();
        }//fim else
    }//fim recuperar senha


    public void trataErro(String erro){
        if(erro.contains("address is badly")){
            Toast.makeText(getBaseContext(), "Email inválido!", Toast.LENGTH_SHORT).show();
        }//email invalido
        else if(erro.contains("there is no user")){
            Toast.makeText(getBaseContext(), "Email não cadastrado!", Toast.LENGTH_SHORT).show();
        }//email nao cadastrado
        else if(erro.contains("INVALID_EMAIL")){
            Toast.makeText(getBaseContext(), "Email inválido!", Toast.LENGTH_SHORT).show();
        }//email nao valido
        else{
            Toast.makeText(getBaseContext(), erro, Toast.LENGTH_SHORT).show();
        }//fim else
    }//fim trataErro()

    private void iniciarComponentes(){
            recuperarEmail= findViewById(R.id.editTextEmailRecuperarConta);
            bt_recuperar= findViewById(R.id.bt_recuperar);
            bt_back= findViewById(R.id.ButtonBackRecuperarConta);
    }//fim iniciarComponenetes

    private void voltarTelaLogin(){
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RecuperarConta.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        }); //fim OnCliclListener
    } //fim voltarTelaLogin
}//fim classe