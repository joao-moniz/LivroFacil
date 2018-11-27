package com.example.joao__us9xpst.livro_facil.Activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.joao__us9xpst.livro_facil.Model.Livro
import com.example.joao__us9xpst.livro_facil.R
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_livro_detalhes.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import android.content.Intent
import com.google.android.gms.tasks.OnCompleteListener


class LivroDetalhes_Edit : AppCompatActivity() {
    lateinit var imLivro : SimpleDraweeView
    lateinit var tvTitulo :TextView
    lateinit var tvAutor :TextView
    lateinit var tvCategoria :TextView
    lateinit var tvDoador :TextView
    lateinit var tvDescricao :TextView
    var livro :Livro = Livro()

    lateinit var btCombinar :Button
    lateinit var btFinalizar :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livro_detalhes_edit)
        setSupportActionBar(toolbar_livroDetalhes)
        val e = intent.extras
        if (e != null) {
            livro = e.getSerializable("livro") as Livro
        }
        val uri = Uri.parse(livro.getImageUrl())
        imLivro = findViewById(R.id.im_livro_livroDetalhes)
        imLivro.setImageURI(uri);

        tvTitulo = findViewById(R.id.tv_titulo)
        tvTitulo.text = livro.titulo

        tvAutor = findViewById(R.id.tv_autor_detalhesLivro)
        tvAutor.text = livro.autor

        tvCategoria = findViewById(R.id.tvCategoria_livroDetalhes)
        tvCategoria.text = livro.categoria.descricao

        tvDoador = findViewById(R.id.tvDoadorName_livroDetalhes)
        tvDoador.text = livro.user.nome

        tvDescricao = findViewById(R.id.tv_descricao)
        tvDescricao.text = livro.descricao

        val clickListener = object :View.OnClickListener {
            override fun onClick(v: View?) {
                val url = "https://wa.me/55"+livro.user.cellNumber
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

        }

        btCombinar = findViewById(R.id.btCombinar)
        btCombinar.setOnClickListener(clickListener)


        val FinalizarListener = object :View.OnClickListener {
            override fun onClick(v: View?) {
                Livro.livroRef.child(livro.id).removeValue().addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this@LivroDetalhes_Edit.finish();
                    }
                });

            }
        }

        btFinalizar = findViewById(R.id.btFinalizar)
        btFinalizar.setOnClickListener(FinalizarListener)

    }


}
