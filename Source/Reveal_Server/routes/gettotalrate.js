/**
 * Created by JonIC on 2016-11-28.
 */
url = require('url');
exports.gettotalrate = function(req, res) {
    console.log(req);


    var facebookid      =req.body.facebookid;
    var query = "SELECT SUM(ratenumber) AS num, SUM(ratesum) AS sum FROM photo WHERE facebookid='"+facebookid+"'";
    global.mysql.query(query, function(err, result){
        if(err){

        }
        var ret = result[0];
        var ratenum = ret.num;
        var ratesum = ret.sum;
        var rate = parseFloat(ratesum/ratenum);
        var data = {};
        data.retcode = 200;
        data.rate = rate;
        data.error_msg = "";
        return res.send(200,data);
    });

}
