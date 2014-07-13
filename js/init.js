/*********************************************************************************/
/* Settings                                                                      */
/*********************************************************************************/

	var _settings = {

		// dropotron
			dropotron: {
				offsetY: -20,
				mode: 'fade',
				noOpenerFade: true
			},

		// skelJS
			skelJS: {
				prefix: 'css/style',
				resetCSS: true,
				boxModel: 'border',
				containers: 1200,
				breakpoints: {
					'wide': { range: '*', containers: 1200, hasStyleSheet: false },
					'normal': { range: '-1280', containers: 1000 },
					'narrow': { range: '-1080', lockViewport: true, containers: '95%', grid: { gutters: 30 } },
					'narrower': { range: '-820', lockViewport: true, containers: '95%', grid: { gutters: 26 } },
					'mobile': { range: '-720', lockViewport: true, containers: '90%', grid: { collapse: true } }
				}
			},

		// skelJS Plugins
			skelJSPlugins: {
				panels: {
					panels: {
						navPanel: {
							breakpoints: 'mobile',
							position: 'left',
							style: 'push',
							size: '80%',
							html: '<div data-action="navList" data-args="nav"></div>'
						}
					},
					overlays: {
						titleBar: {
							breakpoints: 'mobile',
							position: 'top-left',
							width: '100%',
							height: 44,
							html: '<span class="toggle" data-action="togglePanel" data-args="navPanel"></span>' +
								  '<span class="title" data-action="copyHTML" data-args="logo"></span>'
						}
					}
				}
			}

	};

/*********************************************************************************/
/* jQuery Plugins                                                                */
/*********************************************************************************/

	// formerize
		jQuery.fn.n33_formerize=function(){var _fakes=new Array(),_form = jQuery(this);_form.find('input[type=text],textarea').each(function() { var e = jQuery(this); if (e.val() == '' || e.val() == e.attr('placeholder')) { e.addClass('formerize-placeholder'); e.val(e.attr('placeholder')); } }).blur(function() { var e = jQuery(this); if (e.attr('name').match(/_fakeformerizefield$/)) return; if (e.val() == '') { e.addClass('formerize-placeholder'); e.val(e.attr('placeholder')); } }).focus(function() { var e = jQuery(this); if (e.attr('name').match(/_fakeformerizefield$/)) return; if (e.val() == e.attr('placeholder')) { e.removeClass('formerize-placeholder'); e.val(''); } }); _form.find('input[type=password]').each(function() { var e = jQuery(this); var x = jQuery(jQuery('<div>').append(e.clone()).remove().html().replace(/type="password"/i, 'type="text"').replace(/type=password/i, 'type=text')); if (e.attr('id') != '') x.attr('id', e.attr('id') + '_fakeformerizefield'); if (e.attr('name') != '') x.attr('name', e.attr('name') + '_fakeformerizefield'); x.addClass('formerize-placeholder').val(x.attr('placeholder')).insertAfter(e); if (e.val() == '') e.hide(); else x.hide(); e.blur(function(event) { event.preventDefault(); var e = jQuery(this); var x = e.parent().find('input[name=' + e.attr('name') + '_fakeformerizefield]'); if (e.val() == '') { e.hide(); x.show(); } }); x.focus(function(event) { event.preventDefault(); var x = jQuery(this); var e = x.parent().find('input[name=' + x.attr('name').replace('_fakeformerizefield', '') + ']'); x.hide(); e.show().focus(); }); x.keypress(function(event) { event.preventDefault(); x.val(''); }); });  _form.submit(function() { jQuery(this).find('input[type=text],input[type=password],textarea').each(function(event) { var e = jQuery(this); if (e.attr('name').match(/_fakeformerizefield$/)) e.attr('name', ''); if (e.val() == e.attr('placeholder')) { e.removeClass('formerize-placeholder'); e.val(''); } }); }).bind("reset", function(event) { event.preventDefault(); jQuery(this).find('select').val(jQuery('option:first').val()); jQuery(this).find('input,textarea').each(function() { var e = jQuery(this); var x; e.removeClass('formerize-placeholder'); switch (this.type) { case 'submit': case 'reset': break; case 'password': e.val(e.attr('defaultValue')); x = e.parent().find('input[name=' + e.attr('name') + '_fakeformerizefield]'); if (e.val() == '') { e.hide(); x.show(); } else { e.show(); x.hide(); } break; case 'checkbox': case 'radio': e.attr('checked', e.attr('defaultValue')); break; case 'text': case 'textarea': e.val(e.attr('defaultValue')); if (e.val() == '') { e.addClass('formerize-placeholder'); e.val(e.attr('placeholder')); } break; default: e.val(e.attr('defaultValue')); break; } }); window.setTimeout(function() { for (x in _fakes) _fakes[x].trigger('formerize_sync'); }, 10); }); return _form; };

/*********************************************************************************/
/* Initialize                                                                    */
/*********************************************************************************/

	// skelJS
		skel.init(_settings.skelJS, _settings.skelJSPlugins);

	// jQuery
		jQuery(function() {

			var $window = $(window);

			// Forms
				if (skel.vars.IEVersion < 10)
					$('form').n33_formerize();

			// Dropdowns
				jQuery('#nav > ul').dropotron(_settings.dropotron);


			// custimizations for JHawtCode

				$('.myemail').html(function(){
					var e = "jhawtcode";
					var a = "@";
					var d = "gmail";
					var c = ".com";
					var h = 'mailto:' + e + a + d + c;
					$(this).attr('href', h);
					return e + a + d + c;
				});

		});

