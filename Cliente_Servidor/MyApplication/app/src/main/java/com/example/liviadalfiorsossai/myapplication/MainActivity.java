package com.example.liviadalfiorsossai.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {
public int jogada = 0;
public int peca_virada1;
public int peca_virada2;
public int peca_virada1_pos=-1;
public int peca_virada2_pos=-1;
public int pares_virados = 0;

public int NUM_PARES = 8;

public int ID_CLIENTE = 0;

public int[] TABULEIRO_ATUAL;



    public int porta; // O servidor responderá com o número da porta que o cliente deverá ouvir

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Será usado futuramente, para fazer animações
       // ImageView anim = (ImageView) findViewById(R.id.animacao);
       // anim.setBackgroundResource(R.drawable.animacao);

        //AnimationDrawable animation = (AnimationDrawable) anim.getBackground();
        //animation.start();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


        //Elementos de tela
        final TextView textview = (TextView) findViewById(R.id.textView);

        final TratadorPecas pec = new TratadorPecas(NUM_PARES);

        final TratadorJogada TJ = new TratadorJogada();

        final GridView gridview = (GridView) findViewById(R.id.GV);

        final ImageAdapter ImgAdptr = new ImageAdapter(this);

        final Button button2 = (Button) findViewById(R.id.button2); // Botão que ativa a exibição dos níveis do jogo


        final Button button = (Button) findViewById(R.id.button); // Iniciar novo jogo
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                // Montagem do corpo da requisição
                JSONObject jsonObj = new JSONObject();
                try {

                    jsonObj.put("FUNCAO", "iniciaJogo");
                }catch (JSONException e){e.printStackTrace();}

                RequestTask req = new RequestTask(jsonObj, new RequestTask.AsyncResponse() {
                    // Callback da chamada assíncrona
                    @Override
                        public void processFinish(JSONObject result) {

                        // Calback da chamada de embaralhar
                        try {
                            JSONArray pos = result.getJSONArray("pos");
                            // Ao inicializar um jogo, cada cliente recebe um ID
                            ID_CLIENTE = result.getInt("id");
                            int pecas [] = new int[pos.length()];


                            for(int i = 0; i < pos.length(); i++){
                                pecas[i] = pos.getInt(i);
                            }


                            pec.re_inicializa(NUM_PARES);
                            pec.imagensRandomicas(pecas);

                            jogada = 0;
                            peca_virada1_pos=-1;
                            peca_virada2_pos=-1;
                            pares_virados = 0;

                            textview.setTextColor(Color.rgb(0, 0, 0));
                            textview.setTextSize(16);
                            textview.setBackgroundColor(Color.rgb(255, 255, 255));
                            textview.setText("  Total Pairs Flipped: 0");
                            textview.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);

                            ImgAdptr.init(NUM_PARES, pecas);
                            gridview.setAdapter(ImgAdptr);


                        }catch(JSONException e){
                            System.out.println("Erro "+e);
                        }
                    }
                });

                   req.execute();

                Timer t = new Timer();

                t.scheduleAtFixedRate(
                        new TimerTask()
                        {
                            public void run()
                            {



                                // Montagem do corpo da requisição
                                JSONObject jsonObj = new JSONObject();
                                try {
                                    jsonObj.put("FUNCAO", "verificaStatus");
                                    jsonObj.put("id", ID_CLIENTE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                RequestTask req = new RequestTask(jsonObj, new RequestTask.AsyncResponse() {
                                    // Callback da chamada assíncrona
                                    @Override
                                    public void processFinish(JSONObject result) {

                                        try {
                                            JSONArray pos = result.getJSONArray("pecas");
                                            int venc = result.getInt("venc");
                                            int pontos = result.getInt("pont");
                                            TABULEIRO_ATUAL = new int[pos.length()];

                                            for (int i = 0; i < pos.length(); i++) {
                                                TABULEIRO_ATUAL[i] = pos.getInt(i);
                                            }
                                            ImgAdptr.renderiza(NUM_PARES, TABULEIRO_ATUAL);
                                            gridview.setAdapter(ImgAdptr);

                                            if(venc != -1){
                                                if(venc == ID_CLIENTE) {
                                                    textview.setTextSize(18);
                                                    textview.setText("  Você está ganhando | Pontuação: " + pontos);
                                                    textview.setBackgroundColor(Color.rgb(255, 255, 0));

                                                    textview.setTextColor(Color.rgb(255, 0, 255));
                                                    textview.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                                                }
                                                else{
                                                    textview.setTextSize(18);
                                                    textview.setText("  Você está perdendo | Pontuação: " + pontos);
                                                    textview.setBackgroundColor(Color.rgb(0, 0, 0));

                                                    textview.setTextColor(Color.rgb(255, 0, 0));
                                                    textview.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                                                }

                                            }
                                            else{

                                                textview.setTextSize(18);
                                                textview.setText("  Empate | Pontuação: " + pontos);
                                                textview.setBackgroundColor(Color.rgb(255, 255, 255));

                                                textview.setTextColor(Color.rgb(0, 0, 0));
                                                textview.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                                            }

                                        } catch (JSONException e) {
                                            System.out.println("Erro " + e);
                                        }
                                    }
                                });
                                 req.execute();


                            }
                        },
                        0,      // run first occurrence immediately
                        2000);  // run every three seconds

            }
        });

        // Sempre que o jogador clicar em uma peça, serão computados os estados do jogo
        // (se as peças formarem par elas devem sair do jogo, caso contrário, suas imagens devem ser cobertas novamente)
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position2, long id) {
                ImageView imageView = (ImageView) v;
                final int position = position2;

                // Montagem do corpo da requisição
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("FUNCAO", "verificaPeca");
                    jsonObj.put("id", ID_CLIENTE);
                    jsonObj.put("pos", position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestTask req = new RequestTask(jsonObj, new RequestTask.AsyncResponse() {
                    // Callback da chamada assíncrona
                    @Override
                    public void processFinish(JSONObject result) {

                        try {
                            JSONArray pos = result.getJSONArray("pecas");
                            TABULEIRO_ATUAL = new int[pos.length()];

                            for (int i = 0; i < pos.length(); i++) {
                                TABULEIRO_ATUAL[i] = pos.getInt(i);
                            }
                            ImgAdptr.renderiza(NUM_PARES, TABULEIRO_ATUAL);
                            gridview.setAdapter(ImgAdptr);

                        } catch (JSONException e) {
                            System.out.println("Erro " + e);
                        }

                        // Estado 1 da jogada
                        if ((TABULEIRO_ATUAL[position] != 200) & (TABULEIRO_ATUAL[position] != 100)) {
                            if (jogada == 0) {

                                if (pares_virados > 0) {
                                    //textview.setText("  Total Pairs Flipped: " + pares_virados);
                                    if ((peca_virada2 == peca_virada1) && peca_virada1_pos != peca_virada2_pos) { // Não permitir que o clique em uma mesma posição conte como par
                                        //TJ.getIV_j1().setImageResource(pec.getNull());
                                        //TJ.getIV_j2().setImageResource(pec.getNull());

                                        JSONObject jsonObj3 = new JSONObject();
                                        try {
                                            jsonObj3.put("FUNCAO", "eliminaPeca");
                                            jsonObj3.put("id", ID_CLIENTE);
                                            jsonObj3.put("pos1", peca_virada1_pos);
                                            jsonObj3.put("pos2", peca_virada2_pos);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        RequestTask req3 = new RequestTask(jsonObj3, new RequestTask.AsyncResponse() {
                                            // Callback da chamada assíncrona
                                            @Override
                                            public void processFinish(JSONObject result) {

                                                try {
                                                    JSONArray pos = result.getJSONArray("pecas");
                                                    TABULEIRO_ATUAL = new int[pos.length()];

                                                    for (int i = 0; i < pos.length(); i++) {
                                                        TABULEIRO_ATUAL[i] = pos.getInt(i);
                                                    }
                                                    ImgAdptr.renderiza(NUM_PARES, TABULEIRO_ATUAL);
                                                    gridview.setAdapter(ImgAdptr);



                                                } catch (JSONException e) {
                                                    System.out.println("Erro " + e);
                                                }

                                            }
                                        });
                                        pec.setPecaValida(peca_virada1_pos);
                                        pec.setPecaValida(peca_virada2_pos);

                                        pec.setPos(peca_virada1_pos, pec.getNull());
                                        pec.setPos(peca_virada2_pos, pec.getNull());

                                        peca_virada1_pos = -1;
                                        peca_virada2_pos = -1;
                                        req3.execute();



                                    } else {
                                        //imageView.setImageResource(pec.getCover());
                                        //TJ.getIV_j1().setImageResource(pec.getCover());
                                        //TJ.getIV_j2().setImageResource(pec.getCover());
                                        // Montagem do corpo da requisição
                                        JSONObject jsonObj2 = new JSONObject();
                                        try {
                                            jsonObj2.put("FUNCAO", "desbloqueiaPeca");
                                            jsonObj2.put("id", ID_CLIENTE);
                                            jsonObj2.put("pos1", peca_virada1_pos);
                                            jsonObj2.put("pos2", peca_virada2_pos);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        RequestTask req2 = new RequestTask(jsonObj2, new RequestTask.AsyncResponse() {
                                            // Callback da chamada assíncrona
                                            @Override
                                            public void processFinish(JSONObject result) {

                                                try {
                                                    JSONArray pos = result.getJSONArray("pecas");
                                                    TABULEIRO_ATUAL = new int[pos.length()];

                                                    for (int i = 0; i < pos.length(); i++) {
                                                        TABULEIRO_ATUAL[i] = pos.getInt(i);
                                                    }
                                                    ImgAdptr.renderiza(NUM_PARES, TABULEIRO_ATUAL);
                                                    gridview.setAdapter(ImgAdptr);

                                                } catch (JSONException e) {
                                                    System.out.println("Erro " + e);
                                                }

                                            }
                                        });
                                        req2.execute();
                                    }
                                }


                                //imageView.setImageResource(pec.getPeca(position));
                                peca_virada1 = pec.getPeca(position);
                                peca_virada1_pos = position;
                                //TJ.setIV_j1(imageView);
                                jogada = 1;

                            }
                            // Estado 2 da jogada
                            else if (jogada == 1) {
                                if (pec.getPecaV(position)) {
                                    if (position != peca_virada1_pos) { // Para não permitir que cliques simultâneos em uma mesma posição contem como cartas viradas
                                        pares_virados++;
                                        peca_virada2 = pec.getPeca(position);
                                        peca_virada2_pos = position;
                                        //TJ.setIV_j2(imageView);
                                        //imageView.setImageResource(pec.getPeca(position));
                                        jogada = 0; // O jogo só retorna ao estado 1 (verificação) se a posição selecionada for diferente da atual


                                        if (pec.getPecasDisp() <= 2) { // Se só restam duas peças, isso significa que todos os pares já foram encontrados
                                            pec.setPecaValida(peca_virada1_pos);
                                            pec.setPecaValida(peca_virada2_pos);
                                            /*
                                            textview.setTextSize(22);
                                            textview.setText("  YOU WIN!");
                                            textview.setBackgroundColor(Color.rgb(255, 255, 0));

                                            textview.setTextColor(Color.rgb(255, 0, 255));
                                            textview.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                                            */
                                        }
                                    }


                                }

                            }


                        }
                    }

                });


                req.execute();


            }

    });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
