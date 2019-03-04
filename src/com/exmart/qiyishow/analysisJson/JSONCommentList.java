package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Comment;

public class JSONCommentList{
	
	public List<Comment> analyzeCommentList(JSONObject strjson) throws JSONException {
		JSONArray jsonArr = strjson.getJSONArray("Content");
		List<Comment> list = new ArrayList<Comment>();
		for(int i = 0; i < jsonArr.length(); i ++){
			JSONObject job = jsonArr.getJSONObject(i);
			list.add(analyzeComment(job));
		}
		return list;
	}

	public Comment analyzeComment(JSONObject job) throws JSONException{
		Comment comment  = new Comment();
		comment.setId(job.getString("CommentId"));
		comment.setAutherAvatarUrl(job.getString("ImageUrl"));
		comment.setContent(job.getString("Content"));
		comment.setUserId(job.getString("UserId"));
		comment.setCommentUserName(job.getString("MemberName"));
		comment.setDate(job.getString("Time"));
		comment.setReplayUserName(job.getString("ReplyuserName"));
		comment.setReplayUserId(job.getString("ReplyuserId"));
		return comment;
	}
}