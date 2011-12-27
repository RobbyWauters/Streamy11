/*
	EVENTS
*/
$(document).ready(resizeHandler);
$( '#streamview' ).live( 'pageinit',resizeHandler);
$(window).resize(resizeHandler);

$(document).bind("pagechange", function( e, data ) {
	formatStreamList();
	if (myScroll) myScroll.refresh();
	if (commentScroll)commentScroll.refresh();
});

/*
	FUNCTIONS
*/

/**
 * Autoformats the content div and the positioning if the streamy meter
**/
function resizeHandler(){
	displayWindowsSize();
	autoformatContent();
	formatStreamyMeter();
	formatStreamList();
}

/**
 * show current window size in textfield (debugging)
**/
function displayWindowsSize(){
	$('#footertext').html($(window).width()+'x'+$(window).height());
}

/**
 * Adjusts the height of the content so it stays between the absolute top and footer
**/
function autoformatContent(){
	var h = $(window).height();//800
	var footer = 109;
	var header = 55;
	var newHeight = h - footer - header - 43;
	//console.log('setting height of content: ' + newHeight);
	$('#content').css('height', newHeight);
}

/**
 * Adjusts the height of the main_spacer to position streamy meter at the bottom
**/
function formatStreamyMeter(){
	$('#streamy-container').css('width',$(window).width());
}

function formatStreamList(){
	var menuHeight = $('#heightRef').height();
	if (menuHeight != 0){
		$('#wrapper').css('height',$(window).height()-menuHeight-72);
	}
	
	var breadCrumbVisible = $("#breadcrumb").is(":visible");
	var breadcrumbH = 0;
	if(breadCrumbVisible)breadcrumbH = $("#breadcrumb").height();
	var clean_headerH = $('#clean_header').height();
	var textAreaH = $('#textArea').height()+42; //42 + textArea
	$('#commentWrapper').css('top',textAreaH+clean_headerH+breadcrumbH);
	$('#commentWrapper').css('height',$(window).height()-textAreaH-clean_headerH-breadcrumbH);
}

