const express = require('express');
const router = express.Router();
const Comment = require('../models/comment');
const Course = require('../models/course');

//all comments route
router.get('/', async (req, res)=>{
    let query = Comment.find();
    if(req.query.courseNumber != null && req.query.courseNumber !==''){
        query = query.regex('courseNumber', new RegExp(req.query.courseNumber,'i'))
    }
    if(req.query.professor != null && req.query.professor !==''){
        query = query.regex('professor', new RegExp(req.query.professor,'i'))
    }
    if(req.query.publishedAfter != null && req.query.publishedAfter !==''){
        query = query.gte('publishDate', req.query.publishedAfter)
    }
    try{
        const  courses = await Course.find({});
        const comments = await query.exec();
        res.render('comments/index',{
            courses: courses,
            comments: comments,
            searchOptions: req.query
        })
    }catch{
        res.redirect('/')
    }
})
//new comment route
router.get('/new', (req, res)=>{
    renderNewComment(res, new Comment());
})
//create comment route
router.post('/', async(req,res)=>{
    const comment = new Comment({
        courseNumber: req.body.courseNumber,
        professor: req.body.professor,
        description: req.body.description
    })
    try{
        const newComment = await comment.save();
        res.redirect(`comments/${newComment.id}`)
    }catch{
        renderNewComment(res, comment, true);
    }
})

//show comment route
router.get('/:id', async(req, res)=>{
    try{
        const comment = await Comment.findById(req.params.id).populate('course').exec();
        res.render('comments/show',{comment:comment});
    }catch{
        res.redirect('/');
    }
})

//edit comment route
router.get('/:id/edit', async (req,res)=>{
    try{
        const comment = await Comment.findById(req.params.id);
        renderEditComment(res, comment);
    }catch{
        res.redirect('/')
    }
})

//update comment route
router.put('/:id', async (req,res)=>{
    let comment;
    try{
        comment = await Comment.findById(req.params.id);
        comment.courseNumber = req.body.courseNumber;
        comment.professor = req.body.professor;
        comment.description = req.body.description;
        await comment.save();
        res.redirect(`/comments/${comment.id}`)
    }catch{
        if(comment!=null){
            renderEditComment(res, comment, true)
        }else{
            res.redirect('/');
        }
    }
})

//delete a comment
router.delete('/:id', async (req,res)=>{
    let comment;
    try{
        comment = await Comment.findById(req.params.id);
        await comment.remove();
        res.redirect('/comments');
    }catch{
        if(comment!=null){
            res.render('comments/show',{
                comment:comment,
                errorMessage: 'Coud not remove comment'
            })
        }else{
            res.redirect('/')
        }
    }
})

async function renderNewComment(res, comment, hasError=false){
    renderFormComment(res, comment, 'new', hasError);
}

async function renderEditComment(res, comment, hasError=false){
    renderFormComment(res, comment, 'edit', hasError);
}

async function renderFormComment(res, comment, form, hasError=false){
    try{
        const courses = await Course.find({});
        const params = {
            courses: courses,
            comment: comment
        }
        if(hasError){
            if(form ==='edit'){
                params.errorMessage = 'Error Updating Comment'
            }else{
                params.errorMessage = 'Error Creating Comment'
            }
        }
        res.render(`comments/${form}`, params);
    }catch{
        res.redirect('/comments');
    }
}

module.exports = router;