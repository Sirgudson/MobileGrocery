package com.example.appfirebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private ImageButton imageButtonVoltar;

    private String usuarioId;

    private EditText edit_nome, edit_email, edit_senha, edit_senha2;

    private AppCompatButton bt_cadastrar;

    private String [] msgs={"Preencha todos os campos", "Cadastro realizado com sucesso!", "Senhas não são iguais"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        this.iniciarComponentes();
        this.voltarLogin();
        this.gerenciaCadastro();
    } //fim onCreate

    public void voltarLogin(){
        this.imageButtonVoltar= findViewById(R.id.ButtonBackCadastro);
        imageButtonVoltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent= new Intent(FormCadastro.this, FormLogin.class);
                startActivity(intent);
            }
        });
    } //fim voltarLogin

    private void iniciarComponentes(){
        edit_nome= findViewById(R.id.editTextNomeCadastro);
        edit_email= findViewById(R.id.editTextEmailCadastro);
        edit_senha= findViewById(R.id.editTextSenhaCadastro);
        edit_senha2= findViewById(R.id.editTextSenha2Cadastro);
        bt_cadastrar=findViewById(R.id.bt_cadastrarCadastro);

    } //fim iniciarComponenentews


    public void gerenciaCadastro(){
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome=edit_nome.getText().toString();
                String email=edit_email.getText().toString();
                String senha=edit_senha.getText().toString();
                String senha2=edit_senha2.getText().toString();
                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty()){
                    @SuppressLint("ShowToast") Snackbar objSnackbar= Snackbar.make(view, msgs[0], Snackbar.LENGTH_SHORT);
                    objSnackbar.setBackgroundTint(Color.WHITE);
                    objSnackbar.setTextColor(Color.BLACK);
                    objSnackbar.show();
                } //fim if
                else if(!senha.equals(senha2)) {
                    @SuppressLint("ShowToast") Snackbar objSnackbar= Snackbar.make(view, msgs[2], Snackbar.LENGTH_SHORT);
                    objSnackbar.setBackgroundTint(Color.WHITE);
                    objSnackbar.setTextColor(Color.BLACK);
                    objSnackbar.show();
                } //fim else if
                else{
                    cadastrarUser(view, email, senha);
                } //fim else
            }
        });
    } //fim gerenciaUsuario

    public void cadastrarUser(View view,String email,String senha){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener
                (new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){//DEU CERTO?
                            salvarDadosUsuario();
                            Snackbar objSnackbar = Snackbar.make(view, msgs[1], Snackbar.LENGTH_SHORT);
                            objSnackbar.setBackgroundTint(Color.WHITE);
                            objSnackbar.setTextColor(Color.BLACK);
                            objSnackbar.show();
                            new Handler().postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent objIntent = new Intent(FormCadastro.this, FormLogin.class);
                                            startActivity(objIntent);
                                            finish();
                                        }//fim run
                                    }, 3000);
                        }//fim if
                        else{//deu ruim
                            String erro= "";
                            try {
                                throw task.getException();
                            }//fim try
                            catch (FirebaseAuthWeakPasswordException e){
                                erro="Digite uma senha com 6 caracteres ou mais!";
                            }//fim catch
                            catch(FirebaseAuthUserCollisionException e){
                                erro="Já existe uma conta vinculada ao email!";
                            }//fim catch
                            catch(FirebaseAuthInvalidCredentialsException e){
                                erro="Email invalido!";
                            }//fim catch
                            catch(Exception e){
                                erro="Erro ao cadastrar usuario!";
                            }//fim catch
                            Snackbar objSnackbar= Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                            objSnackbar.setBackgroundTint(Color.WHITE);
                            objSnackbar.setTextColor(Color.BLACK);
                            objSnackbar.show();
                        }//fim else
                    }// fim onComplete
                });//fim onCompleteListener
    }//fim cadastraUser

    public void salvarDadosUsuario(){
        String nome= edit_nome.getText().toString();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Map<String, Object> usuario= new HashMap<>();
        usuario.put("nome", nome);
        usuarioId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference= db.collection("Usuarios").document(usuarioId);
        documentReference.set(usuario).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("d", "Sucesso ao salvar dados");
                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", "Erro ao salvar dados" + e.getMessage());
                    }
                }
        );
    }//fim salvarDadosUsuario()
}
