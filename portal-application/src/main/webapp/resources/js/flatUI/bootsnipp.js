var bootsnipp = function() {	
	// boostrap component
	var panels = $('.user-infos');
	var panelsButton = $('.dropdown-product');
    panels.hide();

    //Click dropdown
    panelsButton.on("click",function() {
        //get data-for attribute
        var dataFor = $(this).attr('data-for');
        var idFor = $(dataFor);
        //current button
        var currentButton = $(this);
        idFor.slideToggle(400, function() {
            //Completed slidetoggle
            if(idFor.is(':visible'))
            {
                currentButton.html('<i class="icon-chevron-up text-muted"></i>');
            }
            else
            {
                currentButton.html('<i class="icon-chevron-down text-muted"></i>');
            }
        })
    });
};
