var timeline = function(wallContainerUri) {

	"use strict";
	var mapEntry = function(entry) {
	  var event = {
	      id: entry.key.uri,
	      url: rootUrl + entry.key.uri,
	      start: entry.date,
	      end:   entry.date
	  };
	  
	  
	switch (entry.type) {
	
	case 'COLLABORATION_WORKSPACE_DOCUMENT_SHARED_ANOTHER_WORKSPACE':
		event.title = entry.initiator.displayName + ' shared the document: ' + entry.document.key.document.name + ' in workspace ' + entry.sharedFromSpace.name;
		event.description = entry.entryDescription;
	    event.class = 'event-important';
	    break;
	
	case 'WALL_POST':
      event.title = entry.initiator.displayName + ' posted: ' + entry.content;
      event.description = entry.entryDescription;
      event.class = 'event-important';
      break;
      
    case 'COLLABORATION_WORKSPACE_PARTICIPANT_JOINED':
      event.title = entry.initiator.displayName + ' joined.';
      event.description = entry.entryDescription;
      event.class = 'event-special';
      break;
      
    case 'COLLABORATION_WORKSPACE_DOCUMENT_SHARED':
      event.title = entry.initiator.displayName + ' shared the document ' + entry.document.key.document.name;
      event.description = entry.entryDescription;
      event.class = 'event-info';
      break;
      
    case 'COLLABORATION_WORKSPACE_TASK_CREATED':
      event.title = entry.initiator.displayName + ' created the task ' + entry.task.key.name;
      event.description = entry.entryDescription;
      event.class = 'event-info';
      break;
      
    case 'COLLABORATION_WORKSPACE_ACCESSED':
      event.title = entry.initiator.displayName + ' accessed the workspace';
      event.description = entry.entryDescription;
      event.class = 'event-special';
      break;
      
    case 'COLLABORATION_WORKSPACE_DOCUMENT_VIEWED':
      event.title = entry.initiator.displayName + ' viewed the document ' + entry.document.key.document.name;
      event.description = entry.entryDescription;
      event.class = 'event-inverse';
      break;
      
    case 'COLLABORATION_WORKSPACE_DOCUMENT_UPDATED':
    	event.title = entry.initiator.displayName + ' updated the document ' + entry.document.key.document.name;
        event.description = entry.entryDescription;
        event.class = 'event-inverse';
        break;
        
    case 'COLLABORATION_WORKSPACE_DOCUMENT_DOWNLOADED':
    	event.title = entry.initiator.displayName + ' downloaded the document ' + entry.document.key.document.name;
    	event.description = entry.entryDescription;
        event.class = 'event-info';
    	break;
    	
    case 'COMMENT_POSTED':
      event.title = entry.initiator.displayName + ' commented: ' + entry.comment.content;
      event.description = entry.entryDescription;
      event.class = 'event-inverse';
      break;
      
  default:
      event.title = entry.initiator.displayName + ' event ';
      event.description = entry.entryDescription;
      event.class = 'event-warning';
    }
	  return event;
	};
	
	var options = {
		events_source: function(fromDate, toDate) {
        var events = [];
        $.ajax({
          url:      rootUrl + "/api/" + wallContainerUri + "/wall/entries/by-date"
          		+ "?fromDate=" + formatTimestamp(fromDate)
          		+ "&toDate=" + formatTimestamp(toDate),
          dataType: 'json',
          type:     'GET',
          async:    false
        }).done(function(json) {
            for(var i=0; i < json.length; i++) {
              var entry = json[i];
              var event = mapEntry(entry);
              events.push(event);
            }
        });
        return events;
		},
		view: 'month',
		tmpl_path: '../resources/tmpls/timeline/',
		tmpl_cache: false,
		day: 'now',
		modal: '#events-modal',
		onAfterEventsLoad: function(events) {
			if(!events) {
				return;
			}
			var list = $('#eventlist');
			list.html('');

			$.each(events, function(key, val) {
				$(document.createElement('li'))
					.html('<a href="' + val.url + '">' + val.title + '</a>')
					.appendTo(list);
			});
		},
		onAfterViewLoad: function(view) {
			$('.timeline-title').text(this.getTitle());
			$('.btn-group button').removeClass('active');
			$('button[data-calendar-view="' + view + '"]').addClass('active');
		},
		classes: {
			months: {
				general: 'label'
			}
		}
	};

	var calendar = $('#calendar').calendar(options);

	$('.btn-group button[data-calendar-nav]').each(function() {
		var $this = $(this);
		$this.click(function() {
			calendar.navigate($this.data('calendar-nav'));
		});
	});

	$('.btn-group button[data-calendar-view]').each(function() {
		var $this = $(this);
		$this.click(function() {
			calendar.view($this.data('calendar-view'));
		});
	});

	$('#first_day').change(function(){
		var value = $(this).val();
		value = value.length ? parseInt(value) : null;
		calendar.setOptions({first_day: value});
		calendar.view();
	});

	$('#language').change(function(){
		calendar.setLanguage($(this).val());
		calendar.view();
	});

	$('#events-in-modal').change(function(){
		var val = $(this).is(':checked') ? $(this).val() : null;
		calendar.setOptions({modal: val});
	});
	$('#events-modal .modal-header, #events-modal .modal-footer').click(function(e){
		// e.preventDefault();
		// e.stopPropagation();
	});
};