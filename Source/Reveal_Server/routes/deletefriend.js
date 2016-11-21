/**
 * Created by JonIC on 2016-11-19.
 */
url = require('url');
exports.deletefriend = function(req, res) {
    console.log(req);
    //var url_parts        = url.parse(req.url,true);
    //var facebookid       = url_parts.query.facebookid;
    //var sendfacebookid   = url_parts.query.sendfacebookid;
    var facebookid = req.body.facebookid;
    var sendfacebookid = req.body.sendfacebookid;

    var query ="DELETE FROM friend WHERE facebookid1='" +facebookid + "' AND facebookid2='" + sendfacebookid
                                 + "' OR facebookid1='" +sendfacebookid+  "' AND facebookid2='" +facebookid +"'";
    global.mysql.query(query, function (err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.content = "fail";
            data.error_msg = "Error sql server";
            return res.send(200,data);

        }
        var data = {};
        data.retcode = 300;
        data.content = "ok";
        data.error_msg = "Error sql server";
        return res.send(200,data);

    });
}
