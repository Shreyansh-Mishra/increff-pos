
function getOrderUrl(){
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
function addOrder(event){
	//Set the values to update
	var $form = $("#order-form");
	var json = toJson($form);
	var url = getOrderUrl()+"/add-order";

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();  
	   },
	   error: (response)=>{
			orderError(response,false);
		}
	});

	return false;
}

function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();	
	var url = getOrderUrl() + id;

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
	   		getOrderList();   
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});

	return false;
}


function getOrderList(){
	//empty all the error present in form
	$("#order-form").find(".alert").remove();
	var url = getOrderUrl()+"/get-orders";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: (response)=>{
		orderError(response,false);
		}
	});
}

function deleteOrder(id){
	var url = getOrderUrl() + "/delete/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderList();  
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
	var url = getOrderUrl()+"/add-order";
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

function displayOrderList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];

		var buttonHtml = ' <button onclick="displayEditOrder(' + e.id + ')">edit</button>'
        buttonHtml += ' <button onclick="displayWholeOrder('+e.id+')">view</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + e.time.toString() + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
}

function displayWholeOrder(id){
    var url = getOrderUrl() + "/" + id;
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
       		displayOrderItems(data);   
       },
       error: (response)=>{
        orderError(response,false);
        }
    });	
}

function displayOrderItems(data){
    var $tbody = $('#dtBasicExample2').find('tbody');
    $tbody.empty();
    let j=1;
    for(var i in data){
        var e = data[i];
        var row = '<tr>'
        + '<td>' + j + '</td>'
        + '<td>' + e.orderId + '</td>'
        + '<td>'  + e.productId + '</td>'
        + '<td>' + e.quantity + '</td>'
        + '<td>' + e.sellingPrice + '</td>'
        + '</tr>';
        $tbody.append(row);
        j++;	
    }
    paginate("#dtBasicExample2");
    $('#view-order-modal').modal('toggle');
}

function displayEditOrder(id){
	var url = getOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data);   
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

function displayOrder(data){
	$("#order-edit-form input[name=name]").val(data.name);	
	$("#order-edit-form input[name=age]").val(data.age);	
	$("#order-edit-form input[name=id]").val(data.id);	
	$('#edit-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order').click(addOrder);
	$('#update-order').click(updateOrder);
	$('#refresh-data').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#orderFile').on('change', updateFileName)
}

function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getOrderList);

