var express = require('express');
var shuffle = require('shuffle-array');
var router = express.Router();


//************Inicio Jogo***********//
var NUM_PARES;
var NUM_IMAGENS = 15;

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
     else {
        res.json(undefined);
    }
});

function ContarPalavra(str) {
        var palavras = str.split(" ");
        return palavras.length;
}
//********Fim Contar Palavra**********//


module.exports = router;