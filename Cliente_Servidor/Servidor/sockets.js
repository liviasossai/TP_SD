var express = require('express');
var http = require('http').Server(express).listen(3001);
var io_server = require('socket.io')(http);
var io_client1 = require('socket.io-client')('http://localhost:4001');
var io_client2 = require('socket.io-client')('http://localhost:5001');

//Socket Servidor
io_server.on('connection', function(socket){
  console.log('a user connected');
  socket.on('disconnect', function(){
    console.log('user disconnected');
  });
});


//Socket cliente
io_client1.on('connect', function () { 
    console.log("socket connected with server 4001");
    io_client1.emit('message');
    io_client1.on('message', function(msg){
        console.log(msg);
    });
});
io_client2.on('connect', function () { console.log("socket connected with server 5001"); });
