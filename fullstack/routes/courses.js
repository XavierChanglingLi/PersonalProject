const express = require('express');
const router = express.Router();
const Course = require('../models/course');
const Comment = require('../models/comment');

//all courses route
router.get('/',async(req,res)=>{
    let searchOptions = {};
    if(req.query.name!==null&&req.query.name!==''){
        searchOptions.name = new RegExp(req.query.name,'i')
    }
    try{
        const courses = await Course.find(searchOptions);
        res.render('courses/index',{courses: courses, searchOptions: req.query});
    }catch{
        res.redirect('/');
    }
});

//new course route
router.get('/new',(req,res)=>{
    res.render('courses/new',{course: new Course()})
});

//create course route
router.post('/', async (req,res)=>{
    const course = new Course({
        name: req.body.name
    })
    try{
        const newCourse = await course.save();
        res.redirect(`courses/${newCourse.id}`);
    }catch{
        res.render('courses/new',{
            course: course,
            errorMessage: 'Error Creating Author'
        })
    }
});

module.exports = router;