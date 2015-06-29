var express = require('express');
var shuffle = require('shuffle-array');

var router = express.Router();


var http = require('http').Server(express).listen(7001);
var io_server = require('socket.io')(http);

//************Inicio Jogo***********//
var NUM_IMAGENS = 20;

var numPares = 8;


var pecas_jogo = embaralhaPecas(numPares);

var horario_local = 0;

// Variável que armazena como cada jogador verá o tabuleiro
// É uma matriz cuja linhas são o tabuleiro de cada cliente
// e cada coluna uma posicão
var tabuleiro_cliente = [];

// Aramazena se cada carta está liberada ou não
 // Status:
 //   100: Carta bloqueada
 //   200: Carta não existente (o par já foi formado)
 //   Valor original (ID da img): carta liberada




var ids_possiveis = [0, 1, 2, 3];
var ids_clientes = [];

var pontuacao_clientes = []
num_clientes = 0;

var vencedor = -1;

var heartbeat_clientes = []; // Armazena os heartbeats, para o caso de algum jogador deixar o jogo
                             // Liberar os locks neste caso


function embaralhaPecas(qtd_pares) {

    var imagensPossiveis = new Array();
    var imagensPossiveis_aux = new Array();
    var posicoesPossiveis = new Array();

    var posicoes;


    for (var i = 0; i < NUM_IMAGENS; i++) {
      imagensPossiveis[i] = i;
    }

    shuffle(imagensPossiveis);

    for (var i = 0; i < qtd_pares; i++) {
      imagensPossiveis_aux[imagensPossiveis_aux.length] = imagensPossiveis[i];
    }

    for (var i = 0; i < qtd_pares; i++) {
      posicoesPossiveis[posicoesPossiveis.length] = imagensPossiveis_aux[i];

    }
    for (var i = 0; i < qtd_pares; i++) {
      posicoesPossiveis[posicoesPossiveis.length] = imagensPossiveis_aux[i];

    }

    shuffle(posicoesPossiveis);

    //console.log("Posições das peças");
    //console.log(posicoesPossiveis);

    return posicoesPossiveis;


  }

function iniciaTab(numPares){
    var tab = [];
    for(var i = 0; i < 2*numPares; i++){
        tab[i] = -10;
        pontuacao_clientes[i] = 0;
        
    }
    
    return tab;
    
}


//*******Inicio Requisicao RPC********//
router.post('/', function(req, res) {
  var func = req.body.FUNCAO;
    if (func == 'iniciaJogo') {
    //console.log("Função requisitada: peças");
    
    num_clientes++;
    ids_clientes.push(ids_possiveis[num_clientes-1])
    tabuleiro_cliente.push(iniciaTab(numPares));
            
    res.json({
      pos: pecas_jogo,
      id: ids_clientes[num_clientes-1]
    });
  }
  if (func == 'verificaPeca') {
    //console.log("Função requisitada: verificar/reservar peças");
   
            //console.log(req.body.id);
            //console.log(req.body.pos);
            
            
    // Varrer o tabuleiro de cada jogador e ver se a carta está bloqueada:
            var cont = 0;
            
            for(var i = 0; i < num_clientes; i++){
            //console.log("i: "+i+"req: "+req.body.id+" "+tabuleiro_cliente[i][req.body.pos]);
                if(tabuleiro_cliente[i][req.body.pos] == 100){
                cont = cont + 1;
                }
            
            }
            //console.log("cont: "+cont);
            if(cont == 0){ // Se a carta não está bloqueada para ninguém
            tabuleiro_cliente[req.body.id][req.body.pos] = pecas_jogo[req.body.pos];
                for(var i = 0; i < num_clientes; i++){
                    if(i != req.body.id){
                    tabuleiro_cliente[i][req.body.pos] = 100; // A carta é bloqueada para todos os outros jogadores
                }
            }
            
         }

            //console.log(tabuleiro_cliente);
            res.json({pecas: tabuleiro_cliente[req.body.id]}); // Entrega o vetor pronto para o cliente renderizar
            }
            
    if (func == 'desbloqueiaPeca') {
       //console.log("Função requisitada: desbloqueia");
            for(var i = 0; i < num_clientes; i++){
            tabuleiro_cliente[i][req.body.pos1] = -10; // A carta é bloqueada para todos os outros jogadores
            tabuleiro_cliente[i][req.body.pos2] = -10;
            }
       //console.log(tabuleiro_cliente);
            
        res.json({
                  pecas: tabuleiro_cliente[req.body.id]
                });
    }
            
            if (func == 'eliminaPeca') {
            //console.log("Função requisitada: desbloqueia");
            for(var i = 0; i < num_clientes; i++){
            tabuleiro_cliente[i][req.body.pos1] = 200; // A carta é bloqueada para todos os outros jogadores
            tabuleiro_cliente[i][req.body.pos2] = 200;
            }
            
            pontuacao_clientes[req.body.id] = pontuacao_clientes[req.body.id] + 1;
            //console.log(tabuleiro_cliente);
            
            
            
            res.json({
                     pecas: tabuleiro_cliente[req.body.id]
                     });
            }
            
            if (func == 'verificaStatus') {
            
            heartbeat_clientes[req.body.id] = horario_local;
            
            // Verifica quem está ganhando
            var maior = pontuacao_clientes[0];
            vencedor = 0;
            var empate = 1;
            
            for(var i = 0; i < num_clientes; i++){
            if(pontuacao_clientes[i] > maior){
            maior = pontuacao_clientes[i];
            vencedor = i;
            }
            }
            
            // Verifica se houve empate:
            var atual = pontuacao_clientes[0];
            for(var i = 1; i < num_clientes; i++){
                if(pontuacao_clientes[i] != atual){
                    empate = 0;
            }
            atual = pontuacao_clientes[i];
            }
            
            if(empate == 1){
            vencedor = -1;
            }
            
            
            //console.log(tabuleiro_cliente);
            res.json({pecas: tabuleiro_cliente[req.body.id], venc: vencedor, pont: pontuacao_clientes[req.body.id]}); // Entrega o vetor pronto para o cliente renderizar
            }
});


// A cada 30 segundo verifica se algum cliente desconectou
// Neste caso os locks deverão ser liberados
setInterval(function () {

            horario_local = horario_local + 5;
            
            for(var i = 0; i < num_clientes; i++){
                  if((horario_local - heartbeat_clientes[i] > 10) && heartbeat_clientes[i] != -1){
                        heartbeat_clientes[i] = -1;
                        var pecas_bloqueadas = [];
                        for(var j = 0; j < 2*numPares; j++){
                            if(tabuleiro_cliente[i][j] != 100 && tabuleiro_cliente[i][j] != 200 && tabuleiro_cliente[i][j] != -10){
                                pecas_bloqueadas.push(j);
                            }
                        }
                        for(var k = 0; k < num_clientes; k++){
                            for(var l = 0; l < pecas_bloqueadas.length; l++){
                                tabuleiro_cliente[k][pecas_bloqueadas[l]] = -10;
                            }
                        }
            
            
                  }
            }
            
            
            }, 5000);


module.exports = router;
