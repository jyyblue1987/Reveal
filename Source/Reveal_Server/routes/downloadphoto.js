/**
 * Created by JonIC on 2016-11-10.
 */
// /files/* is accessed via req.params[0]
// but here we name it :file
url = require('url');
exports.downloadphoto = function(req, res){
    console.log(req);
    //var url_parts = url.parse(req.url, true);
    //var file = url_parts.query.file;
    var file = req.body.photopath;
    var path = __dirname + 'public/images/' + photopath;

    res.download(path);
};

// /files/* is accessed via req.params[0]
// but here we name it :file
//app.get('/:file(*)', function(req, res, next){
//    var file = req.params.file
//        , path = __dirname + '/files/' + file;
//
//    res.download(path);
//});
