/**
 * Created by JonIC on 2016-11-10.
 */

// function : receive add friend notification and accept response notification.
url = require('url');
exports.addfriend = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;
    //var request = url_parts.query.request;
    //var checkedtime = url_parts.query.checkedtime;

    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    var name1 = req.body.name1;
    var sendfacebookid =  req.body.sendfacebookid;
    var request =  req.body.request;
    var checkedtime =  req.body.checkedtime;
    var sender_name = req.body.sender_name;


    if(request == "requestfriend"){
        // add notification about this message
        var sendtime = "10";// new Date.toString();
        var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval,sender_name) VALUES ('" +
            sendfacebookid + "', '"+ facebookid +"', 'requestfriend', '" + sendtime +"', '" + "" + "', '" + sender_name + "')";
        global.mysql.query(newfeedquery, function(err, newresult){
            if(err){

            }
            var data = {};
            data.retcode=200;
            data.error_msg = "";
            data.content = "success";
            res.send(200, data);

        });
    } else if(request=="acceptfriend"){
//delete from notification where sender='4' and destination='1' and notekind='matchRequest'

        var delquery = "DELETE FROM notification WHERE sender='"+facebookid+"', ' AND destination = '"+sendfacebookid +", AND sendtime='"+checkedtime + "')";
        global.mysql.query(delquery, function(err, delresult){
           if(err){

           }
            // delete the match
            //var deletematch1 = "DELETE FROM matching WHERE facebookid1='" + facebookid +"' AND facebookid2='" + sendfacebookid +"'"
            //global.mysql.query(deletematch1, function(er,result){
            //    console.log(result);
            //});
            //var deletematch2 = "DELETE FROM matching WHERE facebookid1='" + sendfacebookid +"' AND facebookid2='" +facebookid  +"'"
            //global.mysql.query(deletematch2, function(er,result){
            //    console.log(result);
            //});
            // see if there is friend with the same facebookid.
            var samequery = "SELECT facebookid1 FROM friend  WHERE facebookid1='" + facebookid + "' AND facebookid2='" + sendfacebookid
                + "' OR facebookid1='" + sendfacebookid + "' AND facebookid2='" + facebookid + "'" ;
            global.mysql.query(samequery, function(err, sameresult){
                if(err){

                }
                if(sameresult.length > 0){
                    // already exist
                    var data = {};
                    data.retcode=203
                    data.error_msg = "Already is friend of you."
                    data.content = "";
                    res.send(200, data);

                }else{

                    // insert added friend notification
                    var added1 = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('" + facebookid + "', '"+sendfacebookid +
                        "', 'acceptfriend', '"  +sendtime+ "', '" + "" + "', '" + sender_name + "')";
                    var added2 = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('" + sendfacebookid + "', '"+facebookid   +
                        "', 'acceptfriend', '"  +sendtime+ "', '" + "" + "', '" + sender_name + "')";
                    global.mysql.query(added1, function(er,result){
                        //var data = {};
                        //data.retcode=200;
                        //data.error_msg = "";
                        //data.content = "success";
                        //res.send(200, data);

                        console.log(result);
                    });
                    global.mysql.query(added2, function(er,result){
                        //var data = {};
                        //data.retcode=200;
                        //data.error_msg = "";
                        //data.content = "success";
                        //res.send(200, data);
                        console.log(result);
                    });

                    //INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('a', 'g', 'matchRequest', '0')
                    var addfriend ="INSERT INTO friend (facebookid1, facebookid2, name1, name2) VALUES ('"
                        + facebookid + "', '"+sendfacebookid +"', '"+name1+"', '"+sender_name+"')";
                    global.mysql.query(addfriend, function(err, addresult){
                        if(err){

                        }
                        // here you should notify the both side.

                        var data = {};
                        data.retcode=200;
                        data.error_msg = "";
                        data.content = "success";
                        res.send(200, data);
                    });

                }

            });

        });

    }else if(request=="friendadd"){ // request to be friend with you. // add notification table.

        var query = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('"
            + sendfacebookid + "', '" + facebookid + "', 'friendadd', '10', '" + "" + "', '" + sender_name + "')";
        global.mysql.query(query, function(err, result){
            var data1 = {};
            var ret = {};
            //ret.sender = sendfacebookid;
            //ret.destination = facebookid;
            //ret.notekind = "friendadd";
            //data1.retcode = 234;
            //data1.error_msg = "";
            //data1.content = ret;
            //global.io.sockets.in(facebookid).emit("notification", data1);

            var data = {};
            data.retcode=200;
            data.error_msg = "";
            data.content = "success";
            res.send(200, data);
        });
    }else if(request == "friendaccept"){ // if someone accept the friend request
        var queryadd = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('"
            + sendfacebookid + "', '" + facebookid + "', 'friendaccept', '10', '" + "" +"', '" + sender_name + "')";
            // add notification table as the receivers notification
        global.mysql.query(queryadd, function(err, result){
            if(err){
                var data = {};
                data.retcode=201;
                data.error_msg = "";
                data.content = "fail";
                res.send(200, data);

            }
            var data1 = {};
            var ret = {};
            data1.retcode = 200;
            data1.error_msg = "";
            data1.content = ret;
            res.send(200, data1);
            //global.io.sockets.in(facebookid).emit("notification", data1);

        });

        // add friend table
        var queryfriend = "INSERT INTO friend (facebookid1, facebookid2, name1, name2) VALUES ('"
            + sendfacebookid + "', '" + facebookid + "', '"+ sender_name +"', '" + name1 + "')";
        global.mysql.query(queryfriend, function(err, result){
            if(err){
                //var data = {};
                //data.retcode=201;
                //data.error_msg = "";
                //data.content = "fail";
                //res.send(200, data);

            }
            //var data = {};
            //data.retcode=200;
            //data.error_msg = "";
            //data.content = "success";
            //res.send(200, data);

        });
    }
}