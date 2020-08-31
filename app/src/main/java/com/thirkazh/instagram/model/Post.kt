package com.thirkazh.instagram.model

class Post {

    private var postId        : String = ""
    private var postimage     : String = ""
    private var publisher     : String = ""
    private var description   : String = ""

    constructor()
    constructor(postId: String, postImage: String, publisher: String, description: String) {
        this.postId = postId
        this.postimage = postImage
        this.publisher = publisher
        this.description = description
    }
    //get
    fun getPostId() : String{
        return postId
    }
    fun getPostImage() : String{
        return postimage
    }
    fun getPublisher() : String{
        return publisher
    }
    fun getDescription() : String{
        return description
    }
    //set
    fun setPostId(postId: String){
        this.postId = postId
    }
    fun setPostImage(postImage: String){
        this.postimage = postImage
    }
    fun setPublisher(publisher: String){
        this.publisher = publisher
    }
    fun setDescription(description: String){
        this.description = description
    }
}