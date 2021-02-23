if(process.env.NODE_ENV!=='production'){
    require('dotenv').config();
}

const express = require('express');
const app = express();
const expressLayouts = require('express-ejs-layouts');
const bodyParser = require('body-parser');
const methodOverride = require('method-override');

//import index router
const indexRouter = require('./routes/index');
const courseRouter = require('./routes/courses');
const commentRouter = require('./routes/comments');

//set the view engine using ejs
app.set('view engine', 'ejs');
//set where the view coming from
app.set('views', __dirname + '/views');
//set where the layout file is
app.set('layout','layouts/layout');
//to use express layouts
app.use(expressLayouts);
//indicate where the public files are
app.use(express.static('public'));
app.use(bodyParser.urlencoded({limit:'10mb', extended: false}));
//override the method for post to others such as delete
app.use(methodOverride('_method'));

//import mongoose
const mongoose = require('mongoose');
mongoose.connect(process.env.DATABASE_URL,{useNewUrlParser: true, useCreateIndex: true,useUnifiedTopology: true});
const db = mongoose.connection;
db.on('error', error=>console.log(error));
db.once('open',()=>console.log('Connected to Mongoose'));

app.use('/', indexRouter);
app.use('/courses', courseRouter);
app.use('/comments', commentRouter);

app.listen(process.env.PORT || 3000);