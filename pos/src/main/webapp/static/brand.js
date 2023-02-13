
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//BUTTON ACTIONS
function addBrand(brand, category){
	//Set the values to update
	var json = {brand: brand,category: category}
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList();
			handleSuccess("Brand added successfully");  
	   },
	   error: (response)=>{
			handleError(response);
		}
	});

	return false;
}

function updateBrand(id, brandName, category){
	//Get the ID
	var url = getBrandUrl() + "/" + id;
	var brandData = {brand: brandName,category: category}
	console.log(brandData,JSON.stringify(brandData));
	//Set the values to update
	// var $form = $("#brand-edit-form");
	// let jsoni = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(brandData),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			handleSuccess("Brand updated successfully");
	   		getBrandList();   
	   },
	   error: (response)=>{
		handleError(response);
		}
	});

	return false;
}


function getBrandList(){
	//empty all the error present in form
	$("#brand-form").find(".alert").remove();
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);  
	   },
	   error: (response)=>{
		handleError(response);
		}
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


document.getElementById('brandtsv').addEventListener('submit', (e)=>{
	e.preventDefault();
	let url = $("meta[name=baseUrl]").attr("content") + "/api/file?entity=brand";
	axios.post(url, new FormData(e.target)).then((res)=>{
		if(res.data!=""){
		const blob = new Blob([res.data], {type: 'text/tsv'});
		const url = window.URL.createObjectURL(blob);
		const link = document.createElement('a');
		link.href = url;
		link.download = "error.tsv";
		link.click();
		}
		else{
			$('#upload-brand-modal').modal('toggle');
			handleSuccess("All brands uploaded successfully");
			getBrandList();
		}
	}).catch(err=>{
		Swal.fire({title: "Error",text: "Invalid TSV file",icon: "error",});
	})
})






//UI DISPLAY METHODS

function displayBrandList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];
		//get role from div with class get-role which contains a span inside it with role
		var role = $('.get-role').find('span').text();
		console.log(role);
		if(role=='operator'){
			var row = '<tr>'
			+ '<td>' + j + '</td>'
			+ '<td>' + e.brand + '</td>'
			+ '<td>'  + e.category + '</td>'
			+ '</tr>';	
		}
		else{
		var buttonHtml = ' <button class="btn btn-link btn-sm btn-rounded" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		}
        $tbody.append(row);
		j++;	
	}
	paginate();
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);   
	   },
	   error: (response)=>{
		handleError(response);
		}
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}



function updateFileName(){
	var $file = $('#brandFile');
	console.log($file.val());
	var fileName = $file.val();
	$('#brandFileName').html(fileName.split('\\')[fileName.split('\\').length-1]);
	fileName = fileName.split('\\')[fileName.split('\\').length-1].split('.')[1];
	if(fileName!='tsv'){
		Swal.fire({title: "Error",text: "Invalid file type",icon: "error",});
		return;
	}
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}


function displayBrand(data){	
	// $('#edit-brand-modal').modal('toggle');
	Swal.fire({
		title: 'Edit Brand',
		width: "40%",
		html:`<form class="form-inline" id="#brand-edit-form">
		<div class="container">
		<div class="form-outline row">
		<label class="col" for="brand">Brand</label>
		<input placeholder="Brand Name" value="${data.brand}" id="brand" type="text" name="brand" class="swal2-input col" />
		</div>
		<div class="form-outline row">
		<label class="col" for="category">Category</label>
		<input placeholder="Category" value="${data.category}" id="category" type="text" name="category" class="swal2-input col" />
		</div>
		<input id="id" type="hidden" value=${data.id} name="id">
		</div>
		</form>`,
  		showCancelButton: true,
  		confirmButtonText: `Save`,
  		denyButtonText: `Don't save`,
		preConfirm: () => {
			let id = Swal.getPopup().querySelector('#id').value;
			let brand = Swal.getPopup().querySelector('#brand').value;
			let category = Swal.getPopup().querySelector('#category').value;
			return {id,brand,category}
		}
	}).then((result)=>{
		updateBrand(result.value.id,result.value.brand,result.value.category);

	})
}

function displayBrandForm(){
	Swal.fire({
		title: 'Add Brand',
		width: "40%",
		html:`<form class="form-inline" id="#brand-form">
		<div class="container">
		<div class="form-outline row">
		<label class="col" for="brand">Brand</label>
		<input placeholder="Brand Name" id="brand" type="text" name="brand" class="swal2-input col" />
		</div>
		<div class="form-outline row">
		<label class="col" for="category">Category</label>
		<input placeholder="Category" id="category" type="text" name="category" class="swal2-input col" />
		</div>
		</div>
		</form>`,
		showCancelButton: true,
		confirmButtonText: `Add brand`,
		preConfirm: () => {
			let brand = Swal.getPopup().querySelector('#brand').value;
			let category = Swal.getPopup().querySelector('#category').value;
			return {brand,category}
		}
	}).then((result)=>{
		addBrand(result.value.brand,result.value.category);
	})
}


//INITIALIZATION CODE
function init(){
	$('#add-brand').click(displayBrandForm);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayUploadData);
    $('#brandFile').on('change', updateFileName);
}

function paginate() {
	$('#dtBasicExample').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getBrandList);


