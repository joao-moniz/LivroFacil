package com.example.joao__us9xpst.livro_facil.Util;

import com.example.joao__us9xpst.livro_facil.Model.Usuario;

public class UserUtil {
    private static Usuario user = new Usuario();

    public static Usuario getUser() {
        return user;
    }

    public static void setUser(Usuario user) {
        UserUtil.user = user;
    }
}
