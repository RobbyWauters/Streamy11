/**
  * Class that defines all the different notification items
  *
  */

function Comment(author, text){
	this.author = author;
	this.text = text;
}
  
function TweetItem(category, comments, guid, tags, to, author, text, isolanguagecode, set, link, lastmodified, datestamp){
	this.category = category;
	this.comments = comments;
	this.guid = guid;
	this.tags = tags;
	this.to = to;
	this.author = author;
	this.text = text;
	this.isolanguagecode = isolanguagecode;
	this.set = set;
	this.link = link;
	this.lastmodified = lastmodified;
	this.datestamp = new Date(datestamp);
}

function FileItem(category, comments, guid, course, author, text, link, lastmodified, breadcrumb, datestamp){
	this.category = category;
	this.comments = comments;
	this.guid = guid;
	this.course = course;
	this.author = author;
	this.text = text;
	this.link = link;
	this.lastmodified = lastmodified;
	this.datestamp = new Date(datestamp);;
	this.breadcrumb = breadcrumb;
}

function CommentItem(category, guid, author, text, tags, set, link, lastmodified, datestamp){
	this.category = category;
	this.guid = guid;
	this.author = author;
	this.text = text;
	this.tags = tags;
	this.set = set;
	this.link = link;
	this.lastmodified = lastmodified;
	this.datestamp = new Date(datestamp);;
}

function BlogItem(category, guid, author, text, tags, title, set, link, lastmodified, datestamp){
	this.category = category;
	this.guid = guid;
	this.author = author;
	this.text = text;
	this.tags = tags;
	this.title = title;
	this.set = set;
	this.link = link;
	this.lastmodified = lastmodified;
	this.datestamp = new Date(datestamp);;
}

function ScheduleItem(category,comments, guid, course, author, text, set, lastmodified, datestamp){
	this.category = category;
	this.comments = comments;
	this.guid = guid;
	this.course = course;
	this.author = author;
	this.text = text;
	this.set = set;
	this.lastmodified = lastmodified;
	this.datestamp = datestamp;
}