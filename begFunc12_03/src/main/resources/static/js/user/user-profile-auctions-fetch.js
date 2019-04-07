const urlArr = window.location.pathname.split('/');
const id = urlArr[urlArr.length - 1];

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


function onResize() {
    let el = $('.article-square');
    el.height(el.width());
}

$(window).resize(onResize);
$(document).ready(onResize);

function showUserAuctions(status){
    fetch('/auctions/fetch/user/' + id + '/'+ status)
        .then((response) => response.json())
        .then((json) => {
            $("#auctionHolder").empty();
            $("#statusTitle").empty();
            if(json.length==0){
                if(status==='actives'){
                    $("#statusTitle").append('No actives auctions');
                }else if(status==='finishedWithDeal'){
                    $("#statusTitle").append('No finished auctions with deal');
                }else {
                    $("#statusTitle").append('No finished auctions without deal');
                }
                return;
            }
            if(status==='actives'){
                $("#statusTitle").append('Actives auctions');
            }else if(status==='finishedWithDeal'){
                $("#statusTitle").append('Finished auctions with deal');
            }else {
                $("#statusTitle").append('Finished auctions without deal');
            }

            json.forEach((a) => {
                let currentArticle =
                    '  <article class="home-article" id="'+ a.id +'"' +
                    '                         onclick="showModal(this.getAttribute(\'id\'));">\n' +
                    '                    <div class="article-square">\n' +
                    '                        <img src="' + a.mainImageUrl + '"/>\n' +
                    '                    </div>\n' +
                    '                    <div >' + a.name + '</div>\n' +
                    '                    <div>' + a.currentPrice + '</div>\n' +
                    '                    <div>\n' +
                    '                        <a href="/auctions/details/'+ a.id +'">Details</a>\n' +
                    '                    </div>\n' +
                    '                </article>';


                $("#auctionHolder").append(currentArticle);
            });
            $(document).resize();
        })
        .catch((err) => console.log(err));


}

