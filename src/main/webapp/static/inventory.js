
function getorderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function orderError(response, edit){
	let $form;
	if(edit==false)
		$form = $("#order-form");
	else
		$form = $("#order-edit-form");
	$form.find(".alert").remove();
	$form.append(`<div class="alert alert-danger" role="alert">Error : ${JSON.parse(response.responseText).message}</div>`)
	   
}

//BUTTON ACTIONS
function addorder(event){
	//Set the values to update
	var $form = $("#order-form");
	var json = toJson($form);
	var url = getorderUrl()+"/add-product";

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getorderList();  
	   },
	   error: (response)=>{
			orderError(response,false);
		}
	});

	return false;
}

function updateorder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();	
	var url = getorderUrl() + id;

	//Set the values to update
	var $form = $("#order-edit-form");
	let jsoni = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: jsoni,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getorderList();   
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});

	return false;
}


function getorderList(){
	//empty all the error present in form
	$("#order-form").find(".alert").remove();
	var url = getorderUrl()+"/get-order";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayorderList(data);  
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});
}

function deleteorder(id){
	var url = getorderUrl() + "/delete/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getorderList();  
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#orderFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getorderUrl()+"/add-product";
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayorderList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];

		var buttonHtml = ' <button onclick="displayEditorder(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate();
}

function displayEditorder(id){
	var url = getorderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayorder(data);   
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#orderFile');
	var fileName = $file.val();
	$('#orderFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-order-modal').modal('toggle');
}

function displayorder(data){
	$("#order-edit-form input[name=name]").val(data.name);	
	$("#order-edit-form input[name=age]").val(data.age);	
	$("#order-edit-form input[name=id]").val(data.id);	
	$('#edit-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order').click(addorder);
	$('#update-order').click(updateorder);
	$('#refresh-data').click(getorderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#orderFile').on('change', updateFileName)
}

function paginate() {
	$('#dtBasicExample').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getorderList);

