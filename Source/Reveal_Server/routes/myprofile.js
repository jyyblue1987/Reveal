/**
 * Created by JonIC on 2016-11-10.
 */
url = require('url');

exports.myprofile = function(req, res){
    console.log(req);
    var url_parts = url.parse(req.url, true);
    var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;
    //post
    //var facebookid = req.body.facebookid;

    // if they are not match and friends.

    var photoquery = "SELECT * FROM photo WHERE facebookid='" + facebookid + "' ORDER BY Id DESC";
    global.mysql.query(photoquery, function (err, result) {
        if (err) {
            var data = {};
            data.retcode = 300;
            data.error_msg = "Error sql server";
            return res.send(200,data);
        }
        if (result.length > 0 ) {
            var data = {};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            return res.send(200,data);

        }else{
            var data = {};
            data.retcode = 400;
            data.error_msg = "No such man";
            return res.send(200,data);

        }
    });
}/**
 * Created by JonIC on 2016-11-12.
 */
exports.myprofile_post = function(req, res){
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;
    //post
    var facebookid = req.body.facebookid;

    // if get user's registerd information in user table.

    var photoquery = "SELECT * FROM photo WHERE facebookid='" + facebookid +"' ORDER BY Id DESC";
    global.mysql.query(photoquery, function (err, result) {
        if (err) {
            var data = {};
            data.retcode = 300;
            data.error_msg = "Error sql server";
            return res.send(200,data);
        }
        if (result.length > 0 ) {
            var data = {};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            return res.send(200,data);

        }else{
            var data = {};
            data.retcode = 400;
            data.error_msg = "No such man";
            return res.send(200,data);

        }
    });
}/**
 * Created by JonIC on 2016-11-12.
 */
