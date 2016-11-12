/**
 * Created by JonIC on 2016-11-10.
 */
url = require('url');
exports.addfriend = function(req, res) {
    console.log(req);
    var url_parts = url.parse(req.url, true);
    var facebookid = url_parts.query.facebookid;
    var sendfacebookid = url_parts.query.sendfacebookid;
    var saying = url_parts.query.saying;
    var checkedtime = url_parts.query.checkedtime;

    if(saying == "requestfriend"){
        // add notification about this message
        var sendtime = 10;
        var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" +
            sendfacebookid + "', '"+ facebookid +"', 'requestfriend', '" + sendtime +"')";
        global.mysql.query(newfeedquery, function(err, newresult){
            if(err){

            }
            console.log(newfeedquery);
            res.send(200, "success");

        });
    } else {
//delete from notification where sender='4' and destination='1' and notekind='matchRequest'

        var delquery = "DELETE FROM notification WHERE sender='"+facebookid+"', ' AND destination = '"+sendfacebookid +", AND sendtime='"+checkedtime + "')";
        global.mysql.query(delquery, function(err, delresult){
           if(err){

           }
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