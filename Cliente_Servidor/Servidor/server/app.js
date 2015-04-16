    var express = require('express');
    var _ = require('underscore');
    var fs = require('fs');
    var shuffle = require('shuffle-array');

    app = express();

    var bodyParser = require('body-parser');
    app.use( bodyParser.json() );
    app.use(bodyParser.urlencoded({ extended: true }));


    var NUM_PARES;
    var NUM_IMAGENS = 15;

    var pecas_jogo;

     app.post('/embaralha', function (req, res) {
             console.log("oi");
              
              NUM_PARES = req.body.num_pares;
              
             var pecas_jogo = embaralhaPecas(NUM_PARES);
             res.send({pos: pecas_jogo});
             
             
        });





        app.post('/jogada', function (req, res) {
                 //console.log(req.body.peca);
                 
                 /*position = req.body.peca;
                 peca_valida = req.body.peca_valida;
                 
                 jogada = req.body.jogada;
                 peca_virada1 = req.body.peca_virada1;
                 peca_virada2 = req.body.peca_virada2;
                 peca_virada1_pos = req.body.peca_virada1_pos;
                 peca_virada2_pos = req.body.peca_virada2_pos;
                 pares_virados = req.body.pares_virados;
                 
                 
                 console.log(position);
                 console.log(peca_valida);
                 console.log(jogada);
                 console.log(peca_virada1_pos);
                 console.log(peca_virada2_pos);
                 console.log(pares_virados);
                 console.log(peca_virada1);
                 console.log(peca_virada2);*/
                 
                 
                 res.send({pos: req.body.peca});
                 
                 
                 
         
         });


function embaralhaPecas(qtd_pares){

    var imagensPossiveis = new Array();
    var imagensPossiveis_aux = new Array();
    var posicoesPossiveis = new Array();
    
    var posicoes;
    
    
    for(var i = 0; i < NUM_IMAGENS; i++){
        imagensPossiveis[i] = i;
    }
    
    shuffle(imagensPossiveis);
    
    for(var i = 0; i < qtd_pares; i++){
        imagensPossiveis_aux[imagensPossiveis_aux.length] = imagensPossiveis[i];
    }
    
    for(var i = 0; i < qtd_pares; i++){
        posicoesPossiveis[posicoesPossiveis.length] = imagensPossiveis_aux[i];
        
    }
    for(var i = 0; i < qtd_pares; i++){
        posicoesPossiveis[posicoesPossiveis.length] = imagensPossiveis_aux[i];
        
    }
    
    shuffle(posicoesPossiveis);
    
    console.log(posicoesPossiveis);
    
    return posicoesPossiveis;
    
    
}

    app.listen(3000);



