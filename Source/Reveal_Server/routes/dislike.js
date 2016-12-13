/**
 * Created by JonIC on 2016-12-10.
 */

url = require('url');
exports.dislike = function(req, res) {
    console.log(req);

    var sendfacebookid  = req.body.sendfacebookid;
    var sendname            = req.body.sendname;
    var sendprofilephoto    = req.body.sendprofilephoto;

    var facebookid      = req.body.facebookid;
    var photopath    = req.body.photopath;

    var substring2 = sendfacebookid + "&" + sendname + "&" + sendprofilephoto + "^";

    var query2 =
        "UPDATE" +
        "    photo" +
        "    SET" +
        "    likenum = likenum - 1," +
        "        likefacebookid = REPLACE(" +
        "            likefacebookid," +
        "            '"+substring2+"'," +
        "            ''" +
        "        )" +
        "    WHERE facebookid = '"+facebookid+"'" +
        "    AND photopath = '"+photopath+"'" +
        "    AND likenum > '0'" +
        "    AND likefacebookid LIKE '%"+substring2+"%'";

    global.mysql.query(query2, function(err,result1){
        if(err){

            var data = {};
            data.retcode = 300;
            data.error_msg = "sql error";
            return res.send(200,data);
        }

        var data = {};
        data.retcode = 200;
        data.error_msg = "";
        return res.send(200,data);

    });
}
