/**
 * Created by JonIC on 2016-11-26.
 */
url = require('url');
exports.blockmatch = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    var blockfacebookid = req.body.blockfacebookid;
    var blocksort = req.body.blocksort;
    var query = "INSERT INTO block (sender, blockfacebookid, blocksort) VALUES ('" + facebookid + "', '" + blockfacebookid + "', '" + blocksort + "')";
    var query1 = "DELETE from matching where (facebookid1='"+facebookid+"' and facebookid2='"+blockfacebookid+"')"+
                                      " or (facebookid1='"+blockfacebookid+"' and facebookid2='"+facebookid+"')";
    global.mysql.query(query1, function (err, result) {

    });
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql error";
            //res.json(data);
            return res.send(200,data);

        }
        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        //res.json(data);
        return res.send(200,data);

    });
}