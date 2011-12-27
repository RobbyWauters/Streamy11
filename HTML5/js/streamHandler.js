// JavaScript Document

var localURL = [ 'data/tweet.js',
				'data/comment.js',
				'data/file.js',
				'data/schedule.js',
				'data/blog.js'];
				
var publicURL = [ 'http://streamy11.appspot.com/api/entries/tweet',
				'http://streamy11.appspot.com/api/entries/comment',
				'http://streamy11.appspot.com/api/entries/file',
				'http://streamy11.appspot.com/api/entries/schedule',
				'http://streamy11.appspot.com/api/entries/blog'];

var objArray = new Array();
var doneLoading = false;
var maxCategory = 15; //temp 'unlimited'
var categories = ["twitter","comments","files","schedule","rss"] ;
var totalMax = 50;

$(document).ready(function() {
  init();
});


function init(){
	var delay = 200;
	
	$('#splash').delay(delay).animate({'opacity':'0'},500, 'easeOutCubic');
		
		setTimeout(function(){
			$('#splash').hide();
		},delay+500);

	loadFile(0);
}

function loadFile(count){
	if (count != categories.length){
		jqxhr = $.getJSON(publicURL[count], function(json) {
			if (json.results.length > 0) { 
				var cat = categories[count];
				var loop = Math.min(json.results.length,maxCategory);
				
				for (j = 0; j < loop; j++) { 
					var i = json.results[j]; 
					//var cat = i.type;
					
					//TODO: fill comments dynamically Temp hard coded comments
					var comments = new Array();
					for (k = 0; k < 5; k++){
						comments.push(new Comment("Koen Boncquet","Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
					}
					
					if (cat == "twitter"){		
						objArray.push(new TweetItem(cat,comments, i.guid, i.tags, i.to, i.author, i.text, i.isolanguagecode, i.set, i.link, i.lastmodified, i.datestamp));
					}else if (cat == "comments"){
						objArray.push(new CommentItem(cat, i.guid, i.author, i.text, i.tags, i.set, i.link, i.lastmodified, i.datestamp));
					}else if (cat == "files"){
						objArray.push(new FileItem(cat,comments, i.guid, i.course, i.author, i.text, i.link, i.lastmodified, i.breadcrumb, i.datestamp));
					}else if (cat == "schedule"){
						objArray.push(new ScheduleItem(cat,comments, i.guid, i.course, i.author, i.text, i.set, i.lastmodified, i.datestamp));
					}else if (cat == "rss"){
						objArray.push(new BlogItem(cat, i.guid, i.author, i.text, i.tags, i.title, i.set, i.link, i.lastmodified, i.datestamp));
					}
				}
			}
		});
		
		// if complete, load next file
		jqxhr.complete(function(){
			// recursive call to load next file
			loadFile(++count);
		});
	} else {
		quick_sort(objArray);
		objArray = shrinkArray();
		console.log("total objects loaded: " + objArray.length);
	}
}

function shrinkArray(){
	var tempArray = new Array();
	totalMax = Math.min(objArray.length,totalMax);
	for(var i=0; i < totalMax; i++){
		tempArray.push(objArray[i]);
	}
	return tempArray;
}

/**
 * Current category name that is showed. If var is "", all categories are showed.
**/
var showStream = "";

/*
	EVENTS
*/

$( '#streamview' ).live( 'pageinit',function(event){
   fillStream();
});

/*
	FUNCTIONS
*/

/**
 * Fills the stream with Notifcation objects.
 * Each Notification object represents a list item in the #thelist unordered list.
 * Tagname is used to know the category (filtering/menu) of a notification.
 * A Click event is registered with each notification, 'noti_events.js/showDetailOf' giving its id as parameter.
**/
function fillStream(data){
		if(data == null){
			for (index in objArray){
				var noti = objArray[index];
				var title = "";
				var subtitle = "";
				
				if (noti.category == "twitter"){		
					title = noti.author;
					subtitle = noti.tags;					
				}else if (noti.category == "comments"){
					title = noti.author + ' in ' + noti.set;
					subtitle = noti.text;
				}else if (noti.category == "files"){
					title = noti.text;
					subtitle = noti.course;
				}else if (noti.category == "schedule"){
					title = noti.text;
					subtitle = noti.course;
				}else if (noti.category == "rss"){
					title = noti.title;
					subtitle = noti.author;
				}
				
				var timeString = "";
				
				if (noti.datestamp){
					var today = new Date();
					/*today.setMonth(10);
					today.setDate(23);*/
					if (today>noti.datestamp){
						var day = noti.datestamp.getDate();
						var month = noti.datestamp.getMonth()+1;
						
						timeString = day + "/" + month;
					}else{
						var hours = "" + noti.datestamp.getHours();
						if (hours.length == 1) hours = "0" + hours;
						
						var minutes = "" + noti.datestamp.getMinutes();
						if (minutes.length == 1) minutes = "0" + minutes;
						
						timeString = hours + ':' + minutes;
					}
				}
				
				$('#thelist').append(
				'<li tagName="'+noti.category+'">'+
					'<a style="float:left;" onClick="showDetailOf('+index+')" href="#notiview">'+
						'<img src="images/li_'+noti.category+'.png" />'+
						'<div id="listLeft" style="margin-top:25px;float:left;">'+
							'<h3 style="margin:0;margin-bottom:15px;width:300px;font-size:18px">'+title+'</h3>'+
							'<p style="width:300px;font-size:14px;">'+subtitle+'</p>'+
						'</div>'+
					'</a>'+
					'<div id="listRight" style="float:left;width:70px;position:absolute;right:10px;float:left;margin-top:30px;">'+
						'<p style="margin-bottom:5px;font-size:20px">'+ timeString +'</p>'+
						'<img style="width:30px;height:26px;padding:0;margin:0;float:left;" src="images/comments_icon.png"/>'+
						'<p style="padding:0;margin:0;margin-top:2px;float:left;font-size:18px">'+
							10 + //TODO
						'</p>'+
					'</div>'+
				'</li>');
			}
			//force autoformat of JqueryMobile
			$('#thelist').listview('refresh');
		}else{
			for (var i=0; i<myArray[data].length; i++) {
				$('#thelist').append('<li tagName="'+cat+'"><a>'+myArray[data][i]+'</a></li>');
			}
			//force autoformat of JqueryMobile
			$('#thelist').listview('refresh');
		}
		if(showStream != ""){
			showOnlyStream(showStream);
		}
		if (myScroll)myScroll.refresh();
		if (commentScroll)commentScroll.refresh();
}

/**
 * Toggles the visibility of a notification category.
 * List items and bottom menu will change.
**/
function toggleStream(cat){
	showStream = "";
	//toggle visibility of category
	$('li[tagName="'+cat.name+'"]').toggle();
	//$('li[tagName="'+cat.name+'"]').slideToggle('0', function(){$(window).trigger("resize");});
	//toggle images (on/off state)
	if( cat.src.substr(-8) == "_off.png" ) {
		cat.src = "images/nav_"+cat.name+".png"
	}else{
		cat.src = "images/nav_"+cat.name+"_off.png"
	}
	
	if (myScroll)myScroll.refresh();
	if (commentScroll)commentScroll.refresh();
}

/**
 * Deactivates all categories first and then activates the given category.
**/
function showOnlyStream(cat){
	if(cat == null || cat == "all"){
		$('#thelist').each(function(){
			$(this).find('li').each(function(){
            	$(this).show();
			});
		});
		activateAllStreamMenuCategories();
	}else{
		//set variable
		showStream = cat;
		//hide all categories
		$('#thelist').each(function(){
			$(this).find('li').each(function(){
            	$(this).hide();
			});
		});
		//show given categorie
		$('li[tagName="'+cat+'"]').show();
		//hide all stream menu categories
		deactivateAllStreamMenuCategories();
		//show given stream menu categorie
		showStreamMenuCategorie(cat);
	}
	if (myScroll)myScroll.refresh();
	if (commentScroll)commentScroll.refresh();
}

/**
 * Activates all the cateogries. (bottom menu only)
 * This is triggered when the #streamview page is left.
 * 	NOTE: Not being used now. Using current functions make this redundant.
**/
function resetStream(){
	//activate every img tag in #streamMenu
	activateAllStreamMenuCategories();
}

/**
 * Activates all categories. (Bottom menu only)
**/
function activateAllStreamMenuCategories(){
	//activate every img tag in #streamMenu
	$('#streamMenu').each(function(){
		$(this).find('img').each(function(){
			if( this.src.substr(-8) == "_off.png" ) {
				this.src = "images/nav_"+this.name+".png"
			}
		});
	});
}

/**
 * Deactivates all categories. (Bottom menu only)
**/
function deactivateAllStreamMenuCategories(){
	//turn off every img tag in #streamMenu
	$('#streamMenu').each(function(){
		$(this).find('img').each(function(){
			if( this.src.substr(-8) != "_off.png" ) {
				this.src = "images/nav_"+this.name+"_off.png"
			}
		});
	});
}

/**
 * Show a given category in the bottom menu.
**/
function showStreamMenuCategorie(cat){
	 var img = $('img[name="'+cat+'"]').get(0);
	 img.src = "images/nav_"+img.name+".png"
}