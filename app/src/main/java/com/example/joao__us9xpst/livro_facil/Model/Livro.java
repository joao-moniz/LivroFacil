package com.example.joao__us9xpst.livro_facil.Model;

import android.os.Parcelable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Livro implements Serializable {
    public static DatabaseReference livroRef = FirebaseDatabase.getInstance().getReference().child("Livros");
    String id;
    String titulo;
    String descricao;
    String autor;
    Usuario user;
    Categoria categoria;
    String imageUrl;
    Integer status; // 0: analise  -1: reprovado  1: aprovado

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String name) {
        this.titulo = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("titulo",titulo);
        map.put("descricao",descricao);
        map.put("autor",autor);
        map.put("user",user.toMap());
        map.put("categoria",categoria.toMap());
        map.put("imageUrl",imageUrl);
        map.put("status",status);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
