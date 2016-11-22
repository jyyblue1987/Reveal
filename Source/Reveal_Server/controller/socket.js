
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
        saveChatHistory(sender, message, receiver);
        var ddd = {} ;
        ddd.name = receiver;
        ddd.message = message + "-Reply";
        ddd.receiver = sender;
        global.io.sockets.in(receiver).emit("chat_message", data);
        //global.io.sockets.in(sender).emit("chat_message", ddd);
        //socket.broadcast.emit('call_event',data);
    })

    socket.on('chat_history', function(data){
        var sender = data.name;
        var receiver = data.receiver;
        sendChatHistory(sender, receiver);
    })

        //
    //socket.on('notify_on',function(data){
    //  socket.broadcast.emit('notify_on_' + data.property_id,data);
    //});
});

// when chat start with specified person.
sendChatHistory= function(sender, receiver){
    //SELECT * FROM photo where facebookid='1' and ratesum = '0' or facebookid='3' and ratesum > 0 ORDER BY ratesum ASC tiem increase.
    var query = "SELECT * FROM chat WHERE name='" + sender    +  "' AND receiver='" + receiver
                                   + "' OR name ='" + receiver + "' AND receiver='" + sender + "' ORDER BY time ASC"
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql error";
            data.content = "sql error";
            global.io.sockets.in(sender).emit("chat_history", data);
        }
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        data.content = result;
        global.io.sockets.in(sender).emit("chat_history", data);
    });
}
saveChatHistory=function(sender, message, receiver){
    //INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('a', 'g', 'matchRequest', '0')
    var date = new Date();
    var year = date.getYear();
    var month = date.getMonth();
    var dat = date.getDate();
    var hour = date.getHours();
    var min =  date.getMinutes();
    var second = date.getSeconds();
    var milise = date.getMilliseconds();
    //var time = milise + second * 1000 + min * 100000 + hour * 10000000 + hour * 1000000000 + dat * 100000000000 * month *  10000000000000 + year *  1000000000000000;
    var time = 0;
    var query = "INSERT INTO chat (name, receiver, message, time) VALUES ('" +sender + "', '" + receiver+ "', '"+ message + "', '" + time + "')";
    global.mysql.query( query, function(err, result){
        if(err){

        }
        console.log(query, "success");
    });
}

sendNotification = function(facebookid){
    var notequery = "SELECT * FROM notification WHERE destination='" + facebookid + "'";
    global.mysql.query(notequery, function(err, result){
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        data.content = result;
        //res.json(data);
        global.io.sockets.in(facebookid).emit("notification", data);
        console.log("Sending data: ",data);
        // here delete that notifications.
        //var delquery = "DELETE  FROM notification WHERE destination='"+facebookid+"'";
        //global.mysql.query(delquery, function(err, delresult){
        //
        //});
    });
}
//global.io.sockets.in(room).emit(data.content.type, data);