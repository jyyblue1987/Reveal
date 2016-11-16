/**
 * Created by JonIC on 2016-11-10.
 */

    // get matches profile (facebookid, photopath, ratesum, ratenumber, commentnum, likenum, )
url = require('url');
exports.matchprofile = function(req, res) {

    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;

    var facebookid = rep.body.facebookid;
    var sendfacebookid = req.body.sendfacebookid;

    var isMatch = "";
    // if they are matches
    var query_match_1 = "SELECT facebookid2 FROM matching WHERE facebookid1='" + facebookid + "' AND facebookid2='" + sendfacebookid + "'";
    var query_match_2 = "SELECT facebookid1 FROM matching WHERE facebookid2='" + facebookid + "' AND facebookid1='" + sendfacebookid + "'";
    var match1 = "";
    var match2 = "";
    global.mysql.query(query_match_1, function (err, rows2) {
        if (err) {
            console.error(err);

            var data = {};
            data.retcode = 300;
            data.error_msg = "Error sql server";
            return res.send(200,data);
            // here finish if there is an database error.
            //res.send(200, "database error");  // end point
        }

        if (rows2.length > 0) {
            var newfeedquery = "SELECT facebookid, photopath, ratesum, ratenumber, commentnum, likenum FROM photo WHERE facebookid='" + facebookid + "'";
            global.mysql.query(newfeedquery, function (err, newresult) {
                if (err) {
                    var data = {};
                    data.retcode = 300;
                    data.error_msg = "Error sql server";
                    return res.send(200,data);
                    //res.send(200, "database error");
                }
                console.log(newfeedquery);
                var data = {};
                data.retcode = 200;
                data.error_msg = "";
                data.content = newresult;
                //res.json(data);
                return res.send(200,data);
                //res.send(200, "success");
            });
        }else{

            global.mysql.query(query_match_2, function (err, rows3) {
                if (err) {
                    console.error(err);
                    var data = {};
                    data.retcode = 300;
                    data.error_msg = "Error sql server";
                    return res.send(200,data);
                    // here finish if there is an database error.
                    //res.send(200,"database error");  // end point
                }

                if (rows3.length > 0 ) {
                    var newfeedquery = "SELECT facebookid, photopath, ratesum, ratenumber, commentnum, likenum FROM photo WHERE facebookid='" + facebookid + "'";
                    global.mysql.query(newfeedquery, function (err, newresult) {
                        if (err) {
                            var data = {};
                            data.retcode = 300;
                            data.error_msg = "Error sql server";
                            return res.send(200,data);
                            //res.send(200, "database error");
                        }
                        console.log(newfeedquery);
                        var data = {};
                        data.retcode = 200;
                        data.error_msg = "";
                        data.content = newresult;
                        //res.json(data);
                        return res.send(200,data);
                        //res.send(200, "success");
                    });
                }else{

                    var data = {};
                    data.retcode = 400;
                    data.error_msg = "No such man";
                    return res.send(200,data);

                }
            });
        }
    });
}