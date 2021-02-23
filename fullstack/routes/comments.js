const express = require('express');
const router = express.Router();
const Comment = require('../models/comment');
const Course = require('../models/course');

//all comments route
router.get('/', async (req, res)=>{
    let query = Comment.find()
    if(req.query.courseNumber != null && req.query.courseNumber !==''){
        query = query.regex('courseNumber', new RegExp(req.query.courseNumber,'i'))
    }
    if(req.query.professor != null && req.query.professor !==''){
        query = query.regex('professor', new RegExp(req.query.professor),'i')
    }
    if(req.query.publishedAfter != null && req.query.publishedAfter !==''){
        query = query.gte('publishDate', req.query.publishedAfter)
    }
    try{
        const comments = await query.exec();
        res.render('comments/index',{
            comments: comments,
            searchOptions: req.query
        })
    }catch{
        res.redirect('/')
    }
})
//new comment route
router.get('/new', (req, res)=>{
    res.render('comments/new', {comment: new Comment()})
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
        // res.redirect('comments/${newComment.id}')
        res.redirect('comments');
    }catch{
        res.render('comments/new',{
            comment: comment,
            errorMessage: 'Error Creating Evaluation'
        })
    }
})

router.get('/:id/edit', async (req,res)=>{
    try{
        const comment = await Comment.findById(req.params.id);
        res.render('comments/edit',{comment:comment})
    }catch{
        res.redirect('/comments')
    }
})

router.put('/:id', async (req,res)=>{
    let comment;
    try{
        comment = await Comment.findById(req.params.id);
        comment.description = req.body.description;
        await comment.save();
        res.redirect(`/comments/${comment.id}`)
    }catch{
        if(comment==null){
            res.redirect('/');
        }else{
            res.render('comments/edit',{
                comment:comment,
                errorMessage:'Error updating Comment'
            })
        }
    }
})

router.delete('/:id', async (req,res)=>{
    let comment;
    try{
        comment = await Comment.findById(req.params.id);
        await comment.remove();
        res.redirect('/comments');
    }catch{
        if(comment==null){
            res.redirect('/');
        }else{
            res.redirect(`/comments/${comment.id}`)
        }
    }
})

async function renderNewComment(res, comment, hasError=false){
    renderFormComment(res, comment, 'new', hasError);
}

async function renderEditPage(res, comment, hasError=false){
    renderFormComment(res, comment, 'edit', hasError);
}

async function renderFormComment(res, comment, form, hasError=false){
    try{
        const course = await Course.find({});
        const params = {
            course: courses
        }
    }catch{

    }
}

module.exports = router;