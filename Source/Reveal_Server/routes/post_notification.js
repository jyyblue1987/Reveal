/**
 * Created by JonIC on 2016-12-15.
 */

url = require('url');
exports.post_notification = function(req, res) {
    console.log(req);
// get
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
// post
    var facebookid = req.body.facebookid;
    var myfacebookid = req.body.myfacebookid;
    var err = false;
    var query1 = "UPDATE" +
        "    friend" +
        "    SET" +
        "    sendfeed1 = 'yes'" +
        "    WHERE facebookid1 = '"+myfacebookid+"'" +
        "    AND facebookid2 = '"+facebookid+"' ";
    var query2 = "UPDATE" +
        "    friend" +
        "    SET" +
        "    sendfeed2 = 'yes'" +
        "    WHERE facebookid1 = '"+facebookid+"'" +
        "    AND facebookid2 = '"+myfacebookid+"' "
    global.mysql.query(query1, function(err, result1){
        if(err){
            err = true;
        }
        err = false;
    });
    global.mysql.query(query2, function(err, result2){
        if(err){
            err = true;
        }
        err = false;
    });

    if(err == true){
        var data = {};
        data.retcode = 300;
        data.error_msg = "sql error";
        return res.send(200,data);

    }else {
        var data = {};
        data.retcode = 200;
        data.error_msg = "success";
        return res.send(200,data);

    }

}
