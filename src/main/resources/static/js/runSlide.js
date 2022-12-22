export default function runSlides() {
  let items = document.getElementsByClassName("carousel-item");
  let dots = document.getElementsByClassName("carousel-inidicator");

  let btnForward = document.getElementById("btn-forward");
  let btnBack = document.getElementById("btn-back");

  btnForward.addEventListener("click", () => {
    nextSlide();
  });

  btnBack.addEventListener("click", () => {
    prevSlide();
  });

  for (let i = 0; i < dots.length; i++) {
    dots[i].addEventListener("click", () => {});
  }

  autoSlide();
  function nextSlide() {
    let currentIndex = 0;
    for (let i = 0; i < items.length; i++) {
      let currentItem = items[i];

      if (currentItem.classList.contains("active-carousel-item")) {
        currentItem.classList.remove("active-carousel-item");
        if (i + 1 === items.length) {
          currentIndex = 0;
        } else {
          currentIndex = i + 1;
        }
        break;
      }
    }

    if (currentIndex === 0) {
      dots[dots.length - 1].classList.remove("active-dot");
    } else {
      dots[currentIndex - 1].classList.remove("active-dot");
    }
    dots[currentIndex].classList.add("active-dot");
    items[currentIndex].classList.add("active-carousel-item");
  }
  function prevSlide() {
    let currentIndex = 0;
    for (let i = 0; i < items.length; i++) {
      let currentItem = items[i];

      if (currentItem.classList.contains("active-carousel-item")) {
        currentItem.classList.remove("active-carousel-item");

        currentIndex = i;
        if (i === 0) {
          items[items.length - 1].classList.add("active-carousel-item");
        } else {
          items[i - 1].classList.add("active-carousel-item");
        }
        break;
      }
    }

    dots[currentIndex].classList.remove("active-dot");
    if (currentIndex === 0) {
      dots[dots.length - 1].classList.add("active-dot");
    } else {
      dots[currentIndex - 1].classList.add("active-dot");
    }
  }

  for (let i = 0; i < dots.length; i++) {
    let currentDot = dots[i];
    currentDot.addEventListener("click", () => {
      deleteActiveDot();
      cleanCurrentSlide();
      currentDot.classList.add("active-dot");
      items[i].classList.add("active-carousel-item");
    });
  }

  function deleteActiveDot() {
    for (let element of dots) {
      if (element.classList.contains("active-dot")) {
        element.classList.remove("active-dot");
      }
    }
  }
  function cleanCurrentSlide() {
    for (const item of items) {
      item.classList.remove("active-carousel-item");
    }
  }

  function autoSlide() {
    nextSlide();
    setTimeout(autoSlide, 5000);
  }
}
