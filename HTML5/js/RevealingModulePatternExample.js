// JavaScript Document

var resizeHandler = function(){

	/*
	 * Adjusts the height of the content so it stays between the absolute top and footer
	*/
	var autoformat = function (){
		var h = $(window).height();
		var footer = 109;
		var header = 55;
		var newHeight = h - footer - header - 43;
		$('#content').css('height', newHeight);
		//alert($('#content').html);
	}
	
	return{
		autoformat:autoformat
	}

}