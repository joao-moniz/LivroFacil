package com.example.joao__us9xpst.livro_facil.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by joao- on 06/06/2017.
 */

public class Usuario implements Serializable {
    public static DatabaseReference UsuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuario");
    private String id;
    private String nome;
    private String email;
    private String photoURL;
    private String cellNumber;
    private Integer tipo;  // -1: desativado 0: normal  1: moderador-1 2: moderador-2 3: moderador-3  4: moderador-4 5: admin

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("nome",nome);
        map.put("email",email);
        map.put("photoURL",photoURL);

        map.put("cellNumber",cellNumber);
        map.put("tipo",tipo);
        return map;
    }

    public Usuario() {
    }
    public Usuario(DataSnapshot dataSnapshot) {

    }

    public Usuario(String id, String nome, String email, String photoURL,String cellNumber) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.photoURL = photoURL;
        this.cellNumber = cellNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getTipo() {

        switch (tipo){
            case   -1: return "desativado";
            case   0:  return "normal";
            case   1:  return " moderador_1";
            case   2:  return " moderador_2";
            case   3:  return " moderador_3";
            case   4:  return " moderador_4";
            case   5:  return "admin";
            default: return null;
        }
    }



    public Integer getTipoInteger() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
}
