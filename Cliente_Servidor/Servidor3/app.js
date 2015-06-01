var express = require('express');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');
var routes = require('./routes/index');
var users = require('./routes/users');
//Socket servidor
var http = require('http').Server(express).listen(5001);
var io_server = require('socket.io')(http);


var app = express();

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));

app.use('/', routes);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


// ------- Variaveis empregadas no gossip --------------

var http2 = require('http').Server(express).listen(5003); // Porta da qual recebera a tabela de alguem
var recebe_tab_gossip = require('socket.io')(http2);

var servidores_possiveis = [1, 2, 4]; // Cada elemento eh o numero do servidor (para tornar a escolha da porta mais flexivel, caso a convencao mude)

shuffle(servidores_possiveis); // embaralha o vetor e seleciona o primeiro e segundo elementos

var porta_envia_gossip1 = 3000 + (servidores_possiveis[0] - 1) * 1000 + 3; // Calculo da porta, de acordo com o numero do servidor escolhido
var porta_envia_gossip2 = 3000 + (servidores_possiveis[1] - 1) * 1000 + 3; // Calculo da porta, de acordo com o numero do servidor escolhido

console.log('O servidor 3 ira enviar sua tabela gossip')
console.log('para o servidor ' + servidores_possiveis[0] + ' na porta ' + porta_envia_gossip1 + ' e');
console.log('para o servidor ' + servidores_possiveis[1] + ' na porta ' + porta_envia_gossip2);



var envia_tab_gossip1 = require('socket.io-client')('http://localhost:' + porta_envia_gossip1);
var envia_tab_gossip2 = require('socket.io-client')('http://localhost:' + porta_envia_gossip2);

// Onde será armazenada a tabela
var tab_gossip = {}; // Armazena o numero de heartbeat dos outros servidores
var tab_gossip_tempo_local = {}; // Armazena o horário local quando foi realizada a última mudança na tabela


var tempo_max_atualizacao = 30; // Se passaram 30s desde a última atualização, então houve um erro no servidor


// -------- Timer para envio de tabela gossip --------- //


var tempo_local = 0;

setInterval(function() {
  tempo_local++;
  //console.log(tempo_local);

  if (tempo_local % 10 == 0) {
    console.log('Enviando tabela gossip para servidor ' + servidores_possiveis[0] + ' na porta ' + porta_envia_gossip1);
    console.log('Enviando tabela gossip para servidor ' + servidores_possiveis[1] + ' na porta ' + porta_envia_gossip2);
    envia_tab_gossip1.emit('gossip', '3', tempo_local, tab_gossip);
    envia_tab_gossip2.emit('gossip', '3', tempo_local, tab_gossip);
  }

  if (tempo_local % 5 == 0) { // A cada 5s verifica se alguém excedeu o tempo máximo de atualização
    for (var i = 0; i < servidores_possiveis.length; i++) {
      if (tempo_local - tab_gossip_tempo_local[servidores_possiveis[i]] > tempo_max_atualizacao) {
        console.log('Detectada falha no servidor ' + servidores_possiveis[i]);
        tab_gossip_tempo_local[servidores_possiveis[i]] = undefined;
        var io_client2 = require('socket.io-client')('http://localhost:7001');
        io_client2.emit('failed', servidores_possiveis[i]);
        setTimeout(function() {
          io_client2.disconnect();
        }, 1000);
      }
    }

  }

}, 1000);


// -------- Recebimento da Tabela Gossip --------- //

recebe_tab_gossip.on('connection', function(socket) {
  socket.on('gossip', function(serv, heartbeat, tab) {
    console.log('Recebi tabela de gossip do servidor ' + serv);
    merge(tab, servidores_possiveis, tempo_local);
    tab_gossip[serv] = heartbeat;
    tab_gossip_tempo_local[serv] = tempo_local;

    console.log(tab_gossip_tempo_local);
  });
});


// Merge dos heartbeats

