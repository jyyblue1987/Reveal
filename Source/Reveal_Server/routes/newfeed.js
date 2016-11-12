/**
 * Created by JonIC on 2016-11-09.
 */
url = require('url');
exports.newfeed = function(req, res){
    console.log(req);
    var url_parts = url.parse(req.url,true);
    var facebookid = url_parts.query.facebookid;
    var photopath = url_parts.query.photopath;
    var group = url_parts.query.group;
    var aboutphoto = url_parts.query.aboutphoto;
    var rate = url_parts.query.rate;

    // first update the photo database
    var upphotoquery = "UPDATE photo SET mycomment='"+ aboutphoto
        +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
    global.mysql.query(upphotoquery, function(err, uppresult){
        if(err){

        }
        console.log(upphotoquery);
    });
    // find the users who take part in the specified group
    if(group == "facebook"){
        var query_friend_1 = "SELECT facebookid2 FROM friend WHERE facebookid1='"+facebookid+"'";
        var query_friend_2 = "SELECT facebookid1 FROM friend WHERE facebookid2='"+facebookid+"'";
        var friend1="";
        var friend2="";
        var queryComplete = 0;
        global.mysql.query(query_friend_1, function(err, rows2){
            if(err){
                console.error(err);
                queryComplete = queryComplete + 1;
                if(queryComplete == 2){
                    // here finish adding the newfeed notification message and return;
                    res.send(200,"success");  // end point
                }
            }

            if(rows2.length>0){
                for(var i=0; i<rows2.length; i++){
                    friend1 = rows2[i].facebookid2;
                    var sendtime = 11;
                    var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval) VALUES ('" +
                        facebookid + "', '"+ friend1 +"', 'newfeed', '" + sendtime +"', '" + photopath +"')";
                    global.mysql.query(newfeedquery, function(err, newresult){
                        if(err){

                        }
                        console.log(newfeedquery);
                    });
                }
            }
            queryComplete = queryComplete + 1;
            if(queryComplete == 2){
                // here finish adding the newfeed notification message and return;
                res.send(200,"success");  // end point
            }

        });
        global.mysql.query(query_friend_2, function(err, rows3){
            if(err){
                console.error(err);
                queryComplete = queryComplete+1;
                if(queryComplete == 2){
                    // here finish adding the newfeed notification message and return;
                    res.send(200,"success");  // end point
                }
            }

            if(rows3.length>0){
                for(var x=0; x<rows3.length; x++){
                    // here add the newfeed notification message.
                    var sendtime = 11;
                    friend2 = rows3[x].facebookid1;
                    var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval) VALUES ('" +
                        facebookid + "', '"+ friend2 +"', 'newfeed', '" + sendtime +"', '" + photopath +"')";
                    global.mysql.query(newfeedquery, function(err, newresult){
                        if(err){

                        }
                        console.log(newfeedquery);
                    });
                }
            }
            queryComplete = queryComplete+1;

            if(queryComplete == 2){
                // here finish adding the newfeed notification message and return;
                res.send(200,"success");  // end point
            }

        });

    }else if(group != ""){
        // here the photo send only one friends and the notification added.
        var query_friend_1 = "SELECT facebookid2 FROM friend WHERE facebookid1='"+group+"'";
        var query_friend_2 = "SELECT facebookid1 FROM friend WHERE facebookid2='"+group+"'";
        global.mysql.query(query_friend_1, function(err, result1){
            if(err){

            }
            if(result.length > 0){
                var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" +
                    facebookid + "', '"+ group +"', 'newfeed', '" + sendtime +"', '" + photopath +"')";
                global.mysql.query(newfeedquery, function(err, newresult){
                   if(err){

                   }
                    console.log(newfeedquery);
                    res.send(200, "success");

                });
            }
        });
        global.mysql.query(query_friend_2, function(err, result1){
            if(err){

            }
            if(result.length > 0) {
                var newfeedquery = "INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('" +
                    facebookid + "', '" + group + "', 'newfeed', '" + sendtime + "', '" + photopath + "')";
                global.mysql.query(newfeedquery, function (err, newresult) {
                    if (err) {

                    }
                    console.log(newfeedquery);
                    res.send(200, "success");
                });
            }
        });
    }
}