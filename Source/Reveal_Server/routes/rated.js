/**
 * Created by JonIC on 2016-11-09.
 */

url = require('url');
exports.rated = function(req, res){
    console.log(req);
    var url_parts = url.parse(req.url,true);
    //var
    var sendfacebookid = req.body.myfacebookid;
    var photopath      = req.body.photopath;
    var facebookid     =req.body.facebookid;
    var rating         = parseInt(req.body.rating);
    var report         = req.body.report;

    //global.io.sockets.in(facebookid).emit('324234', data);

    var responsematchrequest = req.body.responsematchrequest;

    // find photo with facebookid and photopath.
    var query = "SELECT * FROM photo WHERE facebookid='" + facebookid + "' AND photopath='" + photopath + "'";
    global.mysql.query(query, function(err, rows){
        if(err){
            console.error(err);
            throw err;
        }
        // there is only one such photo.
        if(rows.length > 0){
            var ratesum    = parseInt(rows[0].ratesum);
            var rateNumber = parseInt(rows[0].ratenumber);

            var checkedreport;
            var reportkind ="";
            if(report == "Group Photo"){
                checkedreport = parseInt(rows[0].reportgp)+1;
                reportkind = "reportgp";
            }else if(report == "Offensive Material"){
                checkedreport = parseInt( rows[0].reportom)+1;
                reportkind = "reportom";
            }else if(report == "Not Actual Person"){
                checkedreport = parseInt(rows[0].reportnap)+1;
                reportkind = "reportnap";
            }else if(report == "Wrong Gender"){
                checkedreport = parseInt(rows[0].reportwg) +1;
                reportkind = "reportwg";
            }else if(report == "Feels Like Spam"){
                checkedreport = parseInt(rows[0].reportfls) + 1;
                reportkind = "reportfls";
            }
            // if this request is the response for the match event then the notification delete.
            if(responsematchrequest != ""){

                var delquery = "DELETE  FROM notification WHERE sender='"+facebookid+"' AND destination='"+sendfacebookid+"' AND notekind='matchRequest'";
                global.mysql.query(delquery, function(err, delresult){
                    if(err){

                    }
                    console.log(delquery);
                });
            }
            // if there is not report and there is only rate.
            if(reportkind == ""){
                // update the photos' totalrate and ratenumber.
                ratesum    = ratesum + rating;
                rateNumber = rateNumber + 1;
                var updatequery = "UPDATE photo SET ratesum='"+ ratesum +"', "+ "ratenumber='" + rateNumber
                                            +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(updatequery, function(err, udresult){
                    console.log(updatequery);
                });

                // send message to the photo's owner that someone want to match with him/her and save this matchRequest notification to
                // notification table.
                // send part does not implement.
                // check response match request
                if(responsematchrequest == ""){
                    // this is match request
                    var sendtime = 10;
                    var notiquery = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" +
                        sendfacebookid + "', '"+ facebookid +"', 'matchRequest', '" + sendtime +"')";
                    global.mysql.query(notiquery, function(err, noresult){
                        if(err){

                        }
                        console.log(notiquery);
                        res.send(200,"success");  // end point
                    });
                }else if(responsematchrequest == "accept"){
                    // delete notification  , add match table already delete above.
                    ////INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('a', 'g', 'matchRequest', '0')
                    var addmatchquery = "INSERT INTO matching (facebookid1, facebookid2) VALUES ('"+ sendfacebookid+ "', '"+ facebookid + "')";
                    global.mysql.query(addmatchquery, function(err, addresult){
                        console.log(addmatchquery);
                        res.send(200,"success");  // end point
                    });
                }else //if(responsematchrequest == "refuse")
                {
                    // delete notification ,already delete above
                    console.log("success");
                    res.send(200,"success");  // end point
                }
            }else {
                // delete notification. if this is responce of request. already deleted.
                // increase the reported filed by 1.
                var reportquery = "UPDATE photo SET "+ reportkind + "= '"+ checkedreport
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(reportquery, function(err, reresult){
                    console.log(reportquery);
                    res.send(200,"success");  // end point

                });
                if(checkedreport == 20){
                    // send photo report notification to photo's owner. photopath, reportkind, facebookid and please delete that photo.
                    console.log("your photo is dicarded.");
                    res.send(200,"success");  // end point

                }
            }
        }
    });
}