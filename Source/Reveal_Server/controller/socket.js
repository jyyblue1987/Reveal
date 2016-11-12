
global.io.sockets.on('connection',function(socket){
    var room_id = '';
    socket.on('joinRoom',function(data){
        room_id = data;
        socket.join(room_id);
        sendNotification(room_id);
        console.log('JOIN ROOM LIST', global.io.sockets.adapter.rooms);
    });
    socket.on('leaveRoom',function(){
        socket.leave(room_id);
        console.log('OUT ROOM LIST', global.io.sockets.adapter.rooms);
    });

    socket.on('call_event',function(data){
        socket.broadcast.emit('call_event',data);
    })

    // my code
    socket.on('chat_message',function(data){
        //var parsedBody = JSON.parse(data);
        var sender = data.name;
        var message = data.message;
        var receiver =data.receiver;
        var ddd = {} ;
        ddd.name = receiver;
        ddd.message = message + "-Reply";
        ddd.receiver = sender;
        global.io.sockets.in(receiver).emit("chat_message", data);
        //global.io.sockets.in(sender).emit("chat_message", ddd);
        //socket.broadcast.emit('call_event',data);
    })

        //
    //socket.on('notify_on',function(data){
    //  socket.broadcast.emit('notify_on_' + data.property_id,data);
    //});
});

sendNotification = function(facebookid){
    var notequery = "SELECT * FROM notification WHERE destination='"+facebookid+"'";
    global.mysql.query(notequery, function(err, result){
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        data.content = result;
        //res.json(data);
        global.io.sockets.in(facebookid).emit("notification", data);
        console.log("Sending data: ",data);
        // here delete that notifications.
        var delquery = "DELETE  FROM notification WHERE destination='"+facebookid+"'";
        global.mysql.query(delquery, function(err, delresult){

        });
    });
}
//global.io.sockets.in(room).emit(data.content.type, data);