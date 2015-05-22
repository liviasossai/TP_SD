var express = require('express');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');
var routes = require('./routes/index');
var users = require('./routes/users');
//Socket servidor
var http = require('http').Server(express).listen(4001);
var io_server = require('socket.io')(http);
//Socket clientes
var io_client = require('socket.io-client')('http://localhost:5001');


var app = express();



app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));

app.use('/', routes);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function (err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});


/****************** Sockets *********************/

var INIC_ELEICAO; // Variavel utilizada para testes -> indica, manualmente, qual no' ira iniciar a eleicao

var id = 4;
var id_forward = 4;
var elected = null;

console.log('---------------------------------------');
console.log('    Eu sou o servidor de numero 2      ');
console.log('    MEU ID EH '+id);
console.log('---------------------------------------');

//Socket servidor
io_server.on('connection', function (socket) {
    console.log('a user connected');

    socket.on('inic_eleicao',function(port_received){
      if(port_received == true){
        io_client.emit('election', id_forward);
        console.log('Servidor 2 iniciou a eleicao');
        console.log('Servidor 2 enviou id = ' + id_forward + ' para o servidor de numero 3');
      }
    });

    socket.on('election', function (id_received) {

        if (id == id_received) {
            elected = id;
            io_client.emit('id_elected', elected);
            console.log('Servidor 2 comecou o elected');
        } else {
            if (id > id_received) {
                id_forward = id;
            } else {
                if (id < id_received) {
                    id_forward = id_received;
                }
            }
            io_client.emit('election', id_forward);

        }
        console.log('Servidor 2 recebeu id = ' + id_received + ' do servidor de numero 1');
        console.log('Servidor 2 enviou id = ' + id_forward + ' para o servidor de numero 3');

    });


    socket.on('id_elected', function (id_elected) {
        if (id == id_elected) {
            console.log('Eu sou o lider');
            var io_client2 = require('socket.io-client')('http://localhost:7001');
            io_client2.emit('elected', 'localhost', '4000');
        } else {
            elected = id_elected;
            io_client.emit('id_elected', elected);
            console.log('Enviado elected = ' + elected);
        }
    });

    socket.on('disconnect', function () {
        console.log('user disconnected');
    });
});


//Socket cliente
io_client.on('connect', function () {
    console.log("socket connected with server 5001");
});


//Servidor 1 sempre comeca a eleicao
//setTimeout executa uma vez depois de um tempo (em milisegundos)
//setInterval executa a funcao em intervalos de tempo definidos (em milisegundos)
/*if(INIC_ELEICAO){
setTimeout(function () {
    io_client.emit('election', id_forward);
    console.log('Servidor 2 iniciou a eleicao');
    console.log('Servidor 2 enviou id = ' + id_forward + ' para o servidor de numero 3');
}, 10000);

setInterval(function () {
    io_client.emit('election', id_forward);
    console.log('Servidor 1 iniciou a eleicao');
    console.log('Servidor 1 enviou id = ' + id_forward + ' para o servidor de numero 2');
}, 60000);

}*/
/*********************************************/
module.exports = app;
