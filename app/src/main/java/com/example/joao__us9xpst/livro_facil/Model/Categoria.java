package com.example.joao__us9xpst.livro_facil.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class Categoria implements Serializable {
    public static DatabaseReference categoriasRef = FirebaseDatabase.getInstance().getReference().child("Categotias");
    String id;
    String descricao;
    Integer status; // 0: analise  -1: reprovado  1: aprovado

    public Categoria() {
    }

    public String getId() {        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("descricao",descricao);
        map.put("status",status);
        return map;
    }
}
