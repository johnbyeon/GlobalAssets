document.addEventListener('DOMContentLoaded', function() {
    // URL 해시로 탭 활성화
    const hash = window.location.hash;
    const initialTab = hash ? hash.substring(1) : 'user';
    const initialTabBtn = document.querySelector(`#adminTab button[data-bs-target="#${initialTab}"]`);
    if(initialTabBtn){
        new bootstrap.Tab(initialTabBtn).show();
    }

    // 탭 클릭 시 콘텐츠 로드 및 URL 해시에 반영
    document.querySelectorAll('#adminTab button[data-bs-toggle="tab"]').forEach(btn => {
        btn.addEventListener('shown.bs.tab', e => {
            const tabId = e.target.getAttribute('data-bs-target').substring(1);
            window.location.hash = e.target.getAttribute('data-bs-target');
            loadTabContent(tabId);
        });
    });

    // 처음 활성 탭 로드
    loadTabContent(initialTab);
});

// =======================
// 탭 AJAX 콘텐츠 로드
// =======================
function loadTabContent(tabId){
    const container = document.getElementById(tabId);
    if(container.getAttribute('data-loaded')) return;

    let url = '';
    switch(tabId){
        case 'user': url='/get/userList'; break;
        case 'board': url='/get/boardList'; break;
        case 'image-upload': url='/get/imgUp'; break;
        case 'advertisement': url='/get/adv'; break;
        default: return;
    }

    fetch(url)
        .then(res => {
            if(!res.ok) throw new Error('HTTP ' + res.status);
            return res.text();
        })
        .then(html => {
            container.innerHTML = html;
            container.setAttribute('data-loaded','true');

            if(tabId==='image-upload') initImageUpload();
            if(tabId==='advertisement') initAdvertisement();
        })
        .catch(err => {
            console.error(err);
            container.innerHTML = `<div class="alert alert-danger">로드 실패: ${err.message}</div>`;
        });
}

// =======================
// 이미지 업로드 초기화
// =======================
function initImageUpload(){
    const fileInput = document.getElementById("fileInput");
    const preview = document.getElementById("preview");

    if(fileInput){
        fileInput.addEventListener("change", function () {
            const file = fileInput.files[0];
            if(file){
                const reader = new FileReader();
                reader.onload = function(e){
                    preview.innerHTML = `<img src="${e.target.result}" alt="미리보기" style="max-width:300px;">`;
                };
                reader.readAsDataURL(file);
            } else {
                preview.innerHTML = "";
            }
        });
    }
}

async function uploadFile(){
    const file = fileInput.files[0];
            const link = linkInput.value.trim();

            if (!file) {
                alert("파일을 선택하세요");
                return;
            }
            if (!link) {
                alert("광고 링크 URL을 입력하세요");
                return;
            }

            // FormData로 파일과 링크 전송
            const formData = new FormData();
            formData.append("file", file);
            formData.append("linkPath", link);

            // Presigned URL 요청
            const response = await fetch(`/s3/presigned-url?filename=${file.name}`, {
                method: "POST",
                body: formData
            });
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


// =======================
// 광고 관리 초기화
// =======================
function initAdvertisement(){
    const sortableEl = document.getElementById('sortable-ads');
    if(sortableEl) new Sortable(sortableEl, {animation:150});

    // 광고 순서 저장 버튼
    const saveBtn = document.getElementById('saveAdOrderBtn');
    saveBtn.addEventListener('click', async () => {
        const list = document.querySelectorAll("#sortable-ads li");
        const order = Array.from(list).map((item,index)=>({
            adventId: item.getAttribute("data-id"),
            sortNum: index+1
        }));

        const res = await fetch("/admin/advertisement/order", {
            method: "POST",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify(order)
        });

        if(res.ok){
            alert("순서가 저장되었습니다 ✅");
            location.reload();
        } else {
            alert("저장 실패 ❌");
        }
    });
}

// 광고 선택 시 왼쪽 미리보기에 표시
function showAdPreview(imagePath){
    const preview = document.getElementById('adPreview');
    preview.innerHTML = `<img src="${imagePath}" alt="선택 광고" style="max-width:100%; border-radius:6px;">`;
}