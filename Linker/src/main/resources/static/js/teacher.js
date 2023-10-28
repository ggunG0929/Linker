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
			var input = tr.querySelector('td:last-child input');
			const id = input.value;
			const labId = $('#labId').val();
			
			location.href='/lab/'+ labId +'/teacherDetail/' + id;
		});
	});
}

const param = window.location.search;
const url = new URLSearchParams(param);
const prValue = url.get('page');
	
function leftStop(){
	if(prValue == 0 || prValue === null){
		$('#left').click(function(e){
			e.preventDefault();
		});
	}
}

function rightStop(){
	const page = $('#totalPage').val();
	
	if(prValue == page){
		$('#right').click(function(e){
			e.preventDefault();
		});
	}
}

function statusChange(){
	const state = $('#labMemberStatus').val();
	const labId = $('#labId').val();
	
	location.href='/lab/'+ labId +'/teacher?page=0&state='+state;
}

function valid(){
	const keyword = $('#search').val().trim();
	
	if(keyword.length === 0){
		alert('검색어를 입력해주세요.');
		return false;
	}
}

window.onload = function(){
	memberALink();
	leftStop();
	rightStop();
};