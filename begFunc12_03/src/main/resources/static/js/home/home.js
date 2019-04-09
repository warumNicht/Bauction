function onResize() {
    let el = $('.article-square');
    el.height(el.width());
}

$(window).resize(onResize);
$(document).ready(onResize);

function onClick() {
    let article = $('.article-square');
    $('.article-square').css({
        'box-shadow': 'box-shadow: -2px -1px 15px 5px #585858;'
    });
}

$(document).ready(onResize);