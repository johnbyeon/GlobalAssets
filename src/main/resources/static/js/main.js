document.addEventListener('DOMContentLoaded', function() {
    fetch('/fragments/header')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
        })
        .catch(error => console.error('헤더 로드 오류:', error));

    fetch('/fragments/footer')
        .then(response => response.text())
        .then(data => {
            document.getElementById('footer').innerHTML = data;
        })
        .catch(error => console.error('푸터 로드 오류:', error));
});