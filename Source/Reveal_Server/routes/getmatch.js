/**
 * Created by JonIC on 2016-11-11.
 */
 // get one's all matches from match table.
url = require('url');
exports.getmatch = function(req, res){
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var facebookid = url_parts.query.facebookid;

    var facebookid = req.body.facebookid;

    var query_1 = "SELECT facebookid2 FROM matching WHERE facebookid1='"+facebookid+"'";
    var query_2 = "SELECT facebookid1 FROM matching WHERE facebookid2='"+facebookid+"'";
    global.mysql.query(query_1, function(err, result1){
        if(err){
            var data = {};
            data.retcode = 300;
            data.error_msg = "sql server error";
            //res.json(data);
            return res.send(200,data);
// return sql server error
        }
        global.mysql.query(query_2, function(err, result2){
            if(err){
                var data = {};
                data.retcode = 300;
                data.error_msg = "sql server error";
                //res.json(data);
                return res.send(200,data);
// sql server error
            }
            var result=null;
            if(result1.length>0){
                for(var i=0; i<result1.length-1; i++){
                    result = result + "^" + result1[i].facebookid2
                }
                result = result + "^" + result1[result1.length-1].facebookid2
            }
            if(result2.length>0){
                for(var i=0; i<result2.length-1; i++){
                    result = result + "^" + result2[i].facebookid1
                }
                result = result + "^" + result2[result2.length-1].facebookid1
            }
            if(result == null){
                var data = {};
                data.retcode = 201;
                data.error_msg = "No Match";
                //res.json(data);
                return res.send(200,data);
            }else {
                var data = {};
                data.retcode = 200;
                data.error_msg = "";
                data.content = result;
                //res.json(data);
                return res.send(200,data);
            }
        });
    });
    //var facebookid=req.body.facebookid;
}

