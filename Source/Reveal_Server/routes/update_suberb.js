/**
 * Created by JonIC on 2016-12-16.
 */

exports.update_suberb  = function(req, res) {
    console.log(req);

    var facebookid = req.body.facebookid;
    var suberb = req.body.suberb;
    var query = "" +
        "UPDATE" +
        "    users" +
        "    SET" +
        "    suberb = '"+suberb+"' " +
        "    WHERE facebookid = '"+facebookid+"' ";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg="sql error";
            return res.send(200, data);
        }
        data = {};
        data.retcode = 200;
        data.content="success";
        return res.send(200, data);
    });
}