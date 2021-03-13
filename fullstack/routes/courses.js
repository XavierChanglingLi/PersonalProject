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

router.get('/:id', async (req,res)=>{
    try{
        const course = await Course.findById(req.params.id);
        const comments = await Comment.find({courseNumber: course.id}).limit(20).exec();
        res.render('courses/show',{
            course: course,
            commentsOfCourse: comments
        })
    }catch{
        res.redirect('/');
    }
})

router.get('/:id/edit', async(req,res)=>{
    try{
        const course = await Course.findById(req.params.id);
        res.render('courses/edit',{course: course});
    }catch{
        res.redirect('/courses')
    }
})

router.put('/:id', async(req,res)=>{
    let course;
    try{
        course = await Course.findById(req.params.id);
        course.name = req.body.name;
        await course.save();
        res.redirect(`/courses/${course.id}`);
    }catch{
        if (course==null){
            res.redirect('/');
        }else{
            res.render('courses/edit',{
                course: course,
                errorMessage: 'Error updating course'
            })
        }
    }
})

router.delete('/:id', async(req, res)=>{
    let course;
    try{
        course = await Course.findById(req.params.id);
        await course.remove();
        res.redirect('/courses');
    }catch{
        if(course==null){
            res.redirect('/');
        }else{
            res.redirect(`/courses/${course.id}`);
        }
    }
})

module.exports = router;