/**
 * Created by JonIC on 2016-11-11.
 */
url = require('url');

// get and return only one person's data
exports.personinfo = function(req, res){
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;

    var facebookid = req.body.facebookid;
    var query = "SELECT * FROM users WHERE facebookid='"+facebookid+"'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.content = "";
            data.error_msg = "sql server error";
            //res.json(data);
            return res.send(200,data);

        }
        if(result.length > 0){
            var data ={};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            //res.json(data);
            return res.send(200,data);
        }else {
            var data = {};
            data.retcode = 201;
            data.error_msg = "No such person";
            return res.send(200,data);
        }
    });
}

