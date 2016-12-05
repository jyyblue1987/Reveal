/**
 * Created by JonIC on 2016-11-26.
 */
url = require('url');
exports.getnotification = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    var queryprofile = "SELECT profilephoto FROM users WHERE facebookid='"+facebookid+"'";

    var notequery = "SELECT * FROM notification WHERE destination='" + facebookid + "' AND state='0'";

    var notequery2 = "SELECT * FROM " +
        "notification LEFT JOIN users " +
        " ON notification.sender = users.facebookid" +
        " OR notification.destination = users.`facebookid`" +
        " WHERE destination = '"+facebookid+"'" +
        " AND facebookid NOT IN('"+facebookid+"')" +
        "AND state =0";
    global.mysql.query(queryprofile, function(err, resultpro){
        var profile;
        if(err){
            profile = "";
        }else{
            if(resultpro.length == 0){
                profile = "";
            }else{
                profile = resultpro[0].profilephoto;
            }
        }

        global.mysql.query(notequery2, function (err, result) {
            var data = {};

            if (err) {
                data.retcode = 300;
                data.profile = profile;
                data.error_msg = "sql error";
                return res.send(200, data);

            }
            data.retcode = 200;
            data.error_msg = "";
            data.profile = profile;
            data.content = result;
            return res.send(200, data);
        });
    });


}