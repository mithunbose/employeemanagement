$(document).ready(function () {
    var page = 0;
    loadData();

    function download(filename, text) {
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
        element.setAttribute('download', filename);
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    }

    function loadData(e) {
        $.ajax({
            url: '/EmployeeManagementService/rest/getEmployeeDetails?page=' + page,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            error: function () {
                alert("Error fetching employee data!");
            }
        }).then(function (data) {
            if (data.length === 0) {
                alert("No employee data present!");
                if (page !== 0)
                    page = page - 1;
            } else {
                $('#pageTable tr').not(':first').remove();
                var html = '';
                for (var i = 0; i < data.length; i++)
                    html += '<tr><td>' + data[i].employeeId + '</td><td>' +
                            data[i].name + '</td><td>' +
                            data[i].department + '</td><td>' +
                            data[i].designation + '</td><td>' +
                            data[i].salary + '</td><td>' +
                            data[i].joiningDate + '</td></tr>';
                $('#pageTable tr').first().after(html);
            }

        });
    }

    $('#csvUpload').submit(function (e) {
        var form = $('#csvUpload')[0];
        var formData = new FormData(form);
        e.preventDefault();
        $.ajax({
            url: '/EmployeeManagementService/rest/upload',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            error: function () {
                alert("Error while uploading CSV file!");
            }
        }).then(function (data) {
            $("#downloadLink").show();
            console.log(data);
            alert("Inserted " + data.validRowCount + " records and found " + data.invalidRowCount + " invalid records in the file.");
        });
    });

    $('#downloadLink').click(function (e) {
        e.preventDefault();
        $.ajax({
            url: '/EmployeeManagementService/rest/download',
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            error: function () {
                alert("Error while uploading CSV file!");
            }
        }).then(function (data) {
            download("validatedCsv.csv", data.csvString);
        });
    });

    $('#previous').click(function (e) {
        if (page == 0) {
            alert("No more previous data!");
        } else {
            page = page - 1;
            loadData(e);
        }
    });

    $('#next').click(function (e) {
        page = page + 1;
        loadData(e);
    });

    $('#employeeEditForm').submit(function (e) {
        e.preventDefault();
        $('#errorDetails').hide();
        var form = document.getElementById('employeeEditForm');
        var editUrl = '/EmployeeManagementService/rest/update?';
        for (var i = 0, ii = form.length; i < ii; ++i) {
            var input = form[i];
            if (input.name) {
                editUrl = editUrl + input.name + "=" + input.value + "&";
            }
        }
        console.log(editUrl);
        $.ajax({
            url: editUrl,
            type: 'GET',
            contentType: false,
            processData: false,
            cache: false,
            error: function () {
                alert("Error while adding/updating data!");
            }
        }).then(function (data) {
            console.log(data);
            if (data.status === true) {
                alert("Updated succesfully!");
            } else {
                alert("Data validation error!");
                var display_txt = data.errorDetails.replace(/\n/g, "<br />");
                $('#errorDetails').html(display_txt);
                $('#errorDetails').show();
            }
        });
    });

});