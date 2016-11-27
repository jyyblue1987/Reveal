/**
 * Created by JonIC on 2016-11-11.
 */
// return the information about single photo.
exports.photoinfo  = function(req, res){
    console.log(req);

    //var sendfacebookid = req.body.myfacebookid;
    var photopath      = req.body.photopath;
    var facebookid     =req.body.facebookid;
    var query = "SELECT * FROM photo WHERE facebookid='"+ facebookid +"' AND photopath='"+photopath+"'";
    global.mysql.query(query, function(err, result){
        if(err){
            var data = {}
            data.retcode = 300;
            return res.send(200, data);
        }
        if(result.length > 0){
            data = {};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            return res.send(200,data);
        }
    });
}
