package com.example.joao__us9xpst.livro_facil.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.joao__us9xpst.livro_facil.Model.Usuario;
import com.example.joao__us9xpst.livro_facil.R;
import com.example.joao__us9xpst.livro_facil.Util.UserUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity {
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener ;
    Usuario User = new Usuario();
    Usuario UserLoginFacebook = new Usuario();

    private ProgressBar pb_facebbok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        pb_facebbok = (ProgressBar) findViewById(R.id.pb_facebookLogin);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if ( task.isSuccessful() ) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        UserLoginFacebook.setNome(firebaseUser.getDisplayName());
                        UserLoginFacebook.setId(firebaseUser.getUid());
                        UserLoginFacebook.setEmail(firebaseUser.getEmail());
                        UserLoginFacebook.setTipo(0);
                        try{
                            UserLoginFacebook.setPhotoURL(firebaseUser.getPhotoUrl().toString());
                        }catch (NullPointerException e){
                            System.out.print(e.getMessage());
                        }
                        verificarExistenciaUsuario(firebaseUser.getUid());
                    }
                    else{
                        LoginManager.getInstance().logOut();

                        Toast.makeText(FacebookLogin.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        });


    }

    private void verificarExistenciaUsuario(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Usuario");
        final DatabaseReference userRef = myRef.child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User = dataSnapshot.getValue(Usuario.class);
                    if (User.getCellNumber() == null || User.getCellNumber().isEmpty()) {
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(FacebookLogin.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(FacebookLogin.this);
                        alertDialogBuilderUserInput.setView(mView);

                        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                        alertDialogBuilderUserInput
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        goMainScreen();
                                    }
                                })

                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                dialogBox.cancel();
                                                LoginManager.getInstance().logOut();
                                            }
                                        });

                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                    } else {
                        goMainScreen();
                    }
                } else {
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(FacebookLogin.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);

                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(FacebookLogin.this);
                    alertDialogBuilderUserInput.setView(mView);

                    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    UserLoginFacebook.setCellNumber(userInputDialogEditText.getText().toString());
                                    User = UserLoginFacebook;
                                    userRef.setValue(UserLoginFacebook.toMap());
                                    goMainScreen();
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                            LoginManager.getInstance().logOut();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void sendLoginFacebookData(android.view.View v ){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        pb_facebbok.setVisibility(android.view.View.VISIBLE);
    }

    private void goMainScreen() {
        UserUtil.setUser(User);
        Intent intent = new Intent(this, NavigationDrawer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User",User);
        startActivity(intent);
    }

}
