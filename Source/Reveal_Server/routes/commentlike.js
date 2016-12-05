/**
 * Created by JonIC on 2016-11-09.
 */
// like construction:          facebookid & username   ^    facebookid & username
url = require('url');
exports.commentlike = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;
    //var sendfacebookid = url_parts.query.sendfacebookid;
    //var photopath = url_parts.query.photopath;
    //var like = url_parts.query.like;
    //var comment = url_parts.query.comment;

    var facebookid      =req.body.facebookid;
    var sendfacebookid  = req.body.sendfacebookid;
    var photopath       = req.body.photopath;
    var like            = req.body.like;
    var comment         = req.body.comment;
    var sendname        = req.body.sendname;
    var profilephoto    = req.body.profilephoto;

    // if sender like the photo then upgrade like with the sender's facebookid
    // in photo table.
    if(like == "like"){
        // first find the photo that liked AND  update the values.
        var photoquery = "SELECT * FROM photo WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
        global.mysql.query(photoquery, function(err, photoresult){
            if(err){
                var data = {};
                data.retcode = 300;
                data.error_msg = "server error.";
                return res.send(200,data);

            }
            if(photoresult.length > 0){
                // get the values of that photo
                var likenum = parseInt(photoresult[0].likenum);
                if(likenum == 0 || like == NaN){
                    likenum = 1;
                }else{
                    likenum = likenum + 1;
                }
                var likefacebookid = photoresult[0].likefacebookid;
                if(likefacebookid == null || likefacebookid == ""){
                    likefacebookid = sendfacebookid + "&" + sendname;
                }else {

                    // if the sendfacebookid has already like this photo then alarm show.
                    if(likefacebookid.indexOf(sendfacebookid) > -1) {
                        // return error.
                        var data = {};
                        data.retcode = 333;
                        data.error_msg = "You already like this photo.";
                        //res.json(data);
                        return res.send(200,data);


                    }else{ // if sendfacebookid does not like this photo before then append this like message.
                        likefacebookid = likefacebookid + "^" + sendfacebookid +"&" + sendname + "&" + profilephoto;
                    }
                }
                // update the photo column.(table)
                var likequery = "UPDATE photo SET likenum='"+ likenum +"', likefacebookid='"+ likefacebookid
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(likequery, function(err, uppresult){
                    if(err){

                    }
                    //var data = {};
                    //data.retcode = 200;
                    //data.error_msg = "";
                    ////res.json(data);
                    //return res.send(200,data);
                });
                var sendtime = new Date().toString();
                var aaa = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('"
                    + sendfacebookid + "', '" + facebookid + "', 'like', '"+sendtime+"' , '" + photopath + "', '" + sendname + "')";
                global.mysql.query(aaa, function(err, result){

                    if(err){
                        console.log(aaa);
                        var data = {};
                        data.retcode = 300;
                        data.error_msg = "server error";
                        return res.send(200,data);

                    }
                    console.log(aaa);
                    var data = {};
                    data.retcode = 200;
                    data.error_msg = "";
                    //res.json(data);
                    return res.send(200,data);
                })
            }else{
                var data = {};
                data.retcode = 200;
                data.error_msg = "there is no photo";
                return res.send(200,data);

            }
        });
    }else{
        // for the comment data rewrite in photo table comment content and comment size.
        // first find the photo that liked AND  update the values.
        var photoquery = "SELECT * FROM photo WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
        global.mysql.query(photoquery, function(err, photoresult){
            if(err){
                console.log(commentquery);
                var data = {};
                data.retcode = 200;
                data.error_msg = "";
                //res.json(data);
                return res.send(200,data);

            }
            if(photoresult.length > 0){
                // get the values of that photo
                var commentnum = parseInt(photoresult[0].commentnum);
                if(commentnum == 0 || commentnum == NaN){
                    commentnum = 1;
                }else{
                    commentnum = commentnum + 1;
                }
                var commentcon = photoresult[0].commentcon;
                if(commentcon == null || commentcon == ""){
                    commentcon =sendname + "&" +  comment + "&" + sendfacebookid + "&" + profilephoto;
                }else {
                    commentcon = commentcon + "^" + sendname + "&" +  comment + "&" + sendfacebookid+ "&" + profilephoto;
                }
                // insert comment notification in notification table.
//              INSERT INTO notification (sender, destination, notekind, sendtime) VALUES ('a', 'g', 'matchRequest', '0')

                // update the photo column.(table)
                var commentquery = "UPDATE photo SET commentnum='"+ commentnum +"', commentcon='"+ commentcon
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(commentquery, function(err, uppresult){
                    if(err){
                        console.log(commentquery);
                        var data = {};
                        data.retcode = 200;
                        data.error_msg = "";
                        //res.json(data);
                        return res.send(200,data);

                    }

                    if(facebookid != sendfacebookid){
                        var sendtime = new Date().toString();
                        var notequery = "INSERT INTO notification (sender, destination, notekind, sendtime, feedval, sender_name) VALUES ('"
                            + sendfacebookid + "', '" + facebookid + "', 'comment', '" + sendtime + "', '" + photopath + "', '" + sendname + "')";
                        global.mysql.query(notequery, function(err, result){
                            if(err){
                                console.log(commentquery);
                                var data = {};
                                data.retcode = 200;
                                data.error_msg = "";
                                //res.json(data);
                                return res.send(200,data);

                            }

                            console.log(commentquery);
                            var data = {};
                            data.retcode = 200;
                            data.error_msg = "";
                            //res.json(data);
                            return res.send(200,data);


                        }); // notificaton insert mysql.query

                    }else{
                        console.log(commentquery);
                        var data = {};
                        data.retcode = 200;
                        data.error_msg = "";
                        //res.json(data);
                        return res.send(200,data);
                    }

                }); // update photo table query

            } // if find photo that has such photopath
        });  // find photo query
    } // if this message is comment
} // function
