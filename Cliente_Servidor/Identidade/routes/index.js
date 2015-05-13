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

//Socket servidor
io_server.on('connection', function (socket) {
    console.log('Um servidor foi eleito');

    socket.on('elected', function (IP_e, porta_e) {

    	console.log("IP eleito: "+IP_e);
    	console.log("Porta eleito: "+porta_e);
	//IP = IP_e;
	porta = porta_e;

    });


    socket.on('disconnect', function () {
        console.log('user disconnected');
    });
});


/************* Consulta o Servidor Identidade *****************/
router.post('/consulta', function (req, res) {

        res.json({ip: IP, port: porta});

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



//*******Inicio Contar Palavra********//
router.post('/', function (req, res) {
    var func = req.body.FUNCAO;

    if (func == 'ContarPalavra') {
        var str = req.body.TEXTO;
        var tam = ContarPalavra(str);
        res.json({
            numWord: tam
        });
    }
    else if(func == 'embaralharPecas'){
            console.log("Função requisitada: embaralhar peças");
            NUM_PARES = req.body.num_pares;

            var pecas_jogo = embaralhaPecas(NUM_PARES);
            res.json({
                pos: pecas_jogo
                });
    }
     else if(func == 'consulta'){
         res.json({ip: IP, port: porta});

    }else{
        res.json(undefined);
    }
});

function ContarPalavra(str) {
        var palavras = str.split(" ");
        return palavras.length;
}
//********Fim Contar Palavra**********//


module.exports = router;
