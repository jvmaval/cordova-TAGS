window.echo = function(natureOfTalk)
    {
        alert("Inside JS Interface");
        cordova.exec(function(result){alert("Result is : "+result);},
                     function(error){alert("Some Error happened : "+ error);},
                     "TAGS","readTAG",[]);
    };