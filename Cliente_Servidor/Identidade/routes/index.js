var express = require('express');
var shuffle = require('shuffle-array');
var router = express.Router();


var http = require('http').Server(express).listen(7001);
var io_server = require('socket.io')(http);

//************Inicio Jogo***********//
var NUM_PARES;
var NUM_IMAGENS = 15;

var IP = null; // COLOCAR IP DA MAQUINA
var porta = null; // Quando for testar sem ter que inicializar todos: escolher a porta de comunicacao, simulando o servidor eleito. ex.: 5000, para o servidor 3
var list_servers = [3001, 4001, 5001, 6001]; //lista de servidores
//Socket servidor
io_server.on('connection', function(socket) {


  socket.on('elected', function(IP_e, porta_e) {
    console.log('Um servidor foi eleito');
    console.log("IP eleito: " + IP_e);
    console.log("Porta eleito: " + porta_e);
    //IP = IP_e;
    porta = porta_e;

  });


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

  socket.on('disconnect', function() {
    console.log('user disconnected');
  });
});




//var pecas_jogo;



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

    console.log("Posições das peças");
    console.log(posicoesPossiveis);

    return posicoesPossiveis;


  }
  //*********Fim Jogo*********//



//*******Inicio Requisicao RPC********//
router.post('/', function(req, res) {
  var func = req.body.FUNCAO;

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
    if (porta == null) {
      Init_election();
      res.json({
        ip: IP,
        port: porta
      });
    } else {
      res.json({
        ip: IP,
        port: porta
      });
    }

  } else if (func == 'no_server') { // Servidor lider nao responde
    Init_election();

  } else {
    res.json(undefined);
  }
});

function ContarPalavra(str) {
    var palavras = str.split(" ");
    return palavras.length;
  }
  //********Fim Contar Palavra**********//

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
