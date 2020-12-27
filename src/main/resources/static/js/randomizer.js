function randomize() {
    const text = $("#textarealeft").val()
    let json;
    try {
        json = JSON.parse(text)
        $("#textarealeft").html(JSON.stringify(json, null, 4));
    } catch (e) {
        $("#textarearight").html(getErrorMessage(e))
        return
    }
    console.log(json)

    console.log(JSON.stringify(json))
    $.ajax({
        type: "POST",
        url: "/json/randomize",
        data: JSON.stringify(json),
        success: function (data) {
            console.log(data)
            $("#textarearight").html(JSON.stringify(data, null, 4));
        },
        dataType: "json"
    });
}

const getErrorMessage = function (error) {
    return (`[${error instanceof SyntaxError ? 'EXPLICIT' : 'INEXPLICIT'}] ${error.name}: ${error.message}`);
};

