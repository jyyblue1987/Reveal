/**
 * Created by JonIC on 2016-11-09.
 */

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

    // if sender like the photo then upgrade like with the sender's facebookid
    if(like == "like"){
        // first find the photo that liked AND  update the values.
        var photoquery = "SELECT * FROM photo WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
        global.mysql.query(photoquery, function(err, photoresult){
            if(err){

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
                    likefacebookid = sendfacebookid;
                }else {
                    likefacebookid = likefacebookid + "^" + sendfacebookid;
                }
                // update the photo column.(table)
                var likequery = "UPDATE photo SET likenum='"+ likenum +"', likefacebookid='"+ likefacebookid
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(likequery, function(err, uppresult){
                    if(err){

                    }
                    var data = {};
                    data.retcode = 200;
                    data.error_msg = "";
                    //res.json(data);
                    return res.send(200,data);
                });

            }
        });
    }else{

        // first find the photo that liked AND  update the values.
        var photoquery = "SELECT * FROM photo WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
        global.mysql.query(photoquery, function(err, photoresult){
            if(err){

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
                    commentcon = comment;
                }else {
                    commentcon = commentcon + "^" + comment;
                }
                // update the photo column.(table)
                var commentquery = "UPDATE photo SET commentnum='"+ commentnum +"', commentcon='"+ commentcon
                    +"' WHERE facebookid='"+facebookid +"' AND photopath='"+ photopath +"'";
                global.mysql.query(commentquery, function(err, uppresult){
                    if(err){

                    }
                    console.log(commentquery);
                    var data = {};
                    data.retcode = 200;
                    data.error_msg = "";
                    //res.json(data);
                    return res.send(200,data);
                });

            }
        });
    }
}
