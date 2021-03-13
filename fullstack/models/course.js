const mongoose = require('mongoose');
const Comment = require('./comment');

const courseSchema = new mongoose.Schema({
    name:{
        type: String,
        required: true
    }
});

courseSchema.pre('remove', function(next){
    Comment.find({courseNumber: this.id},(err,comments)=>{
        if(err){
            next(err);
        }else if(comments.length>0){
            next(new Error('This course has comments still'));
        }else{
            next();
        }
    })
})

module.exports = mongoose.model('Course', courseSchema);