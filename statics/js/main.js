const toggleBtn = document.querySelector('.navbar-toggleBtn');
const menu = document.querySelector('.navbar-menu');


// Toggle Nav

toggleBtn.addEventListener('click', () => {
  menu.classList.toggle('nav-active');

  toggleBtn.classList.toggle('toggle');
})


// Hide header
var lastScrollTop = 0;
var delta = 5;
var navbarHeight = document.querySelector('.navbar').offsetHeight;
var navbar = document.querySelector('.navbar')
var didScroll;

window.scroll(function(event){
  didScroll = true;
})

setInterval(function() {
  if (didScroll) {
    hasScrolled();
    didScroll = false;
  }
}, 250);

function hasScrolled() {
  var st = this.scrollTop();
  if (Math.abs(lastScrollTop - st)<= delta) {
    return;
  }

  if (st > lastScrollTop && st > navbarHeight) {
    navbar.removeClass('navbar-down').addClass('navbar-up');
  } else {
    if (st + window.height() < document.height()) {
      navbar.removeClass('navbar-up').addClass('navbar-down');
    }
  }
  lastScrollTop = st;
}



// Btn funcs
function post_confirm(url) {
	if (confirm('게시글을 등록하시겠습니까?')) {
		// Yes click
	
		document.newPost.action = url;
		document.newPost.method = "post";
		document.newPost.submit();
	} else {
		// no click

	}
}

function cancel_confirm(url) {
    if (confirm('게시글 작성을 종료하시겠습니까?')) {
        // Yes click
        location.href = url;
    } else {
        // no click
    }
}

function delete_confirm(url) {
    if (confirm('게시글을 삭제하시겠습니까?')) {// Yes click

        location.href = url;
        location.submit();
    } else {
        // no click

    }
}

function intoHTML(className, text) {
	console.log("test" + className);
	document.querySelector(className).innerHTML = text;
}


// Paging
function paging() {

}

// 게시글의 사진크기 조절하는 함수
function imageFit() {
    const imgArr = document.querySelectorAll("img");

    for (let i = 0; i < imgArr.length; i++) {
        imgArr[i].style.maxWidth = imgArr[i].style.width;
        imgArr[i].style.maxHeight = imgArr[i].style.height;
        if (imgArr[i].width > window.innerWidth + 30 /*padding*/) {
            imgArr[i].style.width = "100%";
            imgArr[i].style.height = "auto";
        }
    }
}