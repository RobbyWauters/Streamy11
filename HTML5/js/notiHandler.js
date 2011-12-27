// JavaScript Document

/**
 * File handling the events and eventhandling of the notification-page.
 * Actions such as showing detail page
**/

var colorArray = {};
colorArray["twitter"] = '#498ac2';
colorArray["schedule"] = '#ebcf30';
colorArray["comments"] = '#6ba635';
colorArray["files"] = '#b64653';
colorArray["rss"] = '#eba40c';

/*
	EVENTS
*/


/*
	FUNCTIONS
*/
/**
 * Show the detail page for the given notification (ref by id).
 * Color, course name is changed. rest to follow.
**/
function showDetailOf(id){
	var currId = id;
	var not = objArray[id];
	//console.log('show ' + not);
	
	//color header
	$('#noti_header').css('border-top-color',colorArray[not.category]);
	
	//eventually hide breadcrumb
	$('#breadcrumb').hide();
	
	$('#noti_text').text("");
	
	if (not.category == "twitter"){		
		$('#noti_title').text(not.set);
		$('#noti_text').append('<h3>' + not.author + '</h3>');
		$('#noti_text').append(not.text);
		$('#noti_text').append('<p><a target="_blank" href="' + not.link + '"> Open on twitter' + '</a></p>');
		fillComments(not);
	}else if (not.category == "comments"){
		$('#noti_title').text(not.set);
		$('#noti_text').append('<h3>' + not.author + '</h3>');
		$('#noti_text').append(not.text);
		$('#noti_text').append('<p><a target="_blank" href="' + not.link + '"> Open in new window' + '</a></p>');
		$('#noti_comment_container').hide();
	}else if (not.category == "files"){
		$('#noti_title').text(not.course);
		$('#breadcrumb').show();
		var breadcrumbs = not.breadcrumb.split("/");
		$('#breadcrumb1').text(breadcrumbs[0]);
		$('#breadcrumb2').text(breadcrumbs[1]);
		$('#breadcrumb3').text(breadcrumbs[2]);
		$('#noti_text').append('<h3>' + not.author + '</h3>');
		$('#noti_text').append(not.text);
		fillComments(not);
	}else if (not.category == "schedule"){
		$('#noti_title').text(not.course);
		$('#noti_text').append('<h3>' + not.author + '</h3>');
		$('#noti_text').append(not.text);
		fillComments(not);
	}else if (not.category == "rss"){
		$('#noti_title').text(not.author);
		$('#noti_text').append('<h3>' + not.title + '</h3>');
		$('#noti_text').append(not.text);
		$('#noti_text').append('<p><a target="_blank" href="' + not.link + '"> Open in new window' + '</a></p>');
		$('#noti_comment_container').hide();
	}
}

function fillComments(not){
	$('#noti_comment_container').show();
	$('#noti_comment_container').html("");
	
	for (i = 0; i < not.comments.length; i++){
		var comment = not.comments[i];
		$('#noti_comment_container').append(
			'<li class="noti_comment">' +
				'<div class="noti_comment_left"><h3>' + comment.author + '</h3></div>' +
				'<div class="noti_comment_right"><p style="multiline:yes;">' + comment.text + '</p></div>' +
                '<div class="noti_comment_inner"></div>' +
            '</li>');
	}
}