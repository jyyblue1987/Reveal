/**
 * Created by JonIC on 2016-12-16.
 */
exports.update_setting  = function(req, res) {
    console.log(req);

    var facebookid = req.body.facebookid;
    var profilephoto = req.body.profilephoto;
    var name = req.body.name;
    var email = req.body.email;
    var gender = req.body.gender;

    var age  = req.body.age;
    var locationx = req.body.locationx;
    var locationy = req.body.locationy;
    var showme = req.body.showme;
    var showmatch = req.body.showmatch;

    var maxage = req.body.maxage;
    var minage = req.body.minage;
    var distance = req.body.distance;
    var maxrate = req.body.maxrate;
    var minrate  =req.body.minrate;

    var not_match = req.body.not_match;
    var not_message = req.body.not_message;
    var not_friend  = req.body.not_friend;
    var not_comment = req.body.not_comment;
    var not_like = req.body.not_like;

    var showfullname = req.body.showfullname;
    var searchbyname = req.body.searchbyname;
    var autoaccept = req.body.autoaccept;

    var query = "UPDATE" +
        "    users" +
        "    SET" +
        "        name = '"+name+"'," +
        "        email = '"+email+"'," +
        "        gender = '"+gender+"'," +
        "        age = '"+age+"' ," +
        "        locationx='"+locationx+"'," +
        "        locationy = '"+locationy+"'," +
        "        showme='"+showme+"'," +
        "        showmatch='"+showmatch+"'," +
        "        maxage='"+maxage+"'," +
        "        minage='"+minage+"'," +
        "        distance='"+distance+"'," +
        "        maxrate='"+maxrate+"'," +
        "        minrate='"+minrate+"'," +
        "        not_match='"+not_match+"'," +
        "        not_message='"+not_message+"'," +
        "        not_friend='"+not_friend+"'," +
        "        not_comment='"+not_comment+"'," +
        "        not_like='"+not_like+"'," +
        "        showfullname='"+showfullname+"'," +
        "        searchbyname='"+searchbyname+"'," +
        "        autoaccept='"+autoaccept+"' " +
        "    WHERE facebookid = '"+facebookid+"' ";
    global.mysql.query(query,function(err, result){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg="sql error";
            return res.send(200, data);
        }
        data = {};
        data.retcode = 200;
        data.content="success";
        return res.send(200, data);

    });
}