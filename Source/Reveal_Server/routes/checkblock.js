/**
 * Created by JonIC on 2016-11-26.
*/
url = require('url');
exports.checkblock = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    var blockfacebookid = req.body.blockfacebookid;

    var query = "SELECT * FROM block WHERE sender='"+ facebookid + "' AND blockfacebookid='" + blockfacebookid + "'"
                                    + " OR sender='"+ blockfacebookid + "' AND blockfacebookid='" + facebookid + "'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql error";
            //res.json(data);
            return res.send(200,data);
        }
        if(result.length == 0){
            var data = {};
            data.retcode = 200;
            data.error_msg = "";
            //res.json(data);
            return res.send(200,data);
        }
        if(result.length == 2){
            var data = {};
            data.retcode = 301;
            data.error_msg = "";
            data.content = result[0];
            return res.send(200,data);
        }
        if(result.length == 1){
            var data = {};
            data.retcode = 301;
            data.error_msg = "";
            data.content = result[0];
            return res.send(200,data);
        }
    });
}