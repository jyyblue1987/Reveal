/**
 * Created by JonIC on 2016-11-11.
 */
// return the information about single photo.
exports.photoinfo  = function(req, res){
    console.log(req);

    //var sendfacebookid = req.body.myfacebookid;
    var photopath      = req.body.photopath;
    var facebookid     =req.body.facebookid;
    var query = "SELECT * FROM photo WHERE photopath='"+ photopath +"' AND facebookid='"+facebookid+"'";
    global.mysql.query(query, function(err, result){
        if(err){

        }
        if(result.length>0){
            var data = {};
            data.retcode = 200;
            data.error_msg = "";
            data.content = result;
            //res.json(data);
            return res.send(200,data);

        }
    });

}
