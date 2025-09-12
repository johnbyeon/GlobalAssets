document.addEventListener("DOMContentLoaded", function () {
    const ads = document.querySelectorAll(".ad-item");
    let current = 0;

    if (ads.length > 0) {
        // 초기화
        ads.forEach((ad, index) => {
            ad.style.display = index === 0 ? "block" : "none";
        });

        setInterval(() => {
            ads[current].style.display = "none";
            current = (current + 1) % ads.length;
            ads[current].style.display = "block";
        }, 3000); // 3초마다 교체
    }
});
