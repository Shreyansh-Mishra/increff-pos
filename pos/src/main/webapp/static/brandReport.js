
function getBrandReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getBrandReportList(){
    $form = $('#report-form');
    var json = toJson($form);
    json=JSON.parse(json);
    console.log(json);
    //check if brand and category exists as keys in json
    if(!('brand' in json)){
        json['brand']='placeholder';
    }
    if(!('category' in json)){
        json['category']='placeholder';
    }
    if(json['startDate']=='' || json['endDate']=='' || json['brand']=='placeholder' || json['category']=='placeholder'){
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Please fill all the fields!',
        })
        return;
    }
    var startDate = new Date(json['startDate']);
    var endDate = new Date(json['endDate']);
    if(startDate > endDate){
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Start date cannot be greater than end date',
        });
        return;
    }
	var url = getBrandReportUrl()+"/"+json['brand']+"/"+json['category']+"/brand-report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
        jsontocsv(data);
        displayBrandReportList(data);
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


function displayBrandReportList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
    console.log(data);
	for(var i in data){
		var e = data[i];
        var buttonHtml = ' <button class="btn btn-link btn-sm btn-rounded" onclick="displayWholeBrandReport('+e.id+')">view</button>'
		var row = '<tr>'
        + '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
}


let csv = '';

function jsontocsv(data){
    if(data.length==0){
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'No data found!',
        })
        return;
    }
    csv = '';
	const keys = Object.keys(data[0]);
	csv += keys.join(',') + '\n';
	data.forEach(item=>{
		csv += Object.values(item).join(',') + '\n';
	})
}

function downloadCSV(){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'brand_report.csv';
	hiddenElement.click();
}


function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function populateBrandDropDown(){
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var brandSet = new Set();
			for(var i in data){
				var e = data[i];
				brandSet.add(e.brand);
			}
            data = Array.from(brandSet);
            var $select = $('#inputBrand');
			$select.append('<option value="all">' + 'All' + '</option>');
            for(var i in data){
                var e = data[i];
                var option = '<option value="' + e + '">' + e + '</option>';
                $select.append(option);
            }
        },
        error: handleError
    });
}

function populateCategoryDropdown(){
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var categorySet = new Set();
            for(var i in data){
                var e = data[i];
                categorySet.add(e.category);
            }
            data = Array.from(categorySet);
            var $select = $('#inputCategory');
			$select.append('<option value="all">' + 'All' + '</option>');
            $select.removeAttr('disabled');
            for(var i in data){
                var e = data[i];
                var option = '<option value="' + e + '">' + e + '</option>';
                $select.append(option);
            }
        },
        error: handleError
    });
}




//INITIALIZATION CODE
function init(){
    $('#get-brand-report').click(getBrandReportList);
    $("#endDate").prop("max", function(){
        return new Date().toJSON().split('T')[0];
    });
    $("#download-report").click(downloadCSV);
    $('#inputCategory').on('click', (e)=>{
        if(e.target.value == "placeholder")
            populateCategoryDropdown();
    });
    $('#inputBrand').on('click', (e)=>{
        if(e.target.value == "placeholder")
            populateBrandDropDown();
    });
}



function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}


// $(document).ready(populateBrandDropDown);

$(document).ready(init);
