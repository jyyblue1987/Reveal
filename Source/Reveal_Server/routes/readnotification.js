/**
 * Created by JonIC on 2016-11-21.
 */
url = require('url');
exports.readnotification = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var sender = req.body.sender;
    var destination =  req.body.destination;
    var notekind =  req.body.notekind;
    var feedval =  req.body.feedval;
    var sendtime = req.body.sendtime;
    var id = req.body.Id;
    var state = 1;

    //update notification set state='1' where sender='333379007041276' and destination='1' and  notekind='requestfriend' and  state='0'
    var query =" UPDATE notification SET state = '1' WHERE Id='"+id+"'";
    global.mysql.query(query, function (err, result){
        if(err){
            var data={};
            data.retcode = 222;
            data.error_msg = "fail";
            //res.json(data);
            return res.send(200,data);

        }
        var data = {};
        data.retcode = 200;
        data.error_msg = "success";
        //res.json(data);
        return res.send(200,data);

    });
}
