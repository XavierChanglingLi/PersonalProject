const mongoose = require('mongoose');

const commentSchema = new mongoose.Schema({
    //course number
    courseNumber:{
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref:'Course'
    },
    professor:{
        type: String
    },
    description:{
        type: String,
        required: true
    },
    publishDate:{
        type: Date,
        required: true,
        default: Date.now
    }
})

module.exports = mongoose.model('Comment', commentSchema);