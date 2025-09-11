// ad-rotation.js
document.addEventListener("DOMContentLoaded", function () {
    const adSections = document.querySelectorAll(".ad-section");

    adSections.forEach(section => {
        const ads = section.querySelectorAll(".ad-item");
        let currentIndex = 0;

        if (ads.length > 0) {
            // 초기 첫 광고 보이기
            ads.forEach((ad, idx) => ad.style.display = idx === 0 ? "block" : "none");

            setInterval(() => {
                ads[currentIndex].style.display = "none";
                currentIndex = (currentIndex + 1) % ads.length;
                ads[currentIndex].style.display = "block";
            }, 10000); // 10초마다 전환
        }
    });
});
