/**
 * methods page
 */

(function(window, $) {

	'use strict';

	var ID = window.ID;

	var $window = $(window);

	ID.index = function() {

		(function() {
			var $container = $('#hero .isotope').isotope({
				itemSelector : '.element-item',
				layoutMode : 'fitRows',
				transitionDuration : '0.6s',
				masonry : {
					columnWidth : 110
				},
				cellsByRow : {
					columnWidth : 220,
					rowHeight : 220
				},
				masonryHorizontal : {
					rowHeight : 110
				},
				cellsByColumn : {
					columnWidth : 220,
					rowHeight : 220
				}
			});

			var isHorizontal = false;

			$('#hero .btn-group').on('click', 'button', function() {
				// adjust container sizing if layout mode is changing from
				// vertical or horizontal
				var $this = $(this);
				var isHorizontalMode = !!$this.attr('data-is-horizontal');
				if (isHorizontal !== isHorizontalMode) {
					var containerStyle = isHorizontalMode ? {
						height : $window.height() * 0.7
					} : {
						width : 'auto'
					};
					$container.css(containerStyle);
					isHorizontal = isHorizontalMode;
				}
				// change layout mode
				var layoutModeValue = $this.attr('data-layout-mode-value');
				$container.isotope({
					layoutMode : layoutModeValue
				});
			});

			var $container = $('.isotope'), $select = $('.filters select');

			$("select").selectpicker({ style: 'btn-huge btn-primary btn-small'});
			
			$select.change(function() {
				var filters = $(this).val();
				$('.active').removeClass('active');
				if (filters != '.element-item') {
					$(filters).addClass('active');
				}
				$container.isotope({
					filter : filters
				});
			});
			
			$('div.sort-by .btn').on("click", function(){
			      $('div.sort-by .btn').removeClass("active");
			      $(this).addClass('active');
			});

			$('div.filters .btn').on("click", function(){
			      $('div.filters .btn').removeClass("active");
			      $(this).addClass('active');
			});


		})();

	};

})(window, jQuery);