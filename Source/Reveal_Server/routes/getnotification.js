/**
 * Created by JonIC on 2016-11-26.
 */
url = require('url');
exports.getnotification = function(req, res) {
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    var facebookid = req.body.facebookid;
    var notequery = "SELECT * FROM notification WHERE destination='" + facebookid + "' AND state='0'";
    global.mysql.query(notequery, function (err, result) {
        var data = {};

        if (err) {
            data.retcode = 300;
            data.error_msg = "sql error";
            return res.send(200, data);

        }
        data.retcode = 200;
        data.error_msg = "";
        data.content = result;
        return res.send(200, data);
    });
}