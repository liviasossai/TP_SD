package com.example.liviadalfiorsossai.myapplication;

import android.widget.ImageView;

/**
 * Created by liviadalfiorsossai on 3/22/15.
 */

// Classe que armazena os estados de uma jogada, para que seja possível renderizar a peça certa na posição correta
public class TratadorJogada {

    private ImageView iv_j1; // Armazena a peça do primeiro estado da jogada
    private ImageView iv_j2; // Armazena a peça do segundo estado da jogada

    public TratadorJogada(){
    }

    public ImageView getIV_j1(){
        return iv_j1;
    }

    public void setIV_j1(ImageView i){
    iv_j1 = i;
     }

    public ImageView getIV_j2(){
        return iv_j2;
    }

    public void setIV_j2(ImageView i){
        iv_j2 = i;
    }
}
