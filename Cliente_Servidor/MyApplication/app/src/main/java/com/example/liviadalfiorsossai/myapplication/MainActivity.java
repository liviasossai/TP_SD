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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    public int jogada = 0;
    public int peca_virada1;
    public int peca_virada2;
    public int peca_virada1_pos = -1;
    public int peca_virada2_pos = -1;
    public int pares_virados = 0;

    public int NUM_PARES = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Será usado futuramente, para fazer animações
        // ImageView anim = (ImageView) findViewById(R.id.animacao);
        // anim.setBackgroundResource(R.drawable.animacao);

        //AnimationDrawable animation = (AnimationDrawable) anim.getBackground();
        //animation.start();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicia a thread na qual o cliente escuta uma porta esperando a eleição do servidor líder
        //Thread fst = new Thread(new ServerThread());
        //fst.start();

        //Elementos de tela
        final TextView textview = (TextView) findViewById(R.id.textView);

        final TratadorPecas pec = new TratadorPecas(NUM_PARES);

        final TratadorJogada TJ = new TratadorJogada();

        final GridView gridview = (GridView) findViewById(R.id.GV);

        final ImageAdapter ImgAdptr = new ImageAdapter(this);



        final Button button = (Button) findViewById(R.id.button); // Iniciar novo jogo
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();

                //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                //StrictMode.setThreadPolicy(policy);

                //String url = RPC.geturl();
                //alert.setMessage("O servidor Identificador retornou o IP e porta do servidor do jogo: " + url);
                // alert.setTitle("IP e Porta Server Jogo");
                //alert.show();


                // Montagem do corpo da requisição
                JSONObject jsonObj = new JSONObject();
                try {

                    jsonObj.put("FUNCAO", "embaralharPecas");
                    jsonObj.put("num_pares", Integer.toString(NUM_PARES));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestTask req = new RequestTask(jsonObj, new RequestTask.AsyncResponse() {
                    AlertDialog alertServerUnavailable = new AlertDialog.Builder(MainActivity.this).create();
                    // Callback da chamada assíncrona
                    @Override
                    public void processFinish(JSONObject result, boolean response_is_OK) {
                        // Calback da chamada de embaralhar
                        if(response_is_OK){
                            try {
                                JSONArray pos = result.getJSONArray("pos");
                                int pecas[] = new int[pos.length()];


                                for (int i = 0; i < pos.length(); i++) {
                                    pecas[i] = pos.getInt(i);
                                }


                                pec.re_inicializa(NUM_PARES);
                                pec.imagensRandomicas(pecas);

                                jogada = 0;
                                peca_virada1_pos = -1;
                                peca_virada2_pos = -1;
                                pares_virados = 0;

                                textview.setTextColor(Color.rgb(0, 0, 0));
                                textview.setTextSize(16);
                                textview.setBackgroundColor(Color.rgb(255, 255, 255));
                                textview.setText("  Total Pairs Flipped: 0");
                                textview.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);

                                ImgAdptr.init(NUM_PARES);
                                gridview.setAdapter(ImgAdptr);


                            } catch (JSONException e) {
                                System.out.println("Erro " + e);
                            }
                        }
                        else
                        {
                            alertServerUnavailable.setMessage("O servidor do jogo está indisponível, tente novamente.");
                            alertServerUnavailable.setTitle("Servidor de Jogo indisponível");
                            alertServerUnavailable.show();
                        }
                    }
                });

                req.execute();

            }
        });

        // Sempre que o jogador clicar em uma peça, serão computados os estados do jogo
        // (se as peças formarem par elas devem sair do jogo, caso contrário, suas imagens devem ser cobertas novamente)
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView imageView = (ImageView) v;

                // Estado 1 da jogada
                try {
                    if (pec.getPecaV(position)) if (jogada == 0) {

                        if (pares_virados > 0) {
                            textview.setText("  Total Pairs Flipped: " + pares_virados);
                            if ((peca_virada2 == peca_virada1) && peca_virada1_pos != peca_virada2_pos) { // Não permitir que o clique em uma mesma posição conte como par
                                TJ.getIV_j1().setImageResource(pec.getNull());
                                TJ.getIV_j2().setImageResource(pec.getNull());

                                pec.setPecaValida(peca_virada1_pos);
                                pec.setPecaValida(peca_virada2_pos);

                                pec.setPos(peca_virada1_pos, pec.getNull());
                                pec.setPos(peca_virada2_pos, pec.getNull());

                                peca_virada1_pos = -1;
                                peca_virada2_pos = -1;

                            } else {
                                imageView.setImageResource(pec.getCover());
                                TJ.getIV_j1().setImageResource(pec.getCover());
                                TJ.getIV_j2().setImageResource(pec.getCover());

                            }
                        }

                        imageView.setImageResource(pec.getPeca(position));
                        peca_virada1 = pec.getPeca(position);
                        peca_virada1_pos = position;
                        TJ.setIV_j1(imageView);
                        jogada = 1;

                    }
    // Estado 2 da jogada
                    else if (jogada == 1) {
                        if (pec.getPecaV(position)) {
                            if (position != peca_virada1_pos) { // Para não permitir que cliques simultâneos em uma mesma posição contem como cartas viradas
                                pares_virados++;
                                peca_virada2 = pec.getPeca(position);
                                peca_virada2_pos = position;
                                TJ.setIV_j2(imageView);
                                imageView.setImageResource(pec.getPeca(position));
                                jogada = 0; // O jogo só retorna ao estado 1 (verificação) se a posição selecionada for diferente da atual


                                if (pec.getPecasDisp() <= 2) { // Se só restam duas peças, isso significa que todos os pares já foram encontrados
                                    pec.setPecaValida(peca_virada1_pos);
                                    pec.setPecaValida(peca_virada2_pos);

                                    textview.setTextSize(22);
                                    textview.setText("  YOU WIN!");
                                    textview.setBackgroundColor(Color.rgb(255, 255, 0));

                                    textview.setTextColor(Color.rgb(255, 0, 255));
                                    textview.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                                }
                            }
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ConnException e) {
                    e.printStackTrace();
                }
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
