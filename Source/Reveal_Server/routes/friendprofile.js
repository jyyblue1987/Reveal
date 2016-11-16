/**
 * Created by JonIC on 2016-11-10.
 */
// get friend profile ( photos data ) given facebook id.
url = require('url');
exports.friendprofile = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;
    //var iswho = url_parts.query.iswho;
    var facebookid = req.body.facebookid;
    var sendfacebookid = req.body.sendfacebookid;
    var iswho = req.body.iswho;

    // if they are friends.
    var query_friend_1 = "SELECT facebookid2 FROM friend WHERE facebookid1='" + facebookid + "' AND facebookid2='" + sendfacebookid + "'";
    var query_friend_2 = "SELECT facebookid1 FROM friend WHERE facebookid2='" + facebookid + "' AND facebookid1='" + sendfacebookid + "'";
    var friend1 = "";
    var friend2 = "";
    global.mysql.query(query_friend_1, function (err, rows2) {
        if (err) {
            console.error(err);
            var data = {};
            data.retcode = 300;
            data.error_msg = "Error sql server";
            return res.send(200,data);
        }

        if (rows2.length > 0 ) {
            var newfeedquery = "SELECT * FROM photo WHERE facebookid='" + facebookid + "'";
            global.mysql.query(newfeedquery, function (err, newresult) {
                if (err) {
                    //res.send(200, "database error");
                    var data = {};
                    data.retcode = 300;
                    data.error_msg = "Error sql server";
                    return res.send(200,data);

                }
                console.log(newfeedquery);
                var data = {};
                data.retcode = 200;
                data.error_msg = "";
                data.content = newresult;
                //res.json(data);
                return res.send(200,data);
            });
        }else{

            global.mysql.query(query_friend_2, function (err, rows3) {
                if (err) {
                    console.error(err);
                    var data = {};
                    data.retcode = 300;
                    data.error_msg = "Error sql server";
                    return res.send(200,data);
                }

                if (rows3.length > 0 ) {
                    isfriendormatch = "friend";

                    var newfeedquery = "SELECT * FROM photo WHERE facebookid='" + facebookid + "'";
                    global.mysql.query(newfeedquery, function (err, newresult) {
                        if (err) {
                            //res.send(200, "database error");
                        }

                        console.log(newfeedquery);
                        var data = {};
                        data.retcode = 200;
                        data.error_msg = "";
                        data.content = newresult;
                        //res.json(data);
                        return res.send(200,data);
                    });
                }else{
                    var data = {};
                    data.retcode = 400;
                    data.error_msg = "No such man";
                    return res.send(200,data);

                }

            });


        }
        //else if(isfriendormatch != "friend"){
        //    //getmatch(facebookid, sendfacebookid);
        //}
    });
}
