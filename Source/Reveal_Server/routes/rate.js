/**
 * Created by JonIC on 2016-12-02.
 */

url = require('url');
exports.rate = function(req, res){
    console.log(req);
    var url_parts = url.parse(req.url,true);
    //var
    var sendfacebookid = req.body.myfacebookid;
    var photopath      = req.body.photopath;
    var facebookid     =req.body.facebookid;
    var rating         = parseInt(req.body.rating);
    var report         = req.body.report;
    var sender_name    = req.body.sender_name;
    var name1          = req.body.name1;

    var responsematchrequest = req.body.responsematchrequest;

    // find photo with facebookid and photopath.
    var query = "SELECT * FROM photo WHERE facebookid='" + facebookid + "' AND photopath='" + photopath + "'";
    global.mysql.query(query, function(err, rows) {
        if(err){
            var data1 = {};
            data1.retcode = 300;
            data1.error_msg = "Could not find such photo.";
            res.send(200, data1);  // end point

        }
        // if there exist one photo.
        // there is only one such photo.
        if(rows.length > 0) {
            var ratesum = parseInt(rows[0].ratesum);
            var rateNumber = parseInt(rows[0].ratenumber);
            // report content
            var checkedreport;
            var reportkind = "";
            if (report == "Group Photo") {
                checkedreport = parseInt(rows[0].reportgp) + 1;
                reportkind = "reportgp";
            } else if (report == "Offensive Material") {
                checkedreport = parseInt(rows[0].reportom) + 1;
                reportkind = "reportom";
            } else if (report == "Not Actual Person") {
                checkedreport = parseInt(rows[0].reportnap) + 1;
                reportkind = "reportnap";
            } else if (report == "Wrong Gender") {
                checkedreport = parseInt(rows[0].reportwg) + 1;
                reportkind = "reportwg";
            } else if (report == "Feels Like Spam") {
                checkedreport = parseInt(rows[0].reportfls) + 1;
                reportkind = "reportfls";
            }

            if(reportkind == "")// case there in not report for the photo.
            {
                // update the photo table
                ratesum    = ratesum + rating;
                rateNumber = rateNumber + 1;
                var rate = ratesum/rateNumber;

                var updatequery = "UPDATE photo SET ratesum='"+ ratesum +"', "+ "ratenumber='" + rateNumber + "', rate='" + rate + "'"
                    +" WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(updatequery, function(err, udresult){
                    console.log(updatequery);
                });

                if(responsematchrequest=="accept"){ // rate to match with some one
                    var query2 = "INSERT IGNORE INTO rate (sender, receiver, send_name, receiver_name, kind) VALUES ('"+sendfacebookid+
                        "', '"+facebookid+"', '"+sender_name+"', '"+name1+"','accept')";
                    global.mysql.query(query2, function(err, result2){
                    });

                    var query1 = "SELECT * FROM rate WHERE sender='"+facebookid+"' AND receiver='"+sendfacebookid+"'";
                    global.mysql.query(query1, function(err, result){
                        if(err){
                            var data1 = {};
                            data1.retcode = 300;
                            data1.error_msg = "Could not find such photo.";
                            res.send(200, data1);  // end point
                        }
                        if(result.length > 0){ // if there is someone rate your photo // insert new match notifciation
                            var newmatch1  = "INSERT INTO notification (sender, destination, sender_name, notekind, state) VALUES ('"+
                                +sendfacebookid+"', '"+facebookid+"', '"+sender_name+"','newmatch','0')";
                            var newmatch2  = "INSERT INTO notification (sender, destination, sender_name, notekind, state) VALUES ('"+
                                +facebookid+"', '"+sendfacebookid+"', '"+name1+"','newmatch','0')";
                            var newmatch3 = "INSERT INTO matching (facebookid1, facebookid2, name1, name2) VALUES ('"
                                +sendfacebookid+"', '"+ facebookid + "', '" + sender_name + "','" + name1 + "')"
                            global.mysql.query(newmatch3, function(err, result3){
                                if(err){
                                    var data={};
                                    data.retcode = 300;
                                    data.error_msg = "sql_error";
                                    res.send(200, data);
                                }
                                var data={};
                                data.retcode = 200;
                                res.send(200, data);
                            });
                            global.mysql.query(newmatch1,function(err,result){          });
                            global.mysql.query(newmatch2,function(err,rewult2){         });
                        }
                        var data={};
                        data.retcode = 200;
                        res.send(200, data);
                    }
                );
                }else{ // rate not to match to that photo.
                    var query2 = "INSERT INTO rate (sender, receiver, send_name, receiver_name, kind) VALUES ('"+sendfacebookid+
                        "', '"+facebookid+"', '"+sender_name+"', '"+name1+"','refuse')";
                    global.mysql.query(query2, function(err, result2){
                        var data={};
                        data.retcode = 200;
                        res.send(200, data);
                    });
                }
            }else{ // case there is report for the photo. do not rate and increase the photo report.

                var reportquery = "UPDATE photo SET "+ reportkind + "= '"+ checkedreport
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(reportquery, function(err, reresult){
                });

                var data1 = {};
                data1.retcode = 200;
                data1.error_msg = "";
                res.send(200, data1);  // end point
            }
        }else{
            // if the photo does not exist error.
            // this may be not occur but
            console.log(notiquery);
            var data1 = {};
            data1.retcode = 300;
            data1.error_msg = "Could not find such photo.";
            res.send(200, data1);  // end point

        }

        });
}