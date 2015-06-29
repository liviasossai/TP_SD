package com.example.liviadalfiorsossai.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liviadalfiorsossai on 3/22/15.
 */
public class TratadorPecas {

    private int NUM_PARES;
    private int [] pos;
    public boolean [] peca_valida; // Usado para impedir que seja computada a jogada de peças que não existem mais no jogo

    public TratadorPecas(int NUM_PAR){
        NUM_PARES = NUM_PAR;
        //pos = imagensRandomicas();
        pos = new int[2*NUM_PAR];

        peca_valida = new boolean[2*NUM_PAR];

        for(int i = 0; i < 2*NUM_PAR; i++){
            peca_valida[i] = true;
        }

    }

    public void re_inicializa(int NUM_PAR){
        NUM_PARES = NUM_PAR;
        //pos = imagensRandomicas();

        peca_valida = new boolean[2*NUM_PAR];

        for(int i = 0; i < 2*NUM_PAR; i++){
            peca_valida[i] = true;
        }

    }

    public int getPecasDisp(){
        int count = 0;
        for(int i = 0; i < 2*NUM_PARES; i++){
            if(peca_valida[i]){
                count++;
            }
        }
        return count;
    }

    // Todas as imagens possíveis
    private Integer[] mThumbIds = {
            R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4,
            R.drawable.f5, R.drawable.f6, R.drawable.f7, R.drawable.f8,
            R.drawable.f9, R.drawable.f10, R.drawable.f11, R.drawable.f12,
            R.drawable.f13, R.drawable.f14, R.drawable.f15, R.drawable.f16,
            R.drawable.f17, R.drawable.f18, R.drawable.f19, R.drawable.f20,
            R.drawable.f21,

    };

    // Embaralha as peças
    /*private int [] imagensRandomicas(){

        ArrayList<Integer> imagensPossiveis = new ArrayList<Integer>();
        ArrayList<Integer> imagensPossiveis_aux = new ArrayList<Integer>();
        ArrayList<Integer> posicoesPossiveis = new ArrayList<Integer>();
        int[] posicoes = new int[2*NUM_PARES];

        for(int i = 0; i < mThumbIds.length; i++){
            imagensPossiveis.add(i, i);
        }


        Collections.shuffle(imagensPossiveis);

        for(int i = 0; i < NUM_PARES; i++){
            imagensPossiveis_aux.add(imagensPossiveis.get(i));
        }

        for(int i = 0; i < NUM_PARES; i++){
            posicoesPossiveis.add(i, imagensPossiveis_aux.get(i));

        }
        for(int i = 0; i < NUM_PARES; i++){
            posicoesPossiveis.add(imagensPossiveis_aux.get(i));

        }

        Collections.shuffle(posicoesPossiveis);

        for(int i = 0; i < 2*NUM_PARES; i++){
            posicoes[i] = mThumbIds[posicoesPossiveis.get(i)];

        }

        return posicoes;
    }*/

    public void imagensRandomicas(int[] posi) {

        for(int i = 0; i < 2*NUM_PARES; i++){
                if(posi[i] == 100) {
                    pos[i] = R.drawable.f31;
                }
                else if(posi[i] == 200) {
                    pos[i] = R.drawable.f30;
                }
            else{
                    pos[i] = mThumbIds[posi[i]];
                }

        }
    }


    public int getPeca(int posicao){
        return pos[posicao];

    }

    public int getNull(){
        return R.drawable.f30;
    } // Retorna o código da imagem uma carta que não é mais válida

    public int getBock(){
        return R.drawable.f31;
    } // Retorna o código da imagem uma carta que não é mais válida

    public int getCover(){
        return R.drawable.f0;
    } // Retorna o códido da imagem da carta virada

    public int getPosicao(int img){
        int ind = 0;
        for(int i = 0; i < mThumbIds.length; i++){
            if(mThumbIds[i] == img){ind = i; break;}
        }
        return ind;
    }

    // Modifica a imagem exibida em uma posição
    public void setPos(int posicao, int img){
        pos[posicao] = img;

    }

    // Usado quando uma peça sai do jogo (ou seja, quando um par já foi encontrado)
    public void setPecaValida(int posicao){
        //TODO Lógica de informar o servidor de que foi formado o par
        // Montagem do corpo da requisição
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("FUNCAO", "match");
            jsonObj.put("num_pares", Integer.toString(NUM_PARES));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        peca_valida[posicao] = false;

    }

    // Verifica se uma peça no jogo é válida (ou seja, se seu par ainda não foi encontrado)
    public boolean getPecaV(int position) throws IOException, ConnException {
        //TODO Lógica de validar peça no servidor
        // Montagem do corpo da requisição
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("FUNCAO", "mayI");
            jsonObj.put("pos", Integer.toString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonreturned =  RPC.downloadUrl(jsonObj);
        JSONArray isOK = null;
        Boolean isOKboll = null;
        try {
            isOK = jsonreturned.getJSONArray("OK");
            isOKboll = isOK.getBoolean(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isOKboll){
            return true;
        }
        else
        {
            return false;
        }
    }


}
