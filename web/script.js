function scrollToId(id) {
    var target = document.getElementById(id);
    window.scrollBy({
        top: target.getBoundingClientRect().top-35,
        left: 0,
        behavior: 'smooth'
    });
}
