/**
 * Created by JonIC on 2016-12-15.
 */
exports.fullnamefriend  = function(req, res) {
    console.log(req);

    //var sendfacebookid = req.body.myfacebookid;
    var facebookid = req.body.facebookid;
    var query_2 = "SELECT" +
        "    facebookid" +
        "    FROM" +
        "    friend" +
        "    LEFT JOIN users" +
        "    ON friend.facebookid1 = users.facebookid" +
        "    OR friend.facebookid2 = users.facebookid" +
        "    WHERE facebookid1 = '"+facebookid+"'" +
        "    AND facebookid NOT IN ('"+facebookid+"')" +
        "    AND showfullname = 'yes'" +
        "    OR facebookid2 = '"+facebookid+"'" +
        "    AND facebookid NOT IN ('"+facebookid+"')" +
        "    AND showfullname = 'yes' ";
    global.mysql.query(query_2, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql errorr";
            data.content = result;
            //res.json(data);
            return res.send(200,data);

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
