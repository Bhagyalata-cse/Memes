function download(memeFile) {
    var link = document.createElement('a');
    link.href = "/static/images/" + memeFile;
    link.download = memeFile;
    link.style.display = 'none';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}