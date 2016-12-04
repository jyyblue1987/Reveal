/**
 * Created by JonIC on 2016-12-03.
 */

url = require('url');
exports.isphoto = function(req, res) {
    console.log(req);


    var facebookid      =req.body.facebookid;
    var query = "SELECT count(*) AS size FROM photo WHERE facebookid ='" + facebookid + "'";
    global.mysql.query(query, function(err, result) {
        if(err){
            var data1 = {};
            data1.retcode = 200;
            data1.size = "0";
            return res.send(200, data1);

        }
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        data.size = result[0].size;
        return res.send(200, data);

    });
}