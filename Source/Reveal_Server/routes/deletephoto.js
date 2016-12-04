/**
 * Created by JonIC on 2016-12-03.
 */

url = require('url');
exports.deletephoto = function(req, res){
    console.log(req);
    var facebookid       = req.body.facebookid;
    var photopath        = req.body.photopath;
    var query = "DELETE FROM photo WHERE facebookid='"+facebookid+"' AND photopath='"+photopath+"'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql_server_error";
            res.send(200,data);  // end point

        }
        var data = {};
        data.retcode = 200;
        res.send(200,data);

    });
}
