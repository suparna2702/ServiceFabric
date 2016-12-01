// formats like: yyyy-MM-dd'T'HH:mm:ss.SSS
function formatTimestamp(d) {
  return d.getFullYear()
    + '-' + (d.getMonth() + 1)
    + '-' + d.getDate()
    + "T" + d.getHours()
    + ':' + d.getMinutes()
    + ':' + d.getSeconds()
    + '.' + d.getMilliseconds();
}

//the bootsnipp panel
$(document).on('click', '.panel-heading span.clickable', function(e){
    var $this = $(this);
	if(!$this.hasClass('panel-collapsed')) {
		$this.parents('.panel').find('.panel-body').slideUp();
		$this.addClass('panel-collapsed');
		$this.find('i').removeClass('icon-chevron-up').addClass('icon-chevron-down');
	} else {
		$this.parents('.panel').find('.panel-body').slideDown();
		$this.removeClass('panel-collapsed');
		$this.find('i').removeClass('icon-chevron-down').addClass('icon-chevron-up');
	}
})

//tooltip and pop-over
$(document).ready(function(){
	       $("[rel='tooltip']").tooltip();
		   $("[rel='popover']").popover({
		     'trigger':'hover',
             'html':true
		   });
 })

!function($) {

	$(function() {
		//Popover Share Comment
		$(".new-content .post-wall .body .popover").show();

		// Tabs
		$(".nav-tabs a").on('click', function(e) {
			e.preventDefault();
			$(this).tab("show");
		});

		
		var oldMessage = $(".coment").val();
		if ($("#formSmall").find(".bodyForm").size() > 0) {
			$("#formSmall .memberName").hide();
			$("#formSmall .employerName").hide();
			$("#formSmall .btnShare").hide();
			$("#formSmall .btnCancel").hide();
			$("#formSmall .btnClear").hide();
			$("#formSmall .counterCharacters").hide();
			$("#formBig").hide();
		}

		$(".coment").on("click", function() {
			if ($("#formSmall").find(".bodyForm").size() > 0) {
				$("#formSmall .bodyForm").removeClass("span12");
				$("#formSmall .bodyForm").addClass("offset2 span8");
				$("#formSmall .bodyForm").appendTo("#formBig");
				$("#shareform").hide();
				$("#formSmall").hide();
				$("#formBig").show();
				$("#formBig .memberName").show();
				$("#formBig .employerName").show();
				$("#formBig .btnShare").show();
				$("#formBig .btnCancel").show();
				$("#formBig .btnClear").show();
				$("#formBig .counterCharacters").show();
				$("#formBig .coment").focus();
			}
		});

		$("button.btnShare").on("click", function() {
			if ($("#formBig").find(".bodyForm").size() > 0) {
				$("#formBig .bodyForm").removeClass("offset2 span8");
				$("#formBig .bodyForm").addClass("span12");
				$("#formBig .bodyForm").appendTo("#formSmall");
				$("#formBig").hide();
				$("#shareform").show();
				$("#formSmall").show();
				$("#formSmall .memberName").hide();
				$("#formSmall .employerName").hide();
				$("#formSmall .btnShare").hide();
				$("#formSmall .btnCancel").hide();
				$("#formSmall .btnClear").hide();
				$("#formSmall .counterCharacters").hide();
				oldMessage = $(".coment").val();
			}
		});

		$("input.btnCancel").on("click", function() {
			if ($("#formBig").find(".bodyForm").size() > 0) {
				$("#formBig .bodyForm").removeClass("offset2 span8");
				$("#formBig .bodyForm").addClass("span12");
				$("#formBig .bodyForm").appendTo("#formSmall");
				$("#formBig").hide();
				$("#shareform").show();
				$("#formSmall").show();
				$("#formSmall .memberName").hide();
				$("#formSmall .employerName").hide();
				$("#formSmall .btnShare").hide();
				$("#formSmall .btnCancel").hide();
				$("#formSmall .btnClear").hide();
				$("#formSmall .counterCharacters").hide();
				$(".coment").val(oldMessage);
			}
		});
		
		bootsnipp();

		/*Flat UI Datepicker component*/

		$("input.btnClear").on("click", function() {
			$(".coment").val("");
			var oldMessage = $(".coment").val();
		});
		
		var datepickerSelector = '#datepicker-01';
		$(datepickerSelector).datepicker({
		  showOtherMonths: true,
		  selectOtherMonths: true,
		  dateFormat: "d MM, yy",
		  yearRange: '-1:+1'
		}).prev('.btn').on('click', function (e) {
		  e && e.preventDefault();
		  $(datepickerSelector).focus();
		});

		// Now let's align datepicker with the prepend button
		$(datepickerSelector).datepicker('widget').css({'margin-left': -$(datepickerSelector).prev('.btn').outerWidth() - 2});
		
	})
}(window.jQuery)