package com.example.joao__us9xpst.livro_facil.Activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.example.joao__us9xpst.livro_facil.Adapter.Adapter_Livro
import com.example.joao__us9xpst.livro_facil.Model.Categoria
import com.example.joao__us9xpst.livro_facil.Model.Livro
import com.example.joao__us9xpst.livro_facil.Model.Usuario
import com.example.joao__us9xpst.livro_facil.R
import com.example.joao__us9xpst.livro_facil.Util.UserUtil
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*

class NavigationDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var livros = arrayListOf<Livro>();
    lateinit var lvLivros : ListView
    lateinit var livroAdapter : Adapter_Livro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)
        setSupportActionBar(toolbar)
        livroAdapter = Adapter_Livro(this,livros);
        lvLivros = findViewById(R.id.lvLivros)
        lvLivros.adapter = livroAdapter

        val itemClickListener = object  : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val i = Intent(this@NavigationDrawer, LivroDetalhes::class.java)
                if(livros.get(position).user == null){
                    livros.get(position).user = UserUtil.getUser()
                }
                i.putExtra("livro", livros.get(position));

                startActivity(i)
            }

        }
        lvLivros.setOnItemClickListener(itemClickListener);

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        initLivros()
    }


    private fun initLivros() {
        val menuListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("loadPost:onCancelled ${p0.toException()}")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(ds: DataSnapshot, p1: String?) {

                    var livro :Livro = Livro()
                    livro.id = ds.child("id").getValue(String::class.java)
                    livro.titulo = ds.child("titulo").getValue(String::class.java)
                    livro.descricao = ds.child("descricao").getValue(String::class.java)
                    livro.autor = ds.child("autor").getValue(String::class.java)
                    livro.user = ds.child("user").getValue(Usuario::class.java)
                    livro.categoria = ds.child("categoria").getValue(Categoria::class.java)
                    livro.imageUrl = ds.child("imageUrl").getValue(String::class.java)
                    livro.status = ds.child("status").getValue(Int::class.java)
                    livros.add(livro)

                livroAdapter.notifyDataSetChanged();
            }

            override fun onChildRemoved(ds: DataSnapshot) {
                var livro :Livro = Livro()
                livro.id = ds.child("id").getValue(String::class.java)
                livro.titulo = ds.child("titulo").getValue(String::class.java)
                livro.descricao = ds.child("descricao").getValue(String::class.java)
                livro.autor = ds.child("autor").getValue(String::class.java)
                livro.user = ds.child("user").getValue(Usuario::class.java)
                livro.categoria = ds.child("categoria").getValue(Categoria::class.java)
                livro.imageUrl = ds.child("imageUrl").getValue(String::class.java)
                livro.status = ds.child("status").getValue(Int::class.java)
                livros.remove(livro)

                livroAdapter.notifyDataSetChanged();
            }

        }
        Livro.livroRef.addChildEventListener(menuListener)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_category -> {
                // Handle the camera action
            }
            R.id.nav_donate -> {
                val i = Intent(this@NavigationDrawer, CadastroLivro::class.java)
                startActivity(i)
            }
            R.id.nav_meusLivros -> {
                val i = Intent(this@NavigationDrawer, NavigationDrawer_MeusLivros::class.java)
                startActivity(i)
            }
            R.id.nav_logout -> {
                logout();
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun logout() {
        LoginManager.getInstance().logOut()
        val mAuth = FirebaseAuth.getInstance()
        FirebaseAuth.getInstance().signOut()
        finish()
        val i = Intent(this@NavigationDrawer, FacebookLogin::class.java)
        startActivity(i)
    }
}
