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
    var sendfacebookid =  req.body.sendfacebookid;
    var request =  req.body.request;
    var checkedtime =  req.body.checkedtime;


    if(request == "requestfriend"){
        // add notification about this message
        var sendtime = "10";// new Date.toString();
        var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" +
            sendfacebookid + "', '"+ facebookid +"', 'requestfriend', '" + sendtime +"')";
        global.mysql.query(newfeedquery, function(err, newresult){
            if(err){

            }
            console.log(newfeedquery);
            res.send(200, "success");

        });
    } else if(request=="acceptfriend"){
//delete from notification where sender='4' and destination='1' and notekind='matchRequest'

        var delquery = "DELETE FROM notification WHERE sender='"+facebookid+"', ' AND destination = '"+sendfacebookid +", AND sendtime='"+checkedtime + "')";
        global.mysql.query(delquery, function(err, delresult){
           if(err){

           }
            // delete the match
            var deletematch1 = "DELETE FROM matching WHERE facebookid1='" + facebookid +"' AND facebookid2='" + sendfacebookid +"'"
            global.mysql.query(deletematch1, function(er,result){
                console.log(result);
            });
            var deletematch2 = "DELETE FROM matching WHERE facebookid1='" + sendfacebookid +"' AND facebookid2='" +facebookid  +"'"
            global.mysql.query(deletematch2, function(er,result){
                console.log(result);
            });
            // insert added friend notification
            var added1 = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" + facebookid + "', '"+sendfacebookid +
                "', 'acceptfriend', '"  +sendtime+ "')";
            var added2 = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" + sendfacebookid + "', '"+facebookid   +
                "', 'acceptfriend', '"  +sendtime+ "')";
            global.mysql.query(added1, function(er,result){
                console.log(result);
            });
            global.mysql.query(added2, function(er,result){
                console.log(result);
            });

            //INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('a', 'g', 'matchRequest', '0')
            var addfriend ="INSERT INTO friend (facebookid1, facebookid2) VALUES ('" + facebookid + "', '"+sendfacebookid +"')";
            global.mysql.query(addfriend, function(err, addresult){
                if(err){

                }
                // here you should notify the both side.

                var data = {};
                data.friends = "jyyblue";
                res.send(200, data);
            });
        });

    }
}