var express = require('express');
var shuffle = require('shuffle-array');

var router = express.Router();


var http = require('http').Server(express).listen(7001);
var io_server = require('socket.io')(http);


/******************* Somente para teste gustavo**********************
var io_client = require('socket.io-client')('http://localhost:3001');
io_client.on('connect', function() {
  console.log("socket conectado ao servidor 1 para iniciar eleicao");
  io_client.emit('inic_eleicao', true);
  setTimeout(function() {
    io_client.disconnect();
    }, 500);
});
/*********************************************************************/


//************Inicio Jogo***********//
var NUM_IMAGENS = 20;

<<<<<<< HEAD
var numPares = 8;


<<<<<<< Updated upstream
=======
var pecas_jogo = embaralhaPecas(numPares);
=======
>>>>>>> Stashed changes
var IP = "192.168.25.16"; // COLOCAR IP DA MAQUINA
var porta = null; // Quando for testar sem ter que inicializar todos: escolher a porta de comunicacao, simulando o servidor eleito. ex.: 5000, para o servidor 3
var list_servers = [3001, 4001, 5001, 6001]; //lista de servidores socket
var lista_server = [3000, 4000, 5000, 6000]; //lista de servidores
//Socket servidor
io_server.on('connection', function(socket) {


  socket.on('elected', function(IP_e, porta_e) {
    console.log('Um servidor foi eleito');
    console.log("IP eleito: " + IP_e);
    console.log("Porta eleito: " + porta_e);
    //IP = IP_e;
    porta = porta_e;
>>>>>>> origin/master

// Variável que armazena como cada jogador verá o tabuleiro
// É uma matriz cuja linhas são o tabuleiro de cada cliente
// e cada coluna uma posicão
var tabuleiro_cliente = [];

// Aramazena se cada carta está liberada ou não
 // Status:
 //   100: Carta bloqueada
 //   200: Carta não existente (o par já foi formado)
 //   Valor original (ID da img): carta liberada

<<<<<<< HEAD
=======
  socket.on('failed', function(num_Server) {
    console.log('Servidor ' + num_Server + 'falhou');

    if (porta == list_servers[num_Server] + 1) {
      var rand = Math.floor(Math.random() * 4);
      while (rand == num_Server) {
        var rand = Math.floor(Math.random() * 4);
      }
      var port_client = list_servers[rand];
      var io_client = require('socket.io-client')('http://localhost:' + port_client);

      //Socket cliente
      io_client.on('connect', function() {
        console.log("socket conectado ao servidor " + port_client + " para iniciar eleicao");
        io_client.emit('inic_eleicao', true);
      });
      setTimeout(function() {
        io_client.disconnect();
      }, 1000);

    }
  });

<<<<<<< Updated upstream

  socket.on('failed', function(num_Server) {
    console.log('Servidor ' + num_Server + 'falhou');

    if (porta == list_servers[num_Server] + 1) {
      var rand = Math.floor(Math.random() * 4);
      while (rand == num_Server) {
        var rand = Math.floor(Math.random() * 4);
      }
      var port_client = list_servers[rand];
      var io_client = require('socket.io-client')('http://localhost:' + port_client);

      //Socket cliente
      io_client.on('connect', function() {
        console.log("socket conectado ao servidor " + port_client + " para iniciar eleicao");
        io_client.emit('inic_eleicao', true);
      });
      setTimeout(function() {
        io_client.disconnect();
      }, 1000);

    }
  });

=======
>>>>>>> Stashed changes
  socket.on('disconnect', function() {
    console.log('user disconnected');
  });
});
>>>>>>> origin/master



var ids_possiveis = [0, 1, 2];
var ids_clientes = [];

var pontuacao_clientes = []
num_clientes = 0;

var vencedor = -1;


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
<<<<<<< HEAD
    if (func == 'iniciaJogo') {
    //console.log("Função requisitada: peças");
    
    num_clientes++;
    ids_clientes.push(ids_possiveis[num_clientes-1])
    tabuleiro_cliente.push(iniciaTab(numPares));
            
    res.json({
      pos: pecas_jogo,
      id: ids_clientes[num_clientes-1]
    });
=======

  if (func == 'ContarPalavra') {
    var str = req.body.TEXTO;
    var tam = ContarPalavra(str);
    res.json({
      numWord: tam
    });

  } else if (func == 'embaralharPecas') {
    console.log("Função requisitada: embaralhar peças");
    NUM_PARES = req.body.num_pares;

    var pecas_jogo = embaralhaPecas(NUM_PARES);
    res.json({
      pos: pecas_jogo
    });

  } else if (func == 'consulta') {
      var rand = Math.floor(Math.random() * 4);
      porta = lista_server[rand];
      console.log('porta atual: '+porta);

      Init_election();
      res.json({
        ip: IP,
        port: porta
      });


  } else if (func == 'no_server') { // Servidor lider nao responde
    Init_election();

  } else {
    res.json(undefined);
>>>>>>> origin/master
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



function Init_election() {
  var rand = Math.floor(Math.random() * 4);
  var port_client = list_servers[rand];
  var io_client = require('socket.io-client')('http://localhost:' + port_client);

  //Socket cliente
  io_client.on('connect', function() {
    console.log("socket conectado ao servidor " + port_client + " para iniciar eleicao");
    io_client.emit('inic_eleicao', true);
  });
  setTimeout(function() {
    io_client.disconnect();
  }, 1000);
}

function Init_election() {
  var rand = Math.floor(Math.random() * 4);
  var port_client = list_servers[rand];
  var io_client = require('socket.io-client')('http://localhost:' + port_client);

  //Socket cliente
  io_client.on('connect', function() {
    console.log("socket conectado ao servidor " + port_client + " para iniciar eleicao");
    io_client.emit('inic_eleicao', true);
  });
  setTimeout(function() {
    io_client.disconnect();
  }, 1000);
}


module.exports = router;
