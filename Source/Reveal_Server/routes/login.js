/**
 * Created by JonIC on 2016-11-09.
 */
url = require('url');
exports.login = function(req, res){
    console.log(req);
    // if the man come this app newly then register that man.
    // if the man is customer then return non friend, non match, non herself person list.
    // then return non match and non friend person's photo list.

    var facebookid=req.body.facebookid;
    var email = req.body.email;
    var age = req.body.age;
    var name =req.body.name;
    var gender =req.body.gender;
    var locationx=req.body.locationx;
    var locationy=req.body.locationy;
    var othergender= req.body.othergender;

    var first;
    console.log(facebookid);
    console.log(name);

// get
//    var url_parts = url.parse(req.url,true);
//    var email = url_parts.query.email;
//    var facebookid=url_parts.query.facebookid;
//    var age = url_parts.query.age;
//    var name = url_parts.query.firstname;
//    var gender = url_parts.query.gender;
//    var locationx = url_parts.query.locationx;
//    var locationy = url_parts.query.locationy;
//    var othergender = url_parts.query.othergender;
    var sql = "SELECT facebookid FROM users WHERE facebookid='" + facebookid +"'";//email='"+ email+"' AND age='"+ age +"' AND firstname='"+name+"'";
    console.log(sql);
    global.mysql.query(sql, function(err,rows){
        if(err){
            console.error(err);
            throw err;
        }

        var first = 0;
        if(rows.length == 0){
            insert(facebookid, gender, locationx, locationy, email, age, name);
            first = 1;
            sendphoto(facebookid,res, first);
        }
        if(rows.length > 0){
            sendphoto(facebookid,res, first);
        }
    });
}
insert = function(facebookid, gender, locationx, locationy, email, age, name){
    var totalrate =0;
    //var locationx  = 0;
    //var locationy = 0;
    var profilephoto = "";
    var query = "INSERT INTO users (facebookid, gender, locationx, locationy, totalrate, name, age, email,profilephoto,lastname) VALUES ('"+facebookid+"', '"+ gender
        +"', '"+locationx+"', '"+locationy +"', '"+ totalrate+"', '"+name+"', '"+age+ "', '"+email+"', '"+ profilephoto +"', '')" ;
    global.mysql.query(query,function(err,rows){
        if(err){
            console.error(err);
            throw err;
        }
        if(rows.length==0){
            console.log("aaa") };
    });
}

sendphoto = function(facebookid,res, first){
    var query_match_1 = "SELECT facebookid2 FROM matching WHERE facebookid1='"+facebookid+"'";
    var query_match_2 = "SELECT facebookid1 FROM matching WHERE facebookid2='"+facebookid+"'";
    var query_friend_1 = "SELECT facebookid2 FROM friend WHERE facebookid1='"+facebookid+"'";
    var query_friend_2 = "SELECT facebookid1 FROM friend WHERE facebookid2='"+facebookid+"'";

    var match1="";
    var match2="";
    var friend1="";
    var friend2="";

    var queryComplete = 0;
    global.mysql.query(query_match_1, function(err, rows){
        if(err){
            console.error(err);
            queryComplete = queryComplete+1;
            if(queryComplete == 4){
                getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid, first);
            }
        }
        queryComplete = queryComplete+1;

        if(rows.length>0){
            for(var i=0; i<rows.length-1; i++){
                match1 = match1 + "'" + rows[i].facebookid2 + "' , "
            }
            match1 = match1 + "'" + rows[rows.length-1].facebookid2 + "' "
        }
        if(queryComplete == 4){
            getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid, first);
        }

    });
    global.mysql.query(query_match_2, function(err, rows1){
        if(err){
            console.error(err);
            queryComplete = queryComplete+1;
            if(queryComplete == 4){
                getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid, first);
            }
        }
        queryComplete = queryComplete+1;

        if(rows1.length>0){
            for(var i=0; i<rows1.length-1; i++){
                match2 = match2 + "'" + rows1[i].facebookid1 + "' , "
            }
            match2 = match2 + "'" + rows1[rows1.length-1].facebookid1 + "' "

        }
        if(queryComplete==4){
            getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid,first);
        }

    });
    global.mysql.query(query_friend_1, function(err, rows2){
        if(err){
            console.error(err);
            queryComplete = queryComplete+1;
            if(queryComplete == 4){
                getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid, first);
            }
        }
        queryComplete = queryComplete+1;

        if(rows2.length>0){
            for(var i=0; i<rows2.length-1; i++){
                friend1 = friend1 + "'" + rows2[i].facebookid2 + "' , "
            }
            friend1 = friend1 + "'" + rows2[rows2.length-1].facebookid2 + "' "
        }
        if(queryComplete == 4){
            getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid,first);
        }

    });
    global.mysql.query(query_friend_2, function(err, rows3){
        if(err){
            console.error(err);
            queryComplete = queryComplete+1;
            if(queryComplete == 4){
                getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid,first);
            }
        }
        queryComplete = queryComplete+1;

        if(rows3.length>0){
            for(var i=0; i<rows3.length-1; i++){
                friend2 = friend2 + "'" + rows3[i].facebookid1 + "' , "
            }
            friend2 = friend2 + "'" + rows3[rows3.length-1].facebookid1 + "' "
        }
        if(queryComplete == 4){
            getNonFriendMatch(match1,match2,friend1,friend2,res,facebookid,first);
        }

    });

}

