var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');
var restapi = require('./routes/restapi');
///////////////////// my code  ////////////////////////
//////////////////////////////////////////////////////
var login   = require('./routes/login');
var rated   = require('./routes/rated');
var newfeed = require('./routes/newfeed');
var commentlike = require('./routes/commentlike');
var profile = require('./routes/profile');
var friendprofile = require('./routes/friendprofile');
var matchprofile = require('./routes/matchprofile');
var addfriend = require('./routes/addfriend');
var chat = require('./routes/chat');
var downloadphoto = require('./routes/downloadphoto');
var uploadphoto = require('./routes/uploadphoto');
var photoinfo = require('./routes/photoinfo');
var getfriend = require('./routes/getfriend');
var getmatch  = require('./routes/getmatch');
var personinfo = require('./routes/personinfo');
var myprofile= require('./routes/myprofile');
var deletefriend = require('./routes/deletefriend');
var readnotification = require('./routes/readnotification');
var getnotificationsize = require('./routes/getnotificationsize');
var findbyname = require('./routes/findbyname');
var blockmatch = require('./routes/blockmatch');
var checkblock = require('./routes/checkblock');
var getnotification = require('./routes/getnotification');
var gettotalrate = require('./routes/gettotalrate');
var deletephoto = require('./routes/deletephoto');
var isphoto = require('./routes/isphoto');
var dislike = require('./routes/dislike');
var fullnamefriend = require('./routes/fullnamefriend');
var post_notification = require('./routes/post_notification');
var update_setting = require('./routes/update_setting');
var update_suberb = require('./routes/update_suberb');




//var rate   = require('./routes/rate');
///////////////////////////////////////////////////////
///////////////////////////////////////////////////////
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use(function(req, res, next) {
  req.headers['if-none-match'] = 'no-match-for-this';
  next();
});

app.use('/', index);
app.use('/users', users);
app.use('/api', restapi);

//////////////////////// my code /////////////////////////////
/////////////////////////////////////////////////////
app.post('/routes/login',login.login)
app.post('/routes/rated', rated.rated);
app.post('/routes/photoinfo', photoinfo.photoinfo);
app.post('/routes/newfeed', newfeed.newfeed);
app.post('/routes/commentlike', commentlike.commentlike);
app.post('/routes/profile', profile.profile);
//app.post('/routes/myprofile', myprofile.myprofile);
app.post('/routes/friendprofile', friendprofile.friendprofile);
app.post('/routes/matchprofile', matchprofile.matchprofile);
app.post('/routes/addfriend', addfriend.addfriend);
//app.post('/routes/chat', chat.chat);
app.post('/routes/getfriend', getfriend.getfriend);
app.post('/routes/getmatch',getmatch.getmatch);
app.post('/routes/personinfo', personinfo.personinfo);

app.get('/routes/myprofile', myprofile.myprofile);
app.post('/routes/myprofile', myprofile.myprofile_post);
app.post('/routes/deletefriend',deletefriend.deletefriend);
app.post('/routes/readnotification', readnotification.readnotification);
app.post('/routes/getnotificationsize', getnotificationsize.getnotificationsize);
app.post('/routes/findbyname', findbyname.findbyname);
app.post('/routes/blockmatch',blockmatch.blockmatch);
app.post('/routes/checkblock', checkblock.checkblock);
app.post('/routes/getnotification',getnotification.getnotification);
app.post('/routes/gettotalrate', gettotalrate.gettotalrate);
//app.post('/routes/rate', rate.rate);
app.post('/routes/downloadphoto',downloadphoto.downloadphoto);
app.post('/routes/deletephoto',deletephoto.deletephoto);
app.post('/routes/isphoto',isphoto.isphoto);
app.post('/routes/dislike',dislike.dislike);
app.post('/routes/fullnamefriend',fullnamefriend.fullnamefriend);
app.post('/routes/post_notification',post_notification.post_notification);
app.post('/routes/update_setting',update_setting.update_setting);
app.post('/routes/update_suberb',update_suberb.update_suberb);



//app.get('/routes/deletefriend',deletefriend.deletefriend);
//app.get('/personinfo', personinfo.personinfo);
//app.get('/getmatch',getmatch.getmatch);
//app.get('/getfriend',getfriend.getfriend);
//app.get('/login',login.login);
//app.get('/rated', rated.rated);
//app.get('/newfeed', newfeed.newfeed);
//app.get('/commentlike', commentlike.commentlike);
//app.get('/profile', profile.profile);
//app.get('/friendprofile', friendprofile.friendprofile);
//app.get('/matchprofile', matchprofile.matchprofile);
//app.get('/addfriend', addfriend.addfriend);
app.get('/chat', chat.chat);



//app.post('/routes/uploadphoto',uploadphoto.uploadphoto);

var multer = require('multer');
var fs = require('fs');
var path = require('path');
var appRoot = path.resolve(__dirname);
app.post('/routes/uploadphoto', multer({ dest: appRoot + '/public/images/'}).single('uploadphoto'), uploadphoto.uploadphoto);

// catch 404 and forward to error handler
/////////////////////////////////////////////////////
/////////////////////////////////////////////////////
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
