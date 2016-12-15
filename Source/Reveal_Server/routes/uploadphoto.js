var fs = require('fs');

// create an incoming form object
exports.uploadphoto = function(req, res){
    console.log(req.body); //form fields

    console.log(req.file); //form files
    var facebookid = req.body.facebookid;
    var name = req.body.name;
    var photopath  = req.file.filename + ".jpg";
    var initvalue = 0;
    var initString = "";
    //INSERT INTO notification (sender, destination, notekind, sendtime)
    // VALUES ('a', 'g', 'matchRequest', '0')
    fs.renameSync(req.file.path, req.file.path + ".jpg");

    var queryprofile = "UPDATE users SET profilephoto='"+photopath+"' WHERE facebookid='"+facebookid+"'";
    global.mysql.query(queryprofile, function(err, resultprofile){});

    var query = "INSERT INTO photo (facebookid, photopath, ratesum, ratenumber, reportgp,"+
        "reportom, reportnap, reportwg, reportfls, commentnum,"+
        " commentcon, likenum, likefacebookid,"+
        "mycomment, name) VALUES ('"+
        facebookid+"','" + photopath +"','" + initvalue +"','"+initvalue+"','"+ initvalue+"','"+
        initvalue+"','"+initvalue+"','"+initvalue +"','"+initvalue+"','"+initvalue+"','"+
        initString+"','"+initvalue+"','"+initString+"','"+
        initString+"','" + name + "')"
    global.mysql.query(query, function(err, result){
        if(err){
            var data ={};
            data.retcode = 300;
            data.error_msg ="sql server error";
            return res.send(200,data);
        }
        var data ={};
        data.retcode = 200;
        data.error_msg ="";
        return res.send(200,data);
    });
};
