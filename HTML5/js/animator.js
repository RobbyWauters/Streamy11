
/**
 * Registers a click-event to the streamy-meter
 * [Temporal event]
**/
$( '#streamy-container' ).live( 'click', 
						   function(event){
							   animateStreamyArrow(Math.random()*180-90);
						   })									  
				  
/**
 * Holds the current angle on which the Streamy arrow is positioned
**/
var currentAngle = 0;

/**
 * Animates the streamy-meter to a certain angle
 * [Uses jQueryRotate & JQuery.easing]
**/
var animateStreamyArrow = function (angle){
   $( '#streamy-arrow' ).rotate({
	  angle:currentAngle, 
	  animateTo: angle,
	  easing: $.easing.easeInOutExpo
   });
   currentAngle = angle;
   updateSlogan();
}

var slogans = [ 'You are hopelessly neglecting schoolwork',
				'Start spending more time on school now',
				"It's bad, try to get some work done fast!",
				"It's getting worse, try to focus",
				'Below average, you can do better!',
				'Average performance, could be much better',
				'You are doing fine',
				'Great work',
				'Doing great, perfection is around the corner',
				'Perfect score! Keep up the good work'];
				
function updateSlogan(){
	var index = (currentAngle+90)/18; //currentAngle range: -90 tot 90
	$('#footertext').html(slogans[Math.floor(index)]);
}