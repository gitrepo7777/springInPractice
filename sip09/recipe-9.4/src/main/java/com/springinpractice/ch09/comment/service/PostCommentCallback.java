/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch09.comment.service;

import com.springinpractice.ch09.comment.model.Comment;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface PostCommentCallback {
	
	void post(Comment comment);
}
