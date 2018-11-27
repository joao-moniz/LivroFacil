package com.example.joao__us9xpst.livro_facil.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.joao__us9xpst.livro_facil.Model.Livro;
import com.example.joao__us9xpst.livro_facil.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by joao- on 08/06/2017.
 */

public class Adapter_Livro extends BaseAdapter {
    private Context mContext;
    private ArrayList<Livro> livros;

    public Adapter_Livro(Context mContext, ArrayList<Livro> livros){
        this.mContext = mContext;
        this.livros = livros;
    }
    public void updateResults(ArrayList<Livro>  results) {
        livros = results;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return livros.size();
    }

    @Override
    public Object getItem(int position) {
        return livros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v =  View.inflate(mContext, R.layout.adapter_livro,null);

        TextView tvTitulo = (TextView) v.findViewById(R.id.tv_titulo);
        tvTitulo.setText(livros.get(position).getTitulo());

        Uri uri = Uri.parse(livros.get(position).getImageUrl());
        SimpleDraweeView draweeView = (SimpleDraweeView) v.findViewById(R.id.im_livro);
        draweeView.setImageURI(uri);


        TextView tvDescricao = (TextView) v.findViewById(R.id.tv_descricao);
        tvDescricao.setText(livros.get(position).getDescricao());

        v.setTag(position);

        return v;
    }
}
