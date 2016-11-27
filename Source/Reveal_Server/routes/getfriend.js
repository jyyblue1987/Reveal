/**
 * Created by JonIC on 2016-11-11.
 */
/**
 * Created by JonIC on 2016-11-11.
 */

 // get ones all friend from friend table.
url = require('url');
exports.getfriend = function(req, res){
    console.log(req);
// get
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
// post
    var facebookid = req.body.facebookid;

    var query_1 = "SELECT * FROM friend WHERE facebookid1='"+facebookid+"' OR facebookid2='" + facebookid + "'";
    global.mysql.query(query_1, function(err, result){
        if(err){
        }
        if(result.length > 0){
            var data = {};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            //res.json(data);
            return res.send(200,data);

        }else{
            var data = {};
            data.retcode = 201;
            data.content = "999";
            data.error_msg = "No Friend";
            //res.json(data);
            return res.send(200,data);
            // return sql server error

        }
    });
}


