/**
 * lab/teacher html 페이지에서 사용하는 javascript
 */

function memberALink(){
	var memberIds = document.querySelectorAll('[id^="detail"]');
	var table = document.querySelector('table');
	table.style.borderCollapse = 'collapse';
	
	memberIds.forEach(function(tr){
		var tds = tr.querySelectorAll('td');
		tr.style.cursor = 'pointer';
		
		tr.addEventListener('mouseover', function(){
	        tds.forEach(function(td) {
	            td.style.borderBottom = '1px solid black';
	        });
		});
		
		tr.addEventListener('mouseout', function(){
			tds.forEach(function(td) {
	            td.style.borderBottom = 'none';
	        });
		});
		
		tr.addEventListener('click', function(){
			var idTd = tr.querySelector('td:nth-of-type(3)');
			var id = idTd.textContent;
			var input = tr.querySelector('td:last-child input');
			var labId = input.value;
			
			location.href='/lab/'+ labId +'/teacherDetail/' + id;
		});
	});
}

function search(){
	let inputValue = $('#searchValue').val().toLowerCase();
	console.log(inputValue);
}

window.onload = function(){
	memberALink();
};