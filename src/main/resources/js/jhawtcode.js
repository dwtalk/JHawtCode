jQuery.fn.extend({
    insertAtCaret: function(myValue){
        return this.each(function(i) {
            if (document.selection) {
                //For browsers like Internet Explorer
                this.focus();
                var sel = document.selection.createRange();
                sel.text = myValue;
                this.focus();
            }
            else if (this.selectionStart || this.selectionStart == '0') {
                //For browsers like Firefox and Webkit based
                var startPos = this.selectionStart;
                var endPos = this.selectionEnd;
                var scrollTop = this.scrollTop;
                this.value = this.value.substring(0, startPos)+myValue+this.value.substring(endPos,this.value.length);
                this.focus();
                this.selectionStart = startPos + myValue.length;
                this.selectionEnd = startPos + myValue.length;
                this.scrollTop = scrollTop;
            } else {
                this.value += myValue;
                this.focus();
            }
        });
    }
});

//--------------------------------------------------------------------------
jQuery(document).ready(function($) {

    var $jhcDiv = $('<div id="jhc_parent"><span id="jhc_lic">Licensed For: |lCode|</span><textarea id="jhc" wrap="hard" rows="5">JHawtCode' +
        '</textarea></div>' +
        '<img src=\"http://www.google-analytics.com/collect?v=1&amp;tid=UA-52728333-1&amp;cid=|systemUUID|&amp;uid=|username|&amp;t=event&amp;ec=console&amp;ea=open&amp;cs=|appname|&amp;ck=|license|&amp;cn=usage&amp;an=jhawtcode&amp;av=1.0.0\" />' +
        '');

    $('body').prepend($jhcDiv)

    var self = $('#jhc');
    var parent = $('#jhc_parent');
    var focus = false;
    var propmatch = /^(:sp)\s([a-zA-Z0-9\._]+)\s([a-zA-Z0-9\._]+)$/;
    var jarmatch = /^(:rr)\s([a-zA-Z0-9-:\/\._]+)$/;
    var importmatch = /^(import\s[a-zA-Z0-9_\.]+)/;
    var globalMethodLineOrVarMatch = /^(public|private|protected|package)\s(.+;)/;
    var functionBeginMatch = /^(public|private|protected|package)\s(.+[(].*[)].*[^;])/;
    var clearOnInput = true;

    $(function() {
        $(window).keypress(function(e) {
            if(e.which == 96) {
                parent.slideToggle();
                focus = !focus;
                if(focus) {
                    self.focus();
                } else {
                    self.focusout();
                }
            }
        });
    });

    $(self).bind('input', function() {
        var c = this.selectionStart,
            r = /[`]/gi,
            v = $(this).val();
        if(r.test(v)) {
            $(this).val(v.replace(r, ''));
            c--;
        }
        this.setSelectionRange(c, c);
    });

    $(self).mousedown(function(e) {
        if (clearOnInput) {
            $(self).val('');
            clearOnInput = false;
        }
    });

    $(self).keydown(function(e) {
        if(clearOnInput) {
            $(self).val('');
            clearOnInput = false;
        }

        if (e.which === 13 && !e.shiftKey) {
            //e.preventDefault();
            var text = $(self).val();
            var textarray = text.split('\n');
            var lastCMD = textarray[textarray.length - 1];

            if(lastCMD == ":w") {
                var code = text.slice(0,-3);
                e.preventDefault();
                $(self).val('');
                var imports = "";
                var globals = "";
                var nonFunctions = "";
                var methods = "";
                var bracketCounter = 0;
                var inFunction = false;

                for (var i= 0; i<textarray.length; i++) {
                    if(importmatch.test(textarray[i])) {
                        imports = imports + textarray[i];
                        textarray[i] = "";
                    }
                    if(globalMethodLineOrVarMatch.test(textarray[i])) {
                        globals = globals + textarray[i];
                        textarray[i] = "";
                    }
                }

                textarray[textarray.length-1] = "";

                //loop to parse the functions from the code lines
                for (var i= 0; i<textarray.length; i++) {
                    textarray[i] = textarray[i].trim();

                    //we find a function and ignore the function lines
                    if(!inFunction && functionBeginMatch.test(textarray[i])) {
                        inFunction = true;
                        if(textarray[i].indexOf("{") > -1) {
                            bracketCounter += (textarray[i].split("{").length - 1);
                        }
                        continue;
                    }

                    if((textarray[i].indexOf("{") > -1) || (textarray[i].indexOf("}") > -1)) {
                        //adjust bracket count
                        bracketCounter += (textarray[i].split("{").length - 1);
                        bracketCounter -= (textarray[i].split("}").length - 1);
                    }
                    //add the line to the code stuff
                    if(!inFunction) {
                        nonFunctions += textarray[i];
                        textarray[i] = "";
                    }
                    //reset if we are not in a function
                    if(inFunction && bracketCounter == 0) {
                        inFunction = false;
                    }
                }

                methods = textarray.join("\n");

                $.ajax({
                    url: "/jhawtcode/dynacode",
                    type: "POST",
                    data: {"code" : nonFunctions, "imports" : imports, "globals" : globals, "methods" : methods},
                    cache: false,
                    dataType: "text",
                    success: function(data, textStatus, jqXHR) {
                        $(self).insertAtCaret(data+"\n");
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $(self).insertAtCaret("JHawtCode: Unknown Error, check console for more information\n");
                        console.log(textStatus);
                        console.log(jqXHR);
                        console.log(errorThrown);
                    }
                })
                clearOnInput = true;
            } else if (lastCMD == ":q") {
                $(self).val('');
                e.preventDefault();
                parent.slideToggle();
            } else if (jarmatch.test(lastCMD)) {
                $(self).val('');
                var cmdMatch = jarmatch.exec(lastCMD);

                $.ajax({
                    url: "/jhawtcode/dynajar",
                    type: "POST",
                    data: {"url" : ""+cmdMatch[2]+""},
                    cache: false,
                    dataType: "text",
                    success: function(data, textStatus, jqXHR) {
                        $(self).insertAtCaret("Loaded: "+cmdMatch[2]);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $(self).insertAtCaret("JHawtCode: Unknown Error, check console for more information\n");
                        console.log(textStatus);
                        console.log(jqXHR);
                        console.log(errorThrown);
                    }
                })
                clearOnInput = true;
            } else if (propmatch.test(lastCMD)) {
                $(self).val('');
                var cmdMatch = propmatch.exec(lastCMD);

                $.ajax({
                    url: "/jhawtcode/dynaprop",
                    type: "POST",
                    data: {"propkey" : ""+cmdMatch[2]+"", "value" : ""+cmdMatch[3]+""},
                    cache: false,
                    dataType: "text",
                    success: function(data, textStatus, jqXHR) {
                        $(self).insertAtCaret("Updated: "+cmdMatch[2]+"="+cmdMatch[3]);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $(self).insertAtCaret("JHawtCode: Unknown Error, check console for more information\n");
                        console.log(textStatus);
                        console.log(jqXHR);
                        console.log(errorThrown);
                    }
                })
            }
        }
    });

});
