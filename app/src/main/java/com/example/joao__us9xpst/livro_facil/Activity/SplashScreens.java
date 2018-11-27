package com.example.joao__us9xpst.livro_facil.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.example.joao__us9xpst.livro_facil.Model.Usuario;
import com.example.joao__us9xpst.livro_facil.R;
import com.example.joao__us9xpst.livro_facil.Util.UserUtil;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class SplashScreens extends AppCompatActivity {

    private Usuario UsuarioLogado;
    private boolean usuarioDefinido;
    boolean isLoggedIn;
    int millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screens);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Usuario.UsuarioRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserUtil.setUser(dataSnapshot.getValue(Usuario.class));
                    goMainScreen();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SplashScreens.this,"Erro ao realizar login",Toast.LENGTH_LONG).show();
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {

                            LoginManager.getInstance().logOut();

                            Intent intent = new Intent(SplashScreens.this, FacebookLogin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }

                        public void onTick(long millisUntilFinished) {

                        }
                    }.start();
                }
            });

        } else {
            new CountDownTimer(3000, 1000) {
                public void onFinish() {

                    LoginManager.getInstance().logOut();

                    Intent intent = new Intent(SplashScreens.this, FacebookLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

                public void onTick(long millisUntilFinished) {

                }
            }.start();
        }




        UsuarioLogado = new Usuario();

    }

    private void goMainScreen() {
        Intent intent = new Intent(SplashScreens.this, NavigationDrawer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", UsuarioLogado);
        startActivity(intent);
    }

    /*private void goCadastrarScreen(Usuario User) {
        Intent intent = new Intent(this, CadastroUsuario.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("User", User);
        startActivity(intent);
    }*/

    private void goLoginScreen() {
        Intent intent = new Intent(this, FacebookLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
}
