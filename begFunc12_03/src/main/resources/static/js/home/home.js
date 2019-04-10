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

function showSortOptions() {
    $('#homeAllArticles').empty();
    showElement('sortDiv');
    fetchCategories();
}
function sort() {
    let selectOrdering = document.getElementById('sortingType');
    let orderCriteria= selectOrdering.options[selectOrdering.selectedIndex].text;

    let categories = document.getElementById('category');
    let selectedCategory= categories.options[categories.selectedIndex].text;

    showSortedAuctions(orderCriteria, selectedCategory);
}
function showElement(elementId) {
    let x = document.getElementById(elementId);
    x.style.display = "block";
}
function fetchCategories() {
    fetch('/categories/all')
        .then((response) => response.json())
        .then((json) => {
            $('#category').empty();
            $('#category').append('<option value="all">All</option>');
            json.forEach((category) => {
                $('#category').append('<option value="'+ category +'">'+ category +'</option>');
            });
        })
        .catch((err) => console.log(err));
}

function showSortedAuctions(orderCriteria, selectedCategory) {
    fetch('/auctions/fetch/sort/'+ selectedCategory +'/' +orderCriteria)
        .then((response) => response.json())
        .then((json) => {
            $('#homeAllArticles').empty();
            $('#emptyMessage').empty();
            if(json.length===0){
                $('#emptyMessage').append(' <h2 class="text-center">No results</h2>');
            }else {
                json.forEach((a) => {
                    let currentAuction=' <article class="home-article" id="'+ a.id +'" ' +
                        '                         onclick="showModal(this.getAttribute(\'id\'));">' +
                        '<div class="article-square">' +
                        '     <img src="'+ a.mainImageUrl +'"/>' +
                        '</div>' +
                        '<div>'+ a.name +'</div>' +
                        '<div>' + a.currentPrice + '</div>' +
                        '<div>\n' +
                        '    <a href="/auctions/details/' + a.id +'">Details</a>' +
                        '</div>' +
                        '</article>';

                    $('#homeAllArticles').append(currentAuction);
                });
                $(document).resize();
            }
        })
        .catch((err) => console.log(err));


}

function Get(yourUrl) {
    let Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET", yourUrl, false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

function showModal(id) {

    let x = JSON.parse(Get('/auctions/fetch/' + id));

    $("#auction-name").empty().append(x.name);
    $("#main-image").empty().append('<img class="modal-div" src="' + x.mainImageUrl + '"/>');
    $("#price").empty().append(x.currentPrice);
    $("#seller").empty().append(x.seller);
    $("#time").empty().append(x.expiresAt);
    $("#town").empty().append(x.town);
    $("#details").empty().append('<a href="/auctions/details/' + x.id + '"  class="btn btn-primary">Details</a>');

    $('#homeModal').modal('show');
}