/**
 * Created by JonIC on 2016-11-23.
 */
url = require('url');
exports.findbyname = function(req, res) {
    console.log(req);
    //var url_parts        = url.parse(req.url,true);
    //var facebookid       = url_parts.query.facebookid;
    //var sendfacebookid   = url_parts.query.sendfacebookid;
    var name = req.body.username;
    var query = "SELECT * FROM users WHERE name='" + name + "'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 220;
            data.content = "";
            data.error_msg="error";
            return res.send(200,data);

        }
        var data = {};
        data.retcode = 200;
        data.content = result;
        data.error_msg="";
        return res.send(200,data);
    });
}