getNonFriendMatch = function(match1, match2, friend1, friend2,res,facebookid, first) {
    var MatchAndFriend = match1;
    var MatchAndFriend2 = match2;
    var MatchAndFriend3 = friend1;
    var MatchAndFriend4 = friend2;
    var searchIn = "";

    var finalMatchF="";
    if(finalMatchF == "" && MatchAndFriend != ""){finalMatchF = MatchAndFriend;}
    if(finalMatchF == "" && MatchAndFriend2 != ""){
        finalMatchF = MatchAndFriend2;
    }else if(finalMatchF !="" && MatchAndFriend2 != ""){
        finalMatchF = finalMatchF + ", " + MatchAndFriend2;
    };
    if(finalMatchF == "" && MatchAndFriend3 != ""){
        finalMatchF = MatchAndFriend3;
    }else if(finalMatchF !="" && MatchAndFriend3 != ""){
        finalMatchF = finalMatchF + ", " + MatchAndFriend3;
    };
    if(finalMatchF == "" && MatchAndFriend4 != ""){
        finalMatchF = MatchAndFriend4;
    }else if(finalMatchF !="" && MatchAndFriend4 != ""){
        finalMatchF = finalMatchF + ", " + MatchAndFriend4;
    };

    if(finalMatchF == ""){
        finalMatchF = facebookid;
    }else{
        finalMatchF = finalMatchF + ", '"+ facebookid + "'";
    }
    var query;
    if(first == 0){
        query = "SELECT facebookid FROM users WHERE facebookid NOT IN(" + finalMatchF +")";
    }else{
        query = "SELECT facebookid FROM users";
    }
    global.mysql.query(query, function(err, rows){
        if(err){
            console.error(err);
            throw err;
        }

        if(rows.length > 0){
            for(var i=0; i < rows.length-1; i++){
                searchIn  = searchIn + "'" + rows[i].facebookid + "', ";
            }
            searchIn = searchIn + "'" + rows[rows.length-1].facebookid + "'"

            var query = "SELECT facebookid, photopath FROM photo WHERE facebookid IN ("+ searchIn +")"
            global.mysql.query(query, function(err, result){
                if(err){

                }
                //res.status(200).send(result);
                //res.status(200);
                var data = {};
                data.retcode = 200;
                data.error_msg = "";
                data.content = result;
                //res.json(data);
                return res.send(200,data);
                //return res.status(200).json(result);
                //res.send(200, "success") // last sending part........................
            });
        }

    });

}

getPhoto = function(search ,res){

    var query = "SELECT facebookid, photopath FROM photo WHERE facebookid IN ("+ search +")";
    global.mysql.query(query, function(err, result){
        if(err){

        }
        res.json(result);
        res.send(200, "success")
    });
}
