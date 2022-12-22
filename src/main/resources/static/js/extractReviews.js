
const url = "http://localhost:8080/api/v1/app/reviews/animes";
let reviewsContainer = document.getElementsByClassName("reviews")[0];

fetch(url)
    .then(data => data.json())
    .then(exports => {
        if(exports.length === 0){
            reviewsContainer.innerHTML = "<h4>Thid field is empty :(</h4>"
        }else{
            for (let i = 0; i <exports.length ; i++) {
                let reviewTemplate = reviewFormat(exports[i].imageUrl,exports[i].animeTitle,exports[i].content,exports[i].published,exports[i].username)
                reviewsContainer.appendChild(reviewTemplate);
            }
        }
    });


function reviewFormat(imageUrl,animeTitle,content,created,username){
    let containerReview = document.createElement("div");
    containerReview.classList.add("review-template");

    let containerImage = document.createElement("div");
    containerImage.classList.add("image-review");

    let imageAnime = document.createElement("img");
    imageAnime.src = imageUrl;
    containerImage.appendChild(imageAnime);
    containerReview.appendChild(containerImage);


    let contentReview = document.createElement("div");
    contentReview.classList.add("content-review");

    let titleH5 = document.createElement("h5");
    titleH5.innerText = animeTitle;
    contentReview.appendChild(titleH5);

    let paragraph = document.createElement("p");
    paragraph.innerText = content;
    contentReview.appendChild(paragraph);

    let dateParagraphContainer = document.createElement("p");

    dateParagraphContainer.innerHTML = `<b>${created} </b> by <b>${username}</b>`;



    contentReview.appendChild(dateParagraphContainer);
    containerReview.appendChild(contentReview);

    return containerReview;
}