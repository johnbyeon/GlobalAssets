// 페이지 로드 시 URL 해시로 탭 활성화
document.addEventListener('DOMContentLoaded', function() {
    const hash = window.location.hash;
    if(hash) {
        const tabTrigger = new bootstrap.Tab(document.querySelector(`[data-bs-target="${hash}"]`));
        tabTrigger.show();
    }

    // 탭 클릭 시 URL 해시에 반영
    const tabLinks = document.querySelectorAll('#adminTab button[data-bs-toggle="tab"]');
    tabLinks.forEach(btn => {
        btn.addEventListener('shown.bs.tab', function (event) {
            window.location.hash = event.target.getAttribute('data-bs-target');
        });
    });
});

const fileInput = document.getElementById("fileInput");
  const preview = document.getElementById("preview");


// 로고 이미지 업로드
  if (fileInput) {
    // 파일 선택 시 미리보기
    fileInput.addEventListener("change", function () {
      const file = fileInput.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
          preview.innerHTML = `<img src="${e.target.result}" alt="미리보기" style="max-width:300px;">`;
        };
        reader.readAsDataURL(file);
      } else {
        preview.innerHTML = "";
      }
    });
  }

  // 파일 업로드
  async function uploadFile() {
    const file = fileInput.files[0];
    if (!file) {
      alert("파일을 선택하세요");
      return;
    }

    // Presigned URL 요청
    const response = await fetch("/s3/presigned-url?filename=" + file.name, {method: "POST"});
    const data = await response.json();

    // S3 업로드
    const put = await fetch(data.url, {
      method: "PUT",
      headers: {"Content-Type": data.contentType},
      body: file
    });

    if (put.ok) {
      preview.innerHTML = `<div class="alert alert-success mt-3">업로드 완료되었습니다 ✅</div>`;
    } else {
      preview.innerHTML = `<div class="alert alert-danger mt-3">업로드 실패 ❌</div>`;
    }
  }



// 광고 탭
   // 드래그 앤 드롭 활성화
    new Sortable(document.getElementById('sortable-ads'), {
      animation: 150
    });

    // 순서 저장
    async function saveAdOrder() {
      const list = document.querySelectorAll("#sortable-ads li");
      const order = Array.from(list).map((item, index) => ({
        adventId: item.getAttribute("data-id"),
        sortNum: index + 1
      }));

      const response = await fetch("/admin/advertisement/order", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(order)
      });

      if (response.ok) {
        alert("순서가 저장되었습니다 ✅");
        location.reload();
      } else {
        alert("저장 실패 ❌");
      }
    }

    // 새 광고 업로드
    document.getElementById("uploadAdForm").addEventListener("submit", async (e) => {
      e.preventDefault();
      const formData = new FormData(e.target);

      const response = await fetch("/admin/advertisement/upload", {
        method: "POST",
        body: formData
      });

      if (response.ok) {
        alert("광고가 등록되었습니다 ✅");
        location.reload();
      } else {
        alert("업로드 실패 ❌");
      }
    });