function merge(tab_fora, serv_possiveis, t_local) {

  for (var i = 0; i < serv_possiveis.length; i++) {
    if (tab_gossip[serv_possiveis[i]] == undefined && tab_fora[serv_possiveis[i]] != undefined) {
      tab_gossip[serv_possiveis[i]] = tab_fora[serv_possiveis[i]];
      tab_gossip_tempo_local[serv_possiveis[i]] = t_local;
    } else if (tab_gossip[serv_possiveis[i]] < tab_fora[serv_possiveis[i]] && tab_fora[serv_possiveis[i]] != undefined) {
      tab_gossip[serv_possiveis[i]] = tab_fora[serv_possiveis[i]];
      tab_gossip_tempo_local[serv_possiveis[i]] = t_local;
    }
  }


}

/* ---------------------------------------- Fim - Gossip ----------------------------------------- */




/*---------------------------------------- Eleicao ----------------------------------------*/

var INIC_ELEICAO; // Variavel utilizada para testes -> indica, manualmente, qual no' ira iniciar a eleicao

var id = 3;
var id_forward = 3;
var elected = null;
var io_client = null;
var server_index = 0;
var chosen_server = 0;
var server_ports = [6001, 3001, 4001];

//Socket cliente
function Connect_servers(callback) {
    server_index = 0;
    io_client = require('socket.io-client')('http://localhost:' + server_ports[server_index]);

    io_client.on('connect', function() {
        console.log("socket connected with server " + server_ports[server_index]);
        callback();
    });

    io_client.on('connect_error', function() {
        console.log("socket not connected with server " + server_ports[server_index]);
        io_client.disconnect();
        next_server(callback);
    });

}


function next_server(callback) {
    server_index = server_index + 1;
    if (server_index < 3) {
        io_client = require('socket.io-client')('http://localhost:' + server_ports[server_index]);
        io_client.on('connect', function() {
            console.log("socket connected with server " + server_ports[server_index]);
            callback();
        });

        io_client.on('connect_error', function() {
            console.log("socket not connected with server " + server_ports[server_index]);
            io_client.disconnect();
            next_server(callback);
        });
    } else {
        console.log('Nenhum server ativo, impossivel conectar!');
    }

}



console.log('---------------------------------------');
console.log('    Eu sou o servidor de numero 3      ');
console.log('    MEU ID EH ' + id);
console.log('---------------------------------------');




//Socket servidor
io_server.on('connection', function(socket) {
    console.log('a user connected');

    socket.on('inic_eleicao', function(port_received) {
        if (port_received == true) {
            Connect_servers(init_elect);

            function init_elect() {
                id_forward = id;
                io_client.emit('election', id_forward);
                console.log('Servidor 3 iniciou a eleicao');
                console.log('Servidor 3 enviou id = ' + id_forward + ' para o servidor ' + server_ports[server_index]);
            }
        }
    });

    socket.on('election', function(id_received) {
        if (io_client == null) {
            Connect_servers(elect);
        } else {
            elect();
        }

        function elect() {

            console.log('Servidor 3 recebeu id = ' + id_received);
            if (id == id_received) {
                elected = id;
                io_client.emit('id_elected', elected);
                console.log('Servidor 3 comecou o elected');
            } else {
                if (id > id_received) {
                    id_forward = id;
                } else {
                    if (id < id_received) {
                        id_forward = id_received;
                    }
                }
                io_client.emit('election', id_forward);
                console.log('Servidor 3 enviou id = ' + id_forward + ' para o servidor '+ server_ports[server_index]);
            }
        }

    });


    socket.on('id_elected', function(id_elected) {
        if (id == id_elected) {
            console.log('Eu sou o lider');
            var io_client2 = require('socket.io-client')('http://localhost:7001');
            io_client2.emit('elected', 'localhost', '5000');
            setTimeout(function() {
                io_client2.disconnect();
            }, 1000);
        } else {
            elected = id_elected;
            io_client.emit('id_elected', elected);
            console.log('Enviado elected = ' + elected);
        }

    });

    socket.on('disconnect', function() {
        console.log('user disconnected');
    });
});

/*---------------------------------------- Fim - Eleicao ----------------------------------------*/



function shuffle(o) {
    for (var j, x, i = o.length; i; j = Math.floor(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x);
    return o;
}

module.exports = app;
