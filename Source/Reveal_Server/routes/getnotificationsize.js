/**
 * Created by JonIC on 2016-11-22.
 */
url = require('url');
exports.getnotificationsize = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    //select count(*) from notification where notification='333379007041276' and state='0'
    var query = "SELECT count(*) FROM notification WHERE destination ='" + facebookid + "' AND state='0'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 210;
            data.error_msg = "error";
            data.content = "";
            return res.send(200,data);

        }
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        data.content = result;
        return res.send(200,data);
    });

}