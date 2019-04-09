
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
        $('#mainToInsert').append('<img class="edit-main-image" src="/'+ json[0] + '" alt="">');
    })
    .catch((err) => console.log(err));


fetch('/images/fetch/auction/' + id)
    .then((response) => response.json())
    .then((json) => {
        $('#imagesTitle').empty();
        if (json.length === 0) {
            $('#imagesTitle').append('No images');
        } else {
            $('#imagesTitle').append('Images');
            json.forEach((image) => {
                let currentArticle =
                    '<div class="mb-3 mt-5">\n' +
                    '<div class="row justify-content-center mb-3">\n' +
                    '<div class="col-2"></div>\n' +
                    '<div class="col-8">\n' +
                    '<img class="edit-main-image" src="' + image.path + '" alt="">\n' +
                    '</div>\n' +
                    '<div class="col-2"></div>\n' +
                    '</div>\n' +
                    '<a href="/images/delete/' + image.id + '/' + id + '" class="btn btn-danger w-25">Remove</a>\n' +
                    '</div>';

                $('#allImages').append(currentArticle);
            });
        }

    })
    .catch((err) => console.log(err));
