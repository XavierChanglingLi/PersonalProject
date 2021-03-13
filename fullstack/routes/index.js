//set up all the routes for the application
const express = require('express');
const router = express.Router();
const Comment = require('../models/comment');
const Course = require('../models/course');

router.get('/',async (req, res)=>{
    let comments;
    try{
        comments = await Comment.find().sort({createdAt: 'desc'}).limit(10).exec();
    }catch{
        comments = []
    }
    res.render('index', {comments: comments});
})

module.exports = router;