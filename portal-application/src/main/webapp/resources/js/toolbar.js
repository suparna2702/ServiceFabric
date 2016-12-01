$('div.highlightableWrapper').live(
		{
			mouseover : function() {
				$(this).find(".highlightableElement").addClass(
						'highlightedBorder').addClass('higlightedBorderRadius')
						.removeClass('borderSpaces');
				$(this).addClass('highlightableWrapperBorder');
				$(this).addClass('wrapperBorderRadius');
			},
			mouseout : function() {
				$(this).find(".highlightableElement").removeClass(
						'highlightedBorder').removeClass(
						'strongerHighlightedBorder').removeClass(
						'higlightedBorderRadius').addClass('borderSpaces');
				$(this).removeClass('highlightableWrapperBorder');
				$(this).removeClass('wrapperBorderRadius');
			}
		});

$('.highlightableElement').live(
		{
			mouseover : function() {
				$(this).addClass('strongerHighlightedBorder').addClass(
						'higlightedBorderRadius');
			},
			mouseout : function() {
				$(this).removeClass('strongerHighlightedBorder').removeClass(
						'higlightedBorderRadius');
			}
		});
