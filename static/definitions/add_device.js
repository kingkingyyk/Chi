var stepDivs = [$("#step1"), $("#step2"), $("#step3"), $("#step4")];
var currentStepIndex = 0;

$("#back-button").click(function () {
    $(".action-error").text("");
    stepDivs[currentStepIndex].hide(300);
    currentStepIndex--;
    stepDivs[currentStepIndex].show(300);
    if (currentStepIndex == 0) {
        $(this).hide(70);
    }
});
$("#next-button").click(function () {
    var hasError = false;
    if (currentStepIndex == 0) {
        if ($("#select-building").val() === "new" && $("#new-building-name input").val().length==0) {
            $(".action-error").text("Building name cannot be empty!");
            hasError = true;
        }
        if (!hasError) {
            if ($("#select-building").val() != "new") {
                $("#new-site").hide();
                $('#loading-bar').show();
                $('#select-site').html("");
                $('#select-site').formSelect();
                $.get( "building/"+$("#select-building").val()+"/sites", {},
                        function(data) {
                            $('#select-site').html(data);
                            $('#select-site').formSelect();
                            $('#select-site').change();
                        }).always(function(data) {
                            $('#loading-bar').hide();
                        });
            } else {
                $('#select-site').html("<option value=\"new\">Create a new site...</option>");
                $('#select-site').formSelect();
                $('#select-site').change();
            }
        }
    } else if (currentStepIndex == 1) {
        var siteFields = {};
        siteFields["#new-site-name input"]="Site name";
        siteFields["#new-site-floor input"]="Floor";
        if ($("#select-site").val() === "new") {
            for (var key in siteFields) {
                if ($(key).val().length==0) {
                    $(".action-error").text(siteFields[key]+" cannot be empty!");
                    hasError = true;
                    break;
                }
            }
            if (!hasError && document.getElementById("new-site-floor-plan-input").files.length == 0) {
                $(".action-error").text("Floor plan cannot be empty!");
                 hasError = true;
            }
        }
    } else if (currentStepIndex == 2) {
        if ($("#new-device-name input").val().length==0) {
            $(".action-error").text("New device name cannot be empty!");
            hasError = true;
        }
        if (!hasError) {
            var createData = {};
            if ($("#select-building").val() != "new") createData["building"] = {"type": "existing", "id": parseInt($("#select-building").val())};
            else createData["building"] = {"type": "new", "name": $("#new-building-name input").val()};

            if ($("#select-site").val() != "new") createData["site"] = {"type": "existing", "id": parseInt($("#select-site").val())};
            else createData["site"] = {"type": "new", "name": $("#new-site-name input").val(), "floor": $("#new-site-floor input").val(), "floor-plan": floorPlanByte};

            createData["name"] = $("#new-device-name input").val();

            $("#loading-bar").show();
            $.post( "create", JSON.stringify(createData),
                    function(response) {
                    }).fail(function(response) {
                        $(".action-error").text("Failed to create entities!");
                    }).always(function(response) {
                        $('#loading-bar').hide();
                    });
        }
    } else if (currentStepIndex == stepDivs.length - 1) {
        window.location.href = '/definitions/devices/';
    }

    if (hasError) return;
    $(".action-error").text("");
    stepDivs[currentStepIndex].hide(300);
    currentStepIndex++;
    stepDivs[currentStepIndex].show(300);
    if (currentStepIndex == 1) {
        $("#back-button").show(70);
    } else if (currentStepIndex == stepDivs.length - 1) {
        $("#back-button").hide(70);
        $("#next-button").text("Done");
    }
});
for (i=1;i<stepDivs.length;i++) {
    stepDivs[i].hide();
}
$("#back-button").hide();

//Step 1
$("#select-building").change(function() {
    if ($( this ).val() === "new") {
        $("#new-building-name").show(300);
    } else {
        $("#new-building-name").hide(300);
    }
});
$( "#select-building" ).change();

//Step 2
$("#select-site").change(function() {
    if ($( this ).val() === "new") {
        $("#new-site").show(300);
    } else {
        $("#new-site").hide(300);
    }
});
$("#new-site").hide();

var floorPlanByte = null;
function previewFloorPlan() {
    $(".action-error").text("");
    var ele = document.getElementById("new-site-floor-plan-input");
    if (ele.files && ele.files[0]) {
        var reader = new FileReader();
        reader.onload = function () {
            $('#new-site-floor-plan-preview').attr('src',this.result);
        };
        reader.readAsDataURL(ele.files[0]);

        reader = new FileReader();
        reader.onload = function () {
            floorPlanByte = this.result;
            console.log(floorPlanByte);
        };
        reader.readAsBinaryString(ele.files[0]);
    }
}