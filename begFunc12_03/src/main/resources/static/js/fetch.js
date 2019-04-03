function showBiddingsModal() {

    const urlArr = window.location.pathname.split('/');
    console.log(urlArr);
    const id = urlArr[urlArr.length - 1];
    console.log(id);

    console.log('before');

    $('#all-biddings-table').empty();
    $('#all-biddings-table').append(
        '<table class="table table-striped">' +
        '<thead>' +
        '<tr>' +
        '<th scope="col">Username</th>' +
        '<th scope="col">Step</th>' +
        '<th scope="col">Reached price</th>' +
        '<th scope="col">Submitted On</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody id="tableBiddings">');
    console.log('before');


    function Get(yourUrl){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("GET",yourUrl,false);
        Httpreq.send(null);
        return Httpreq.responseText;
    }

    var json_obj = JSON.parse(Get('http://localhost:8000/fetch/auctions/biddings/10'));
    console.log("this is the author name: "+json_obj);
    console.log(json_obj);



    fetch('http://localhost:8000/fetch/auctions/biddings/10', ) // Fetch the data (GET request)
        .then((response) => response.json()) // Extract the JSON from the Response
        .then((json) => json.forEach((x) => { // Render the JSON data to the HTML
            console.log('before');
            let currentRow = '<tr >' +
                '<td>' +
                '<a href="/users/profile/' + x.participantId + '">' + x.participantUsername + '</a>\n' +
                '</td>' +
                '<td class="table-reached-price">' + x.biddingStep + '\u20AC</td>' +
                '<td class="table-reached-price-bold">' + x.reachedPrice + '\u20AC</td>' +
                '<td>' + x.submittedOn + '</td>' +
                '</tr>';

            $('#tableBiddings').append(currentRow);

        }))
        .then(() => {
            $('#all-biddings-table').append('</tbody>\n' +
                '</table>');
            $('#biddingModalScrollable').modal('show');
        })
        .catch((err) => console.log(err));
    console.log('after');
}