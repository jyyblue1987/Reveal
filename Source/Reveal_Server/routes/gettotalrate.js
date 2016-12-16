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
            var query2 = "UPDATE users SET totalrate='"+rate+"' WHERE facebookid='"+facebookid+"'";
        global.mysql.query(query2, function(err, result2){    });

        if(ratenum != null && ratenum != 0){
            data.retcode = 200;
            data.rate = rate;
            data.error_msg = "";
            return res.send(200,data);
        }else{
            data.retcode = 300;
            data.rate = "0";
            data.error_msg = "there is no photo";
            return res.send(200,data);
        }
    });

}
