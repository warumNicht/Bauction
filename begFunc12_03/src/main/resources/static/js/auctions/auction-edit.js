
$('input[type="file"]').on('change', function () {
    let filenames = [];
    let files = document.getElementById('files').files;

    for (let i in files) {
        if (files.hasOwnProperty(i)) {
            filenames.push(files[i].name);
        }
    }
    $(this).next('.custom-file-label').addClass("selected").html(filenames.join(', '));
});

$('#mainImage').on('change', function () {
    let fileName = $(this).val().split('\\').pop();
    $(this).next('.custom-file-label').addClass("selected").html(fileName);
});

function showCollectionFields() {
    let element = document.getElementById('category');
    let category = element.options[element.selectedIndex].text;
    let collectionDiv = document.getElementById('collectionFields');


    if (category === 'Coins' || category === 'Banknotes') {
        // element.disabled = true;
        $('#collectionTitle').empty();
        collectionDiv.style.display = "block";
        let coinSpecific = document.getElementById('coinSpecific');
        let banknoteSpecific = document.getElementById('banknoteSpecific');

        if (category === 'Coins') {
            $('#collectionTitle').append('Coin specifications:');
            coinSpecific.style.display = "block";
            banknoteSpecific.style.display = "none";
        } else {
            $('#collectionTitle').append('Banknote specifications:');
            coinSpecific.style.display = "none";
            banknoteSpecific.style.display = "block";
        }

    } else {
        collectionDiv.style.display = "none";
    }
}

const urlArr = window.location.pathname.split('/');
const id = urlArr[urlArr.length - 1];

let mainImage = document.getElementById('insertMainImage');
mainImage.style.display = "block";

let allImages = document.getElementById('insertImages');
allImages.style.display = "block";


fetch('/images/fetch/main/'+id)
    .then((response) => response.json())
    .then((json) => {
        $('#mainToInsert').append('<img class="edit-main-image" src="'+ json[0] + '" alt="">');
    })
    .catch((err) => console.log(err));



