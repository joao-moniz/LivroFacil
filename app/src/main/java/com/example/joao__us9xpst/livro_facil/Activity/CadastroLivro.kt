package com.example.joao__us9xpst.livro_facil.Activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.joao__us9xpst.livro_facil.Adapter.Adapter_Spiner
import com.example.joao__us9xpst.livro_facil.Model.Categoria
import com.example.joao__us9xpst.livro_facil.Model.Livro
import com.example.joao__us9xpst.livro_facil.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cadastro_livro.*
import android.content.Intent
import android.view.MotionEvent
import android.widget.Toast
import android.graphics.Bitmap
import android.provider.MediaStore
import android.app.Activity
import com.example.joao__us9xpst.livro_facil.Model.Usuario
import com.example.joao__us9xpst.livro_facil.Util.UserUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException


class CadastroLivro : AppCompatActivity() {
    private val SELECT_PICTURE = 1
    var bitmap: Bitmap? = null

    var livro : Livro = Livro();
    var categorias = arrayListOf<Categoria>();
    lateinit var spinnerAdapter : Adapter_Spiner
    lateinit var btAdd :ImageButton
    lateinit var tvTitulo : TextView
    lateinit var tvAutor : TextView
    lateinit var tvDescricao : TextView
    lateinit var btAdicionar :Button
    lateinit var imLivro :ImageView
    lateinit var spinnerCategoria :Spinner


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_livro)
        setSupportActionBar(toolbar_cadastroLivro)


        spinnerCategoria = findViewById(R.id.sp_categorias) as Spinner
        btAdd = findViewById(R.id.imbutton_addCategoria)
        btAdd.setOnClickListener(clickListener)

        tvTitulo = findViewById(R.id.tv_titulo_cadastroLivro)
        tvTitulo.setError(null);
        tvAutor  = findViewById(R.id.tv_autor_cadastroLivro)
        tvAutor.setError(null);
        tvDescricao = findViewById(R.id.tv_descrcao_cadastroLivro)
        tvDescricao.setError(null);

        btAdicionar = findViewById(R.id.bt_adicionarLivro)
        btAdicionar.setOnClickListener(clickListener)

        imLivro = findViewById(R.id.im_LivroCadastro)
        imLivro.setOnClickListener(clickListener)
        imLivro.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                imLivro.setBackground(resources.getDrawable(R.color.background_color))
            } else if (event.action == MotionEvent.ACTION_UP) {
                imLivro.setBackground(resources.getDrawable(R.color.mtrl_btn_transparent_bg_color))
            }
            false
        })



        spinnerAdapter = Adapter_Spiner(baseContext!!, categorias)
        spinnerCategoria?.adapter = spinnerAdapter

        initCategory();



    }

    val clickListener = View.OnClickListener {view ->

        when (view.getId()) {
            R.id.imbutton_addCategoria -> {
                val layoutInflaterAndroid = LayoutInflater.from(this@CadastroLivro)
                val mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box_livro, null)
                val alertDialogBuilderUserInput = AlertDialog.Builder(this@CadastroLivro)
                alertDialogBuilderUserInput.setView(mView)

                val userInputDialogEditText = mView.findViewById<View>(R.id.userInputDialog) as EditText
                alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialogBox, id ->
                        var myRef : DatabaseReference =   Categoria.categoriasRef.push();
                        var categoria = Categoria()
                        categoria.id = myRef.key
                        categoria.descricao = userInputDialogEditText.text.toString()
                        myRef.setValue(categoria).addOnCompleteListener(OnCompleteListener {  task ->
                            if(task.isCanceled){
                                Toast.makeText(this, task.exception!!.message,Toast.LENGTH_LONG).show();
                            }
                            if(task.isSuccessful){
                                categorias.add(categoria);
                                spinnerAdapter.notifyDataSetChanged();
                                dialogBox.cancel();

                            }
                        })
                    }

                    .setNegativeButton(
                        "Cancel"
                    ) { dialogBox, id ->
                        dialogBox.cancel()
                    }

                val alertDialogAndroid = alertDialogBuilderUserInput.create()
                alertDialogAndroid.show()
            }
            R.id.im_LivroCadastro ->{
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Buscar imagens galeria"),
                    SELECT_PICTURE
                )
            }
            R.id.bt_adicionarLivro ->{
                verificarTexto()

            }

        }
    }
    fun SalvarImagem(){
        var  storage : FirebaseStorage = FirebaseStorage.getInstance();
        var  storageRef : StorageReference = storage.getReference();
        var imagesRef = storageRef.child("Imagens_Livro")
        var titulo = livro.titulo;
        titulo = titulo.replace(" ","_");
        imagesRef = imagesRef.child(titulo);
        var  baos :ByteArrayOutputStream =  ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        var  data : ByteArray? = baos.toByteArray();
        var uploadTask :UploadTask = imagesRef.putBytes(data!!);

        imLivro.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                imLivro.setBackground(resources.getDrawable(R.color.background_color))
            } else if (event.action == MotionEvent.ACTION_UP) {
                imLivro.setBackground(resources.getDrawable(R.color.mtrl_btn_transparent_bg_color))
            }
            false
        })
        uploadTask.addOnFailureListener(OnFailureListener {exception ->
                        })
                    .addOnSuccessListener (
            object : OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                    imagesRef.downloadUrl.addOnCompleteListener () {taskSnapshot ->

                        var url = taskSnapshot.result
                        livro.imageUrl = url.toString()

                        SalvarLivro();

                    }
                }
            }
        )
    }

    fun     SalvarLivro(){
        var myRef : DatabaseReference =   Livro.livroRef.push();
        livro.id = myRef.key;
        if(UserUtil.getUser() == null  || UserUtil.getUser().id == null ){
            var uid : String  = FirebaseAuth.getInstance().currentUser!!.uid;

            Usuario.UsuarioRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    var user : Usuario  = p0.getValue(Usuario::class.java)!!;
                    livro.user = user;
                    UserUtil.setUser(user);
                    sav(myRef);
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


            })
        }else{
            livro.user = UserUtil.getUser();
            sav(myRef);
        }

    }

    fun sav(myRef :DatabaseReference ){
        myRef.setValue(livro.toMap()).addOnCompleteListener(OnCompleteListener {  task ->
            if(task.isSuccessful){
                this@CadastroLivro.finish()
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val imageUri = data!!.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri)
                    imLivro.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 128, 128, true))
                } catch (e: IOException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private  fun verificarTexto(): Boolean {
        if(tvTitulo.text != null && tvTitulo.text.isNotEmpty() ){
            if(tvAutor.text != null && tvAutor.text.isNotEmpty() ) {
                if(tvDescricao.text != null && tvDescricao.text.isNotEmpty() ) {
                    if(bitmap == null){
                        Toast.makeText(this,"Selecione uma imagem" ,Toast.LENGTH_LONG).show();
                    }else{
                        if(spinnerCategoria.selectedItemPosition != -1){
                            livro.titulo = tvTitulo.text.toString()
                            livro.autor = tvAutor.text.toString()
                            livro.descricao = tvDescricao.text.toString()
                            livro.categoria = categorias.get(spinnerCategoria.selectedItemPosition);
                            SalvarImagem()
                        }else{
                            Toast.makeText(this,"Selecione uma categoria" ,Toast.LENGTH_LONG).show();
                        }


                    }
                }else {
                    tvDescricao.setError("Campo Obrigarotio")
                    tvDescricao.requestFocus();
                }
            }else{
                tvAutor.setError("Campo Obrigarotio")
                tvAutor.requestFocus();
            }
        }else{
            tvTitulo.setError("Campo Obrigarotio")
            tvTitulo.requestFocus();
        }

        return false;
    }
    private fun initCategory() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categorias.clear()
                dataSnapshot.children.mapNotNullTo(categorias) { it.getValue<Categoria>(Categoria::class.java) }
                spinnerAdapter.notifyDataSetChanged();
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        Categoria.categoriasRef.addListenerForSingleValueEvent(menuListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
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
}


