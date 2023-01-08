
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function inventoryError(response, edit){
	let $form;
	if(edit==false)
		$form = $("#inventory-form");
	else
		$form = $("#inventory-edit-form");
	$form.find(".alert").remove();
	$form.append(`<div class="alert alert-danger" role="alert">Error : ${JSON.parse(response.responseText).message}</div>`)
	   
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl()+"/add-product";

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();  
	   },
	   error: (response)=>{
			inventoryError(response,false);
		}
	});

	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();	
	var url = getInventoryUrl() + "/update/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	let jsoni = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: jsoni,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();   
	   },
	   error: (response)=>{
		inventoryError(response,false);
		}
	});

	return false;
}


function getInventoryList(){
	//empty all the error present in form
	$("#inventory-form").find(".alert").remove();
	var url = getInventoryUrl()+"/get-inventory";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: (response)=>{
		inventoryError(response,false);
		}
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/delete/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();  
	   },
	   error: (response)=>{
		inventoryError(response,false);
		}
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
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
	var url = getInventoryUrl()+"/add-product";
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

function displayInventoryList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];

		var buttonHtml = ' <button onclick="displayEditInventory(' + e.id + ')">edit</button>'
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

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: (response)=>{
		inventoryError(response,false);
		}
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=name]").val(data.name);	
	$("#inventory-edit-form input[name=age]").val(data.age);	
	$("#inventory-edit-form input[name=id]").val(data.id);	
	$('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
}

function paginate() {
	$('#dtBasicExample').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getInventoryList);

