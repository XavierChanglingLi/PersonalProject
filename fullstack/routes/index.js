//set up all the routes for the application
const express = require('express');
const router = express.Router();
const Comment = require('../models/comment');

router.get('/',(req, res)=>{
    res.render('index');
})

module.exports = router